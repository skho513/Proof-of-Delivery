package com.example.podlibrary;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.lalamove.base.presenter.Initializable;

/**
 * Contract for signature view and presenter
 *
 * @author bryanyeung
 */
public class SignatureContract {
    interface ISignatureView {
        void setRecipientName(String recipientName);

        void confirmRecipient(String name);

        Bitmap getDrawing();
    }

    interface ISignaturePresenter extends Initializable<Bundle> {
        void submitPOD();

        void setRecipient(String name);

        void requestConfirmRecipient();
    }
}
