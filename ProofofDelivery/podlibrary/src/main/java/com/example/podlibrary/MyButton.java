package com.example.podlibrary;

import android.view.View;
/**
 * Created by Buzz on 03/01/2017.
 */

public class MyButton  {
    public interface ButtonListener {
        void onSave(View view);
        void onCancel(View view);
        void onClear(View view);
    }
}