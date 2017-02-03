package com.example.podlibrary;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by bryanyeung on 2/2/2017.
 */

public class SignatureContract {
    interface ISignatureView {
        void requestStoragePermission(String[] permissions, int requestCode);

        void setRecipientName(String recipientName);

        void confirmRecipient(String name);

        void handlePermissionDenial();

        Bitmap getDrawing();
    }

    interface ISignaturePresenter {
        void with(Bundle bundle);

        void attach(ISignatureView view);

        void detach();

        void submitPOD();

        void handlePermissionResults(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        void setRecipient(String name);

        void requestStoragePermission();

        void requestConfirmRecipient();
    }
}
