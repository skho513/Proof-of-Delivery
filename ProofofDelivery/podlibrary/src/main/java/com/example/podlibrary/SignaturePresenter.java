package com.example.podlibrary;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.lalamove.base.presenter.AbstractPresenter;
import com.lalamove.core.utils.DataUtils;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.podlibrary.SignatureContract.ISignaturePresenter;

/**
 * Created by bryanyeung on 2/2/2017.
 */
public class SignaturePresenter extends AbstractPresenter<SignatureContract.ISignatureView> implements ISignaturePresenter {
    private static final String TAG = "SignaturePresenter";
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    public static final String KEY_ORDER = "KEY_ORDER";
    private final File PIC_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private final File file = new File(PIC_DIR, "podSignature.png");

    private Order order;
    private Context context;
    private PodApi podApi;

    @Inject
    public SignaturePresenter(Context context, PodApi podApi) {
        this.context = context;
        this.podApi = podApi;
    }

    @Override
    public void with(Bundle bundle) {
        order = DataUtils.get(bundle.getSerializable(KEY_ORDER), Order.class);
        view.setRecipientName(order.getRecipientName());
    }

    @Override
    public void submitPOD() {
        if (DataUtil.saveBitmap(file, view.getDrawing()))
            uploadPOD();
    }


    public void uploadPOD() {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        podApi.uploadPOD(body).enqueue(new Callback<ResponseBody>() {
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

    @Override
    public void setRecipient(String name) {
        order.setRecipientName(name);
    }

    @Override
    public void requestConfirmRecipient() {
        view.confirmRecipient(order.getRecipientName());
    }
}
