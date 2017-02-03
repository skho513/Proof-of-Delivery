package com.example.podlibrary;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignatureActivity extends AppCompatActivity implements SignatureContract.ISignatureView,
        EnterDetailsDialog.OnEditConfirmListener, DrawingView.DrawStateListener {
    private static final String TAG = SignatureActivity.class.getSimpleName();

    private DrawingView drawingView;
    private View ivEmptySignature;
    private Button clearBtn;
    private Button saveBtn;
    private TextView tvRecipientDetails;
    private SignatureContract.ISignaturePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.podlibrary.R.layout.signaturepage);

        drawingView = (DrawingView) findViewById(R.id.drawingView);
        drawingView.setDrawingCacheEnabled(true);
        ivEmptySignature = findViewById(R.id.ivEmptySignature);
        drawingView.setDrawStateListener(this);

        saveBtn = (Button) findViewById(R.id.btnConfirm);
        clearBtn = (Button) findViewById(R.id.btnClear);
        tvRecipientDetails = (TextView) findViewById(R.id.tvRecipientDetails);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Collect Signature");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new SignaturePresenter(this);
        presenter.attach(this);
        presenter.with(getIntent().getExtras());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }

    public void onSave(View view) {
        if (drawingView.isEmpty()) {
            showEmptySigDialog();
        } else {
            presenter.submitPOD();
            Log.d(TAG, saveBtn.getText() + " is successfully pressed and saved.");
        }
    }

    public void onClear(View view) {
        drawingView.clearView();
        Log.d(TAG, clearBtn.getText() + " is successfully pressed and cleared.");
    }

    @Override
    public void onSubmit(String recipientName) {
        tvRecipientDetails.setText(recipientName);
        presenter.setRecipient(recipientName);
    }

    @Override
    public void onDrawStarted() {
        ivEmptySignature.setVisibility(View.GONE);
    }

    public void onConfirmRecipient(View view) {
        presenter.requestConfirmRecipient();
    }

    public void showEmptySigDialog() {
        new EmptySigDialog().show(getSupportFragmentManager(), "No Name Error");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.handlePermissionResults(requestCode, permissions, grantResults);
    }

    @Override
    public void setRecipientName(String recipientName) {
        tvRecipientDetails.setText(recipientName);
    }

    @Override
    public void confirmRecipient(Bundle bundle) {
        DialogFragment enterDetails = new EnterDetailsDialog();
        enterDetails.setArguments(bundle);
        enterDetails.show(getSupportFragmentManager(), "Enter Details");
    }

    @Override
    public void handlePermissionDenial() {
        Toast.makeText(this, "READ_EXTERNAL_STORAGE Denied", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public Bitmap getDrawing() {
        return DataUtil.getBitmap(drawingView);
    }

    @Override
    public void requestStoragePermission(String[] permissions, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showStoragePermissionRationale();
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    public void showStoragePermissionRationale() {
        new AlertDialog.Builder(this, R.style.PodTheme_Dialog).setTitle(R.string.alert_title)
                .setMessage(R.string.alert_info)
                .setPositiveButton(R.string.alert_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.requestStoragePermission();
                    }
                })
                .setNegativeButton(R.string.alert_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}