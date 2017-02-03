package com.example.podlibrary;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.view.View;

import com.lalamove.core.utils.DataUtils;

import java.io.File;
import java.io.FileOutputStream;

public class DataUtil {
    public static final String TAG = DataUtil.class.getSimpleName();

    public static Bitmap getBitmap(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();

        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        view.draw(canvas);
        return returnedBitmap;
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static boolean saveBitmap(File file, Bitmap bitmap) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            Bitmap signatureSize = Bitmap.createScaledBitmap(bitmap, 600, 800, false);
            signatureSize.compress(Bitmap.CompressFormat.PNG, 100, fos);

            if ((!file.exists() || file.delete()) && file.createNewFile()) {
                return true;
            } else
                throw new Exception("Failed to create a file");
        } catch (Exception e) {
            Log.e(TAG, "Error while writing a file", e);
        } finally {
            DataUtils.closeSilently(fos);
        }

        return false;
    }
}
