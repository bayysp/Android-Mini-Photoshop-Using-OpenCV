package com.example.asus.projectcitra;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class GaussianBlur {

    Bitmap gaussianBlurBitmap;
    public Bitmap convertToGaussianBlur(int blur, Bitmap imageBitmap){
        try{
            if (blur%2 == 0){
                blur = blur + 1;
            }
            Mat rgba = new Mat();
            Mat gaussianBlurMat = new Mat();

            int width = imageBitmap.getWidth();
            int height = imageBitmap.getHeight();
            gaussianBlurBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);

            Utils.bitmapToMat(imageBitmap,rgba);

            Imgproc.GaussianBlur(rgba,gaussianBlurMat,new Size(blur,blur),0);

            Utils.matToBitmap(gaussianBlurMat,gaussianBlurBitmap);
            Log.d("Gaussian", blur+"");
            return gaussianBlurBitmap;

        }catch (Exception ex){
//            Toast.makeText(getApplicationContext(),"GAMBAR BELUM ADA",Toast.LENGTH_SHORT).show();
            Log.d("Gaussian", ex.getMessage());
        }
        return imageBitmap;
    }

}
