package com.wizardmb.witerius.universaltableeditor.dialog;

/**
 * Created by User on 15.06.2016.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.wizardmb.witerius.universaltableeditor.R;

public final class CDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.warning)
                    .setMessage(R.string.cant_delete1)
                    .setIcon(R.drawable.ic_cancel)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

        return builder.create();
    }


}