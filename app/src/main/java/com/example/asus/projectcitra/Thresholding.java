package com.example.asus.projectcitra;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Thresholding {

    Bitmap thresholdBitmap;

    protected Bitmap convertToThresholding(int thresh,Bitmap imageBitmap) {
        try{
            Mat rgba = new Mat();
            Mat grayMat = new Mat();
            Mat thresholdMat = new Mat();

//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inDither = false;
//            o.inSampleSize =4;

            int width = imageBitmap.getWidth();
            int height = imageBitmap.getHeight();
            thresholdBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);

            Utils.bitmapToMat(imageBitmap,rgba);
            Imgproc.cvtColor(rgba,grayMat,Imgproc.COLOR_BGR2GRAY);
            Imgproc.threshold(grayMat,thresholdMat,thresh,255,Imgproc.THRESH_BINARY);

            Utils.matToBitmap(thresholdMat,thresholdBitmap);

            return thresholdBitmap;
        }catch (Exception ex){
//            Toast.makeText(getApplicationContext(),"GAMBAR BELUM ADA" ,Toast.LENGTH_SHORT).show();
            Log.d("Grayscale",ex.getMessage());
        }
        return imageBitmap;
    }
}
