package com.example.podlibrary;

import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Created by bryanyeung on 2/2/2017.
 */

public class SignatureContract {
    interface ISignatureView {
        void setRecipientName(String recipientName);

        void confirmRecipient(String name);

        Bitmap getDrawing();
    }

    interface ISignaturePresenter {
        void with(Bundle bundle);

        void attach(ISignatureView view);

        void detach();

        void submitPOD();

        void setRecipient(String name);

        void requestConfirmRecipient();
    }
}
