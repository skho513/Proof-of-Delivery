package com.example.proofofdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.podlibrary.Order;
import com.example.podlibrary.SignatureActivity;
import com.example.podlibrary.SignaturePresenter;

public class getSignaturePage extends AppCompatActivity {

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
                .putExtra(SignaturePresenter.KEY_ORDER, new Order("Bob")));
    }
}
