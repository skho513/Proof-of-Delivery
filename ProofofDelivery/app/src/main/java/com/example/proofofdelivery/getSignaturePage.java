package com.example.proofofdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.podlibrary.MyButton;
import com.example.podlibrary.Serialisation;
import com.example.podlibrary.SignaturePage;

public class getSignaturePage extends AppCompatActivity implements MyButton.ButtonListener {

    private Button btnSignaturePage;
    public static final String SER_KEY = "com.example.proofofdelivery.ser";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_signature_page);

        btnSignaturePage = (Button) findViewById(R.id.btnSignaturePage);
        btnSignaturePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              SerializeMethod();
            }
        });

        // Where is the configuration injected?
        // How do I clear the screen via a button click (and optionally allow me to override?)
        // How do I listen to cancel button click and hide the activity?
        // How do I know the onSave is being pressed and show the loading icon
        // How do i know the save signature is successful so that I can hide the loading icon and (maybe close the activity)
        // How do i know the save signature failed so i can hide the loading icon and show error message?
    }

    public void SerializeMethod(){

        Serialisation orderObject = new Serialisation();
        orderObject.Serialisation("A23H23UK42H42O25N24I24IH");
        Intent toSignaturePage = new Intent(this,SignaturePage.class); // For future changes, change SignaturePage.class to CustomSignaturePage.class
        Bundle passingKey = new Bundle();
        passingKey.putSerializable(SER_KEY, orderObject); // Pass orderObject
        toSignaturePage.putExtras(passingKey);

        startActivity(toSignaturePage);
    }

    @Override
    public void onSave(View view) {
    }

    @Override
    public void onCancel(View view) {
        Log.d("hello",BuildConfig.ENDPOINT);
    }

    @Override
    public void onClear(View view) {

    }
}