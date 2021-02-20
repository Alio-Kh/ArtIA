package com.moisegui.artia.ui.search;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moisegui.artia.R;
import com.moisegui.artia.ShowPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchFragment extends Fragment {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA_TAKEPICTURE"};

    private boolean safeToTakePicture = false;

    private Camera mCamera = null;
    private CameraPreview mPreview;
    private SearchViewModel searchViewModel;
    private PictureCallback picture;
    View root;
    private final int CODE_ACTIVITY = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_search, container, false);


        if (checkCameraHardware(getContext())) {

            if (!allPermissionGranted()) {

                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            } else {
                // Create an instance of Camera
                prepareInterface();
            }

        }

        return root;
    }

    private void prepareInterface() {
        mCamera = getCameraInstance();

        if (mCamera != null) {

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(getContext(), mCamera);
            safeToTakePicture = true;

            FrameLayout preview = (FrameLayout) root.findViewById(R.id.camera_preview);
            preview.addView(mPreview);

            picture = new PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    File pictureFile = getOutputMediaFile();
                    if (pictureFile == null) {
                        Log.d("TAG", "Error creating media file, check storage permissions");
                        Toast.makeText(getContext(), "There was an error while trying to save your photo. please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                        safeToTakePicture = true;
                        Intent intent = new Intent(getActivity(), ShowPhoto.class);
                        intent.putExtra("path", pictureFile.getAbsolutePath().toString());
                        startActivityForResult(intent, CODE_ACTIVITY);
                    } catch (FileNotFoundException e) {
                        Log.d("TAG", "File not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.d("TAG", "Error accessing file: " + e.getMessage());
                    }
                }
            };


            // Add a listener to the Capture button
            FloatingActionButton captureButton = (FloatingActionButton) root.findViewById(R.id.button_capture);
            captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (safeToTakePicture) {
                                // get an image from the camera
                                safeToTakePicture = false;
                                mCamera.takePicture(null, null, picture);
                            }

                        }
                    }
            );


        } else {
            Log.d("Search Fragment", "Camera was null");

        }


    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            c.setDisplayOrientation(90);
            // get Camera parameters
            Camera.Parameters params = c.getParameters();
            // set the focus mode
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            // set Camera parameters
            c.setParameters(params);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Create a file Uri for saving an image
     */
    private static Uri getOutputMediaFileUri() {
        File mediaFile = getOutputMediaFile();
        if (mediaFile != null) return Uri.fromFile(mediaFile);
        else return null;
    }

    /**
     * Create a File for saving an image
     */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaFile = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Artia");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("Artia", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        }


        return mediaFile;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        //releaseCamera();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkCameraHardware(getContext())) {

            if (!allPermissionGranted()) {

                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            } else {
                // Create an instance of Camera
                prepareInterface();
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                Toast.makeText(getContext(), "Permissions granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "You did not granted all the required permissions.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean allPermissionGranted() {

        PackageManager pm = getContext().getPackageManager();
        int hasStoragePerm = pm.checkPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                getContext().getPackageName());
        int hasCameraPerm = pm.checkPermission(
                Manifest.permission.CAMERA,
                getContext().getPackageName());
        if (hasStoragePerm == PackageManager.PERMISSION_GRANTED && hasCameraPerm == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return true;
    }
}