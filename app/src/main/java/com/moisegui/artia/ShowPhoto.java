package com.moisegui.artia;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class ShowPhoto extends AppCompatActivity {

    Button cancel;
    Button submit;
    ImageView img;
    File file;
    private static final String TAG = "OCVSample::Activity";
    private static final int REQUEST_PERMISSION = 100;

    Scalar RED = new Scalar(255, 0, 0);
    Scalar GREEN = new Scalar(0, 255, 0);
    FeatureDetector detector;
    DescriptorExtractor descriptor;
    DescriptorMatcher matcher;
    Mat sceneDescriptor, objDescriptor;
    Mat imgObject;
    MatOfKeyPoint objKeyPoints, sceneKeyPoints;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        cancel = findViewById(R.id.cancel);
        submit = findViewById(R.id.submit);
        img = findViewById(R.id.img);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String path = bundle.getString("path");
            file = new File(path);

            Picasso.get()
                    .load(file)
                    .fit().centerCrop()
//                    .rotate(90)
                    .into(img);
        }

        try {
            initializeOpenCVDependencies();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) {
                    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                    bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Uri imageUri = Uri.fromFile(file);
                    Bitmap bmp = getBitmap(imageUri);

                    Mat image = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
                    Utils.bitmapToMat(bmp, image);

                    image = recognize(image);


                    Utils.matToBitmap(image, bmp);
                    img.setImageBitmap(bmp);

                }
            }
        });
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


    private void initializeOpenCVDependencies() throws IOException {
        detector = FeatureDetector.create(FeatureDetector.ORB);
        descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        imgObject = new Mat();
        AssetManager assetManager = getAssets();
        InputStream istr = assetManager.open("52486.jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        Utils.bitmapToMat(bitmap, imgObject);
        Imgproc.cvtColor(imgObject, imgObject, Imgproc.COLOR_RGB2GRAY);
        imgObject.convertTo(imgObject, 0); //converting the image to match with the type of the cameras image
        objDescriptor = new Mat();
        objKeyPoints = new MatOfKeyPoint();
        detector.detect(imgObject, objKeyPoints);
        descriptor.compute(imgObject, objKeyPoints, objDescriptor);

    }


    public Mat recognize(Mat aInputFrame) {
        Mat impageCopy = aInputFrame.clone();

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
            if (matchesList.get(i).distance <= (1.2 * min_dist))
                good_matches.addLast(matchesList.get(i));
        }

        if (good_matches.size() < 5) {
            Toast.makeText(this, R.string.no_match_found, Toast.LENGTH_SHORT).show();
            return impageCopy;
        }

        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(good_matches);
        MatOfByte drawnMatches = new MatOfByte();
        if (aInputFrame.empty() || aInputFrame.cols() < 1 || aInputFrame.rows() < 1) {
            return impageCopy;
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


}