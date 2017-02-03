package com.example.podlibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.podlibrary.R;

import android.widget.Button;
import android.widget.EditText;

public class EnterDetailsDialog extends DialogFragment {
    public static final String KEY_RECIPIENT_NAME = "KEY_RECIPIENT_NAME";
    EditText details;
    Button btnSubmit;

    public interface OnEditConfirmListener {
        void onSubmit(String recipientName);
    }

    OnEditConfirmListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_edit_details, null);
        btnSubmit = (Button) root.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (details.getText().toString().isEmpty()) {
                        showErrorDialog();
                    } else {
                        listener.onSubmit(details.getText().toString());
                        dismiss();
                    }
                }
            }
        });
        details = (EditText) root.findViewById(R.id.dialog_details_field);
        details.setText(getArguments().getString(KEY_RECIPIENT_NAME));
        details.setSelection(details.getText().length());
        return new AlertDialog.Builder(getActivity(), R.style.PodTheme_Dialog).setView(root).create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnEditConfirmListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void showErrorDialog() {
        DialogFragment error = new EmptyNameDialog();
        error.show(getFragmentManager(), "No Name Error");
    }
}
