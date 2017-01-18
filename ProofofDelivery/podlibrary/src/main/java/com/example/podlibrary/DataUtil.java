package com.example.podlibrary;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class DataUtil {
    public static final String TAG = SignatureActivity.class.getSimpleName();
    public static void closeSilently(Closeable closeable){
        try {
            if (closeable != null)
            closeable.close();
        } catch (IOException e) {
            Log.w(TAG,"Error: Cannot close properly");
        }
    }
}
