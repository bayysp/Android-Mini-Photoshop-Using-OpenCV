package com.example.asus.projectcitra;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GrayScale {

    Bitmap imageBitmap,grayBitmap;

    //method which using to create a gray image
    public Bitmap convertToGray(Bitmap imageBitmap){
        try{
            this.imageBitmap = imageBitmap;
            Mat rgba = new Mat();
            Mat grayMat = new Mat();

            int width = imageBitmap .getWidth();
            int height = imageBitmap.getHeight();
            grayBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565); //create an gray bitmap image

            Utils.bitmapToMat(imageBitmap,rgba); // convert bitmap into mat

            Imgproc.cvtColor(rgba,grayMat,Imgproc.COLOR_BGR2GRAY); //use Rgba to change grayMat into grayscale image
            Utils.matToBitmap(grayMat,grayBitmap); // after that, convert a mat into bitmap

//            mainActivity.imageView.setImageBitmap(grayBitmap);
            return grayBitmap;
        }catch(Exception ex){
//            Toast.makeText(mainActivity.getApplicationContext(),"GAMBAR BELUM ADA" ,Toast.LENGTH_SHORT).show();
            Log.d("Grayscale",ex.getMessage());
        }
        return imageBitmap;
    }
}
