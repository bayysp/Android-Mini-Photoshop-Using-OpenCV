package com.example.asus.projectcitra;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class Contrast {

    Bitmap imageBitmap,conBitmap;

    public Bitmap convertToContrast(Bitmap imageBitmap,double value){
        this.imageBitmap = imageBitmap;
        int widht = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        conBitmap = Bitmap.createBitmap(widht,height,Bitmap.Config.ARGB_8888);

        Mat conMat = new Mat();
        Utils.bitmapToMat(imageBitmap,conMat);
        conMat.convertTo(conMat,-1,value,-50);
        Utils.matToBitmap(conMat,conBitmap);
        return conBitmap;
    }

}
