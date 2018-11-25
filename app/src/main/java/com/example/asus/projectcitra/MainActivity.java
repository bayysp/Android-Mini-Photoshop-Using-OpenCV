package com.example.asus.projectcitra;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.drm.DrmStore;
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
    Button btnGrayscale,btnThresholding,btnGaussianBlur,btnCannyEdgeDetection,btnBrightness,btnContrast;
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
        btnBrightness = findViewById(R.id.btn_brightness);
        btnContrast = findViewById(R.id.btn_contrast);

        btnGrayscale.setEnabled(false);
        btnThresholding.setEnabled(false);
        btnContrast.setEnabled(false);
        btnBrightness.setEnabled(false);
        btnCannyEdgeDetection.setEnabled(false);
        btnGaussianBlur.setEnabled(false);

        seekBar = findViewById(R.id.seek_bar);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setProgress(121);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
                seekBar.setMax(120);
                Thresholding thresholding = new Thresholding();
                Bitmap result = thresholding.convertToThresholding(value,imageBitmap);
                imageView.setImageBitmap(result);
            }
        });

        btnGaussianBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setMax(50);
                GaussianBlur gaussianBlur = new GaussianBlur();
                Bitmap result = gaussianBlur.convertToGaussianBlur(value,imageBitmap);
                imageView.setImageBitmap(result);
            }
        });

        btnCannyEdgeDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setMax(120);
                Canny canny = new Canny();
                Bitmap result = canny.convertToCanny(value,imageBitmap);
                imageView.setImageBitmap(result);
            }
        });

        btnBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setMax(120);
                Brightness brightness = new Brightness();
                Bitmap result = brightness.convertToBrightness(imageBitmap,value);
                imageView.setImageBitmap(result);
            }
        });

        btnContrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = 20;
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setMax(max);
                Contrast contrast = new Contrast();

                double newValue = new Double(value);
                double setVal = newValue/10;
                Bitmap result = contrast.convertToContrast(imageBitmap,setVal);
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

                btnGrayscale.setEnabled(true);
                btnThresholding.setEnabled(true);
                btnContrast.setEnabled(true);
                btnBrightness.setEnabled(true);
                btnCannyEdgeDetection.setEnabled(true);
                btnGaussianBlur.setEnabled(true);
            }catch (Exception e){

            }

            imageView.setImageBitmap(imageBitmap);

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
                break;
        }

        return true;
    }

}
