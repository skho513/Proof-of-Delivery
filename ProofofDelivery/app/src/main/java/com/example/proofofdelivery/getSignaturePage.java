package com.example.proofofdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.podlibrary.MyButton;
import com.example.podlibrary.Order;
import com.example.podlibrary.Serialisation;
import com.example.podlibrary.SignatureActivity;

public class getSignaturePage extends AppCompatActivity implements MyButton.ButtonListener {

    private Button btnSignaturePage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_signature_page);

        btnSignaturePage = (Button) findViewById(R.id.btnSignaturePage);
        btnSignaturePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SerializeMethod();
            }
        });
    }

    public void SerializeMethod() {
        startActivity(new Intent(this, SignatureActivity.class)
                .putExtra(SignatureActivity.KEY_ORDER, new Order("Bob")));
    }

    @Override
    public void onSave(View view) {
    }

    @Override
    public void onCancel(View view) {
        Log.d("hello", BuildConfig.ENDPOINT);
    }

    @Override
    public void onClear(View view) {

    }

}
