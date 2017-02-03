package com.example.podlibrary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by bryanyeung on 2/2/2017.
 */
public class SignaturePresenter implements SignatureContract.ISignaturePresenter {
    private static final String TAG = "SignaturePresenter";
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    public static final String KEY_ORDER = "KEY_ORDER";
    private final File PIC_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private final File file = new File(PIC_DIR, "podSignature.png");

    private SignatureContract.ISignatureView view;
    private Order order;
    private Context context;

    public SignaturePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void with(Bundle bundle) {
        order = (Order) bundle.getSerializable(KEY_ORDER);
        view.setRecipientName(order.getRecipientName());
    }

    @Override
    public void attach(SignatureContract.ISignatureView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void submitPOD() {
        uploadPODWithPermissionCheck();
    }

    private void uploadPODWithPermissionCheck() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            view.requestStoragePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            if (DataUtil.saveBitmap(file, view.getDrawing()))
                uploadPOD();
        }
    }

    public void uploadPOD() {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        getRetrofit().create(PODApi.class).uploadPOD(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Signature Page has been successfully uploaded to server");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Error uploading POD", t);
            }

        });
    }

    public Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder().baseUrl("http://10.10.8.143:8085").client(client).build();
    }

    @Override
    public void handlePermissionResults(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    uploadPODWithPermissionCheck();
                } else {
                    // Permission Denied
                    view.handlePermissionDenial();
                }
                break;
        }
    }

    @Override
    public void setRecipient(String name) {
        order.setRecipientName(name);
    }

    @Override
    public void requestStoragePermission() {
        view.requestStoragePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void requestConfirmRecipient() {
        final Bundle bundle = new Bundle();
        bundle.putString(EnterDetailsDialog.KEY_RECIPIENT_NAME, order.getRecipientName());

        view.confirmRecipient(bundle);
    }
}
