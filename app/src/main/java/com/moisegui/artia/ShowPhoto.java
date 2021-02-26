package com.moisegui.artia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.filters.ar.ARFilter;
import com.moisegui.artia.filters.ar.ImageDetectionFilter;
import com.moisegui.artia.services.MotifCallback;
import com.moisegui.artia.services.MotifService;
import com.squareup.picasso.Picasso;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowPhoto extends AppCompatActivity {

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    private ARFilter mFilter;
    Button btnCancel;
    Button btnSubmit;
    ImageView img;
    ImageView imgMotif;
    TextView motifTitle;
    TextView motifDescription;
    ProgressBar progressBar;
    MaterialCardView resultCard;
    File file;
    List<Motif> motifs;

    private static final String TAG = "OCVSample::Activity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        btnCancel = findViewById(R.id.cancel);
        btnSubmit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.loadingProgress);
        resultCard = findViewById(R.id.result_card);
        imgMotif = findViewById(R.id.image_motif);
        motifTitle = findViewById(R.id.title_motif);
        motifDescription = findViewById(R.id.motif_description);

        img = findViewById(R.id.img);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String path = bundle.getString("path");
            file = new File(path);

            Picasso.get()
                    .load(file)
//                    .fit().centerCrop()
                    .fit()
                    .rotate(90)
                    .into(img);
        }

        motifs = new ArrayList<>();

        MotifService.findAll(new MotifCallback() {
            @Override
            public void onCallback(List<Motif> motifList) {
                motifs = motifList;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) {
                    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                    bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Uri imageUri = Uri.fromFile(file);
                    Bitmap bmp = getBitmap(imageUri);

                    Mat image = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
                    Utils.bitmapToMat(bmp, image);

                    new ImageInitAsyncTask().execute(image);

                }
            }
        });
    }

    int taskStatus = 0;

    final class ImageInitAsyncTask extends AsyncTask<Mat, String, Mat> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);
            btnCancel.setEnabled(false);

            taskStatus = 1;
        }

        @Override
        protected void onProgressUpdate(String... message) {
            super.onProgressUpdate();

        }

        @Override
        protected Mat doInBackground(Mat... mats) {
            Mat image = mats[0];

            Map<String, Mat> result = mFilter.recherche(
                    image, image);

            Set<String> cles = result.keySet();

            Log.v("******* RESULT ********", cles.toString());

            if (cles.contains("SUCCESS")) {
                image = result.get("SUCCESS");
            } else image = null;

            return image;
        }

        @Override
        protected void onPostExecute(Mat result) {
            if (result == null) {
                Toast.makeText(ShowPhoto.this, R.string.no_match_found, Toast.LENGTH_LONG).show();
            } else {
                Uri imageUri = Uri.fromFile(file);
                Bitmap bmp = getBitmap(imageUri);

                Utils.matToBitmap(result, bmp);

                Uri uri = getImageUri(getApplicationContext(), bmp);

                Toast.makeText(ShowPhoto.this, R.string.a_motif_found, Toast.LENGTH_SHORT).show();

                Picasso.get()
                        .load(uri)
                        .fit()
                        .rotate(90)
                        .into(img);

                //TODO: Show the motif details under the picture at the place of the buttons
//                Picasso.get()
//                        .load(motif.getImage())
//                        .into(imgMotif);

                motifTitle.setText("Titre du motif");
                motifDescription.setText("Description du motif");

                resultCard.setVisibility(View.VISIBLE);


                //TODO Create an async Task to save it to the History of the current user
            }

            btnCancel.setEnabled(true);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            progressBar.setVisibility(View.GONE);
            taskStatus = 0;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap getBitmap(Uri uri) {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(),
                    uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    try {
                        mFilter = new ImageDetectionFilter(
                                getApplicationContext(),
                                motifs,
                                1.0);
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to load drawable: " +
                                "mFilter");
                        e.printStackTrace();
                        break;
                    }

                    btnSubmit.setEnabled(true);

                }
                break;
                default: {
                    btnSubmit.setEnabled(false);
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };



}