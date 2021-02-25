package com.moisegui.artia.ui.search;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.moisegui.artia.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class SearchFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"Manifest.permission.CAMERA",
            "Manifest.permission.READ_EXTERNAL_STORAGE", "Manifest.permission.WRITE_EXTERNAL_STORAGE"};

    private static final String TAG = "OCVSample::Activity";
    private static final int REQUEST_PERMISSION = 100;
    private int w, h;
    private CameraBridgeViewBase mOpenCvCameraView;
    TextView tvName;
    Scalar RED = new Scalar(255, 0, 0);
    Scalar GREEN = new Scalar(0, 255, 0);
    FeatureDetector detector;
    DescriptorExtractor descriptor;
    DescriptorMatcher matcher;
    Mat sceneDescriptor, objDescriptor;
    Mat imgObject;
    MatOfKeyPoint objKeyPoints, sceneKeyPoints;

    View root;

    static {
        if (!OpenCVLoader.initDebug())
            Log.d("ERROR", "Unable to load OpenCV");
        else
            Log.d("SUCCESS", "OpenCV loaded");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_search, container, false);

        mOpenCvCameraView = (CameraBridgeViewBase) root.findViewById(R.id.tutorial1_activity_java_surface_view);
        tvName = (TextView) root.findViewById(R.id.cameraErrorTextView);


        if (checkCameraHardware(getContext())) {

            if (!allPermissionGranted()) {
                Toast.makeText(getContext(), R.string.should_grant_app_permissions, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(R.string.should_grant_app_permissions);
            } else {
                //tvName.setVisibility(View.GONE);
                // Create an instance of Camera
               // mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
                //mOpenCvCameraView.setCvCameraViewListener(this);
            }

        }

        return root;
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

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    try {
                        initializeOpenCVDependencies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private void initializeOpenCVDependencies() throws IOException {
        mOpenCvCameraView.enableView();
        detector = FeatureDetector.create(FeatureDetector.ORB);
        descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        imgObject = new Mat();
        AssetManager assetManager = getActivity().getAssets();
        InputStream istr = assetManager.open("a.jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        Utils.bitmapToMat(bitmap, imgObject);
        Imgproc.cvtColor(imgObject, imgObject, Imgproc.COLOR_RGB2GRAY);
        imgObject.convertTo(imgObject, 0); //converting the image to match with the type of the cameras image
        objDescriptor = new Mat();
        objKeyPoints = new MatOfKeyPoint();
        detector.detect(imgObject, objKeyPoints);
        descriptor.compute(imgObject, objKeyPoints, objDescriptor);

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getContext(), mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            //mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        if (checkCameraHardware(getContext())) {

            if (!allPermissionGranted()) {

                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }

        }


    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                Toast.makeText(getContext(), "Permissions granted.", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getContext(), "You did not granted all the required permissions.", Toast.LENGTH_SHORT).show();
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
        return false;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        w = width;
        h = height;
    }

    @Override
    public void onCameraViewStopped() {

    }

    public Mat recognize(Mat aInputFrame) {

        Imgproc.cvtColor(aInputFrame, aInputFrame, Imgproc.COLOR_RGB2GRAY);
        sceneDescriptor = new Mat();
        sceneKeyPoints = new MatOfKeyPoint();
        detector.detect(aInputFrame, sceneKeyPoints);
        descriptor.compute(aInputFrame, sceneKeyPoints, sceneDescriptor);

        // Matching
        MatOfDMatch matches = new MatOfDMatch();
        if (imgObject.type() == aInputFrame.type()) {
            matcher.match(objDescriptor, sceneDescriptor, matches);
        } else {
            return aInputFrame;
        }
        List<DMatch> matchesList = matches.toList();

        Double max_dist = 0.0;
        Double min_dist = 100.0;

        for (int i = 0; i < matchesList.size(); i++) {
            Double dist = (double) matchesList.get(i).distance;
            if (dist < min_dist)
                min_dist = dist;
            if (dist > max_dist)
                max_dist = dist;
        }

        LinkedList<DMatch> good_matches = new LinkedList<>();

        for (int i = 0; i < matchesList.size(); i++) {
            if (matchesList.get(i).distance <= (1.3 * min_dist))
                good_matches.addLast(matchesList.get(i));
        }

        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(good_matches);
        MatOfByte drawnMatches = new MatOfByte();
        if (aInputFrame.empty() || aInputFrame.cols() < 1 || aInputFrame.rows() < 1) {
            return aInputFrame;
        }
        Mat outputImg = new Mat();
        Features2d.drawMatches(imgObject, objKeyPoints, aInputFrame, sceneKeyPoints, goodMatches, outputImg, GREEN, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
        Imgproc.resize(outputImg, outputImg, aInputFrame.size());

//        //-- Localize the object
//        List<Point> obj  = new ArrayList<>();
//        List<Point> scene = new ArrayList<>();
//
//        List<KeyPoint> listOfKeypointsObject = keypoints1.toList();
//        List<KeyPoint> listOfKeypointsScene = sceneKeyPoints.toList();
//
//        if(good_matches.size() > 5){
//            for (int i = 0; i < good_matches.size(); i++) {
//                //-- Get the keypoints from the good matches
//                obj.add(listOfKeypointsObject.get(good_matches.get(i).queryIdx).pt);
//                scene.add(listOfKeypointsScene.get(good_matches.get(i).trainIdx).pt);
//            }
//
//            try {
//                MatOfPoint2f objMat = new MatOfPoint2f(), sceneMat = new MatOfPoint2f();
//                objMat.fromList(obj);
//                sceneMat.fromList(scene);
//                double ransacReprojThreshold = 2.0;
//                Mat H = Calib3d.findHomography( objMat, sceneMat, Calib3d.RANSAC, ransacReprojThreshold );
//                //-- Get the corners from the image_1 ( the object to be "detected" )
//                Mat objCorners = new Mat(4, 1, CvType.CV_32FC2), sceneCorners = new Mat();
//
//                float[] objCornersData = new float[(int) (objCorners.total() * objCorners.channels())];
//                objCorners.get(0, 0, objCornersData);
//                objCornersData[0] = 0;
//                objCornersData[1] = 0;
//                objCornersData[2] = imgObject.cols();
//                objCornersData[3] = 0;
//                objCornersData[4] = imgObject.cols();
//                objCornersData[5] = imgObject.rows();
//                objCornersData[6] = 0;
//                objCornersData[7] = imgObject.rows();
//                objCorners.put(0, 0, objCornersData);
//
//
//                    Core.perspectiveTransform(objCorners, sceneCorners, H);
//
//                float[] sceneCornersData = new float[(int) (sceneCorners.total() * sceneCorners.channels())];
//                sceneCorners.get(0, 0, sceneCornersData);
//                //-- Draw lines between the corners (the mapped object in the scene - image_2 )
//                Imgproc.line(aInputFrame, new Point(sceneCornersData[0] + imgObject.cols(), sceneCornersData[1]),
//                        new Point(sceneCornersData[2] + imgObject.cols(), sceneCornersData[3]), new Scalar(0, 255, 0), 4);
//                Imgproc.line(aInputFrame, new Point(sceneCornersData[2] + imgObject.cols(), sceneCornersData[3]),
//                        new Point(sceneCornersData[4] + imgObject.cols(), sceneCornersData[5]), new Scalar(0, 255, 0), 4);
//                Imgproc.line(aInputFrame, new Point(sceneCornersData[4] + imgObject.cols(), sceneCornersData[5]),
//                        new Point(sceneCornersData[6] + imgObject.cols(), sceneCornersData[7]), new Scalar(0, 255, 0), 4);
//                Imgproc.line(aInputFrame, new Point(sceneCornersData[6] + imgObject.cols(), sceneCornersData[7]),
//                        new Point(sceneCornersData[0] + imgObject.cols(), sceneCornersData[1]), new Scalar(0, 255, 0), 4);
//
//            }
//            catch (Exception e){
//                Log.v(TAG, e.getMessage());
//                return aInputFrame;
//            }
//        }

        return outputImg;
    }


    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return recognize(inputFrame.rgba());

    }

}