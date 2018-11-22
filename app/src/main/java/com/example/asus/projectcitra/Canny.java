package com.example.asus.projectcitra;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Canny {

    Bitmap cannyBitmap;

    public Bitmap convertToCanny(int value,Bitmap imageBitmap){
        try{
            Mat rgba = new Mat();
            Mat grayMat = new Mat();
            Mat cannyMat = new Mat();

            int width = imageBitmap .getWidth();
            int height = imageBitmap.getHeight();
            cannyBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565); //create an gray bitmap image

            Utils.bitmapToMat(imageBitmap,rgba); // convert bitmap into mat

            Imgproc.cvtColor(rgba,grayMat,Imgproc.COLOR_BGR2GRAY); //use Rgba to change grayMat into grayscale image
            Imgproc.Canny(grayMat,cannyMat,value,value-20);
            Utils.matToBitmap(cannyMat,cannyBitmap); // after that, convert a mat into bitmap

            return cannyBitmap;
        }catch (Exception ex){
//            Toast.makeText(getApplicationContext(),"GAMBAR BELUM ADA",Toast.LENGTH_SHORT).show();
            Log.d("Gaussian", ex.getMessage());
        }
        return imageBitmap;
    }

}
