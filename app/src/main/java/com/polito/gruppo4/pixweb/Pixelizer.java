package com.polito.gruppo4.pixweb;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by David on 30/08/2017.
 */

public class Pixelizer {
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public Bitmap getPixelizedBitmap(Bitmap inputBm){
        int originalWidth = inputBm.getWidth();
        int originalHeight= inputBm.getHeight();
        int smallWidth =32 , smallHeight = 32;
        Bitmap small = getResizedBitmap(inputBm, smallWidth, smallHeight);
        Bitmap pixelated = getResizedBitmap(small, originalWidth, originalHeight);
        return pixelated;
    }

}
