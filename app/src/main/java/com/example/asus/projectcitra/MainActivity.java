package com.example.asus.projectcitra;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    Uri imageUri;
    int value = 120;
    Button btnGrayscale,btnThresholding,btnGaussianBlur,btnCannyEdgeDetection;
    Bitmap grayBitmap,thresholdBitmap,imageBitmap,gaussianBlurBitmap,cannyBitmap,restore;
    SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenCVLoader.initDebug();

        if (OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"OpenCV Succes" ,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"OpenCV Error" ,Toast.LENGTH_SHORT).show();
        }

        imageView = findViewById(R.id.imageEdited);

        btnGrayscale = findViewById(R.id.btn_grayscale);
        btnThresholding = findViewById(R.id.btn_thresholding);
        btnGaussianBlur = findViewById(R.id.btn_gaussianblur);
        btnCannyEdgeDetection = findViewById(R.id.btn_canny_edgedetection);

        seekBar = findViewById(R.id.seek_bar);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setProgress(121);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progressChangedValue = progress;
                value = progress;
                seekBar.setProgress(value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        btnGrayscale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setVisibility(View.INVISIBLE);
                GrayScale grayScale = new GrayScale();
                Bitmap result = grayScale.convertToGray(imageBitmap);
                imageView.setImageBitmap(result);
            }
        });

        btnThresholding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setVisibility(View.VISIBLE);
                Thresholding thresholding = new Thresholding();
                Bitmap result = thresholding.convertToThresholding(value,imageBitmap);
                imageView.setImageBitmap(result);
            }
        });

        btnGaussianBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setVisibility(View.VISIBLE);
                GaussianBlur gaussianBlur = new GaussianBlur();
                Bitmap result = gaussianBlur.convertToGaussianBlur(value,imageBitmap);
                imageView.setImageBitmap(result);
            }
        });

        btnCannyEdgeDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setVisibility(View.VISIBLE);
                Canny canny = new Canny();
                Bitmap result = canny.convertToCanny(value,imageBitmap);
                imageView.setImageBitmap(result);
            }
        });
    }

    //running after user choose image in gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            try{
                //imageBitmap merupakan image yg diimport dari gallery
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                restore = imageBitmap;
            }catch (Exception e){

            }

            imageView.setImageBitmap(imageBitmap);


            try{
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.upload_image:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);
                break;

            case R.id.backup :
                imageView.setImageBitmap(restore);
                break;

            case R.id.histogram :
                showHistogram();
                break;
        }

        return true;
    }

    public void showHistogram(){
//        Mat rgba = new Mat();
//        Mat histoMat = new Mat();
//
//        Utils.bitmapToMat(imageBitmap,rgba);
//        Size rgbaSize = rgba.size();
//
//        //set the amount of barr in histogram
//        int histSize = 256;
//        MatOfInt histogramSize = new MatOfInt(histSize);
//
//        // Set the height of the histogram and width of the bar.
//        int histogramHeight = (int) rgbaSize.height;
//        int binWidth = 5;
//
//        //set the value range
//        MatOfFloat histogramRange = new MatOfFloat(0f,256f);
//
//        // Create two separate lists: one for colors and one for channels (these will be used as separate datasets).
//        Scalar[] colorsRgb = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};
//        MatOfInt[] channels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};
//
//        // Create an array to be saved in the histogram and a second array, on which the histogram chart will be drawn.
//        Mat[] histograms = new Mat[]{new Mat(), new Mat(), new Mat()};
//
//        for (int i = 0; i < channels.length; i++) {
//            Imgproc.calcHist(Collections.singletonList(rgba), channels[i], new Mat(), histograms[i], histogramSize, histogramRange);
//            Core.normalize(histograms[i], histograms[i], histogramHeight, 0, Core.NORM_INF);
//            for (int j = 0; j < histSize; j++) {
//                Point p1 = new Point(binWidth * (j - 1), histogramHeight - Math.round(histograms[i].get(j - 1, 0)[0]));
//                Point p2 = new Point(binWidth * j, histogramHeight - Math.round(histograms[i].get(j, 0)[0]));
//                Imgproc.line(histoMat, p1, p2, colorsRgb[i], 2, 8, 0);
//            }
//        }
//
//
//        Utils.matToBitmap(histoMat, histBitmap);
//        imageView.setImageBitmap(histBitmap);

    }

}
