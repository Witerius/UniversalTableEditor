package com.wizardmb.witerius.universaltableeditor.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;


public final class AddingSampleDialogFragment extends DialogFragment {

    private AddingSampleListener addingSampleListener;

    public interface AddingSampleListener {
        void onSampleAdded(ModelSample newTask);

        void onSampleAddingCancel();

    }

    @Override
    public void onAttach(Activity activity ) {
        super.onAttach(activity);
        try {
            addingSampleListener = (AddingSampleListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddingColumnListener");
        }

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_sample_add);

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_sample, null);

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.tilDialogSampleName);
        final EditText etName  = tilName.getEditText();

        tilName.setHint(getResources().getString(R.string.name_sample));

        builder.setView(container);

        final ModelSample modelSample = new ModelSample();

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              try {
                    modelSample.setNameTable(etName.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nowOpenSample = modelSample.getTimeStamp();

                addingSampleListener.onSampleAdded(modelSample);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                addingSampleListener.onSampleAddingCancel();

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (etName.length() == 0) {
                    positiveButton.setEnabled(false);
                    tilName.setError(getResources().getString(R.string.dialog_error_empty_name_sample));
                }

                etName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilName.setError(getResources().getString(R.string.dialog_error_empty_name_sample));
                        } else {
                            positiveButton.setEnabled(true);
                            tilName.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });
        return alertDialog;
    }


}