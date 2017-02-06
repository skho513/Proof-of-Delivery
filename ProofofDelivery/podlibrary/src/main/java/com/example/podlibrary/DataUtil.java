package com.example.podlibrary;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresPermission;
import android.view.View;

import com.lalamove.core.utils.DataUtils;

import java.io.File;
import java.io.FileOutputStream;

import timber.log.Timber;

public class DataUtil {

    public static Bitmap getBitmap(View view) {
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Drawable background = view.getBackground();

        if (background == null) {
            canvas.drawColor(Color.WHITE);
        } else {
            background.draw(canvas);
        }

        view.draw(canvas);
        return bitmap;
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static boolean saveBitmap(File file, Bitmap bitmap) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);

            final Bitmap signature = Bitmap.createScaledBitmap(bitmap, 600, 800, false);
            signature.compress(Bitmap.CompressFormat.PNG, 100, fos);

            if ((!file.exists() || file.delete()) && file.createNewFile()) {
                return true;
            } else
                throw new Exception("Failed to create a file");
        } catch (Exception e) {
            Timber.e("Error while writing a file", e);
        } finally {
            DataUtils.closeSilently(fos);
        }

        return false;
    }
}
