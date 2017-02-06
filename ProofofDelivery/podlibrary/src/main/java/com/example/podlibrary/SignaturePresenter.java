package com.example.podlibrary;

import android.os.Bundle;
import android.os.Environment;

import com.lalamove.base.api.ApiCallback;
import com.lalamove.base.presenter.AbstractPresenter;
import com.lalamove.core.utils.DataUtils;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.podlibrary.SignatureContract.ISignaturePresenter;

/**
 * Presenter for binding signature view with the data
 *
 * @author bryanyeung
 */
public class SignaturePresenter extends AbstractPresenter<SignatureContract.ISignatureView> implements ISignaturePresenter {
    public static final String KEY_ORDER = "KEY_ORDER";
    private static final MediaType IMAGE = MediaType.parse("image/*");
    private final File PIC_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private final File file = new File(PIC_DIR, "podSignature.png");

    private Order order;
    private PodApi podApi;

    @Inject
    public SignaturePresenter(PodApi podApi) {
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
        final RequestBody reqFile = RequestBody.create(IMAGE, file);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        podApi.uploadPOD(body).enqueue(new ApiCallback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                Timber.d("Signature Page has been successfully uploaded to server");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Timber.e("Error uploading POD", throwable);
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
