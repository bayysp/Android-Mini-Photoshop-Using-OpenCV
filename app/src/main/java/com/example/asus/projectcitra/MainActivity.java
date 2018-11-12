package com.example.asus.projectcitra;

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
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    Uri imageUri;
    Button btnGrayscale,btnThresholding;
    Bitmap grayBitmap,thresholdBitmap,imageBitmap,restore;
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
        seekBar = findViewById(R.id.seek_bar);


        btnGrayscale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToGray();
            }
        });

        btnThresholding = findViewById(R.id.btn_thresholding);
        btnThresholding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToThresholding();
            }
        });
    }

    //method which using to create a gray image
    public void convertToGray(){
        Mat Rgba = new Mat();
        Mat grayMat = new Mat();

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize = 4;

        int width = imageBitmap .getWidth();
        int height = imageBitmap.getHeight();
        grayBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565); //create an gray bitmap image

        Utils.bitmapToMat(imageBitmap,Rgba); // convert bitmap into mat

        Imgproc.cvtColor(Rgba,grayMat,Imgproc.COLOR_BGR2GRAY); //use Rgba to change grayMat into grayscale image
        Utils.matToBitmap(grayMat,grayBitmap); // after that, convert a mat into bitmap

        imageView.setImageBitmap(grayBitmap);
    }

    private void convertToThresholding() {
        Mat Rgba = new Mat();
        Mat grayMat = new Mat();
        Mat thresholdMat = new Mat();

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize =4;

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        thresholdBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);

        Utils.bitmapToMat(imageBitmap,Rgba);
        Imgproc.cvtColor(Rgba,grayMat,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(grayMat,thresholdMat,120,255,Imgproc.THRESH_BINARY);

        Utils.matToBitmap(thresholdMat,thresholdBitmap);

        imageView.setImageBitmap(thresholdBitmap);
    }


    //running after user choose image in gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            try{
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
        }

        return true;
    }

}
