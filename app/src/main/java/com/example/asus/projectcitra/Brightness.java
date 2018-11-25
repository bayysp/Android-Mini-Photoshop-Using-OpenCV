package com.example.asus.projectcitra;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class Brightness {

    Bitmap imageBitmap,brightBitmap;
    public Bitmap convertToBrightness(Bitmap imageBitmap,int value){
        this.imageBitmap = imageBitmap;

        int width = imageBitmap .getWidth();
        int height = imageBitmap.getHeight();
        brightBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888); //create an gray bitmap image

        Mat brightMat = new Mat();
        Utils.bitmapToMat(imageBitmap,brightMat);
        brightMat.convertTo(brightMat,-1,1,value);

        Utils.matToBitmap(brightMat,brightBitmap);

        return brightBitmap;

    }
}
