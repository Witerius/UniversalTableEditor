package com.wizardmb.witerius.universaltableeditor.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.UtilsDate;
import com.wizardmb.witerius.universaltableeditor.fragment.CellSFragment;
import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelDate;
import com.wizardmb.witerius.universaltableeditor.model.ModelTempData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dateProverka;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.mSettings;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenTable;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.saveActivated;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.saveDayLimit;

/**
 * Created by Wizard on 10.03.2017.
 */

public final class AddingKeyDialogFragment extends DialogFragment {
    private int tempId = 0;
    private MainActivity activityM;
    private String[] keyString = {"0", "ML5FGAF65GS5HDD4FS", "LIK4ZAF6574CSDD4F8", "ZX4S0AF65745LO0ZDV", "KL54ZAF65CZGE534FS", "AQ12WSF6574KGHTFS",
            "CXFSRG65745FGEYRFS", "KSFAAZAF65745HDFGFH", "RY5ZAFFGJ45HDD4TU", "LUYJCAF6HDGBDSGE5", "HDJT5FZAF65745HNZDSGSH", "ZXCVBN123HTDSF6T4GFAZAX"};


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activityM = (MainActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_enter_key);

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_cell, null);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

        final LinearLayout addCell = (LinearLayout) container.findViewById(R.id.dialog_add_cell);

        LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_text, null);
        textLL.setId(tempId);

        EditText editText = (EditText) textLL.findViewById(R.id.et_text);
        editText.setInputType(1);
        editText.setCursorVisible(true);
        editText.setFocusableInTouchMode(true);
        addCell.addView(textLL);

        builder.setView(container);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LinearLayout textLL = (LinearLayout) addCell.getChildAt(tempId);

                EditText editText = (EditText) textLL.findViewById(R.id.et_text);
                boolean isFound = false;
                for (int i = saveActivated; i < 12; i++) {
                    if (editText.getText().toString().equals(keyString[i])) {
                        saveActivated = i+1;
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putInt(MainActivity.APP_PREFERENCES_SAVE_ACTIVATED, saveActivated);
                        editor.apply();

                        int date = calendar.get(Calendar.DAY_OF_MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        int mont = calendar.get(Calendar.MONTH);

                        if (i != 11) {
                            if(mont ==11)
                            {
                                mont=0;
                                year++;
                            }
                            else
                            {
                                mont++;
                            }
                            if(mont==1 && date>28)
                            {
                                date = 28;
                            }
                            calendar.set(year, mont, date);

                        } else {
                            year = 2099;
                            calendar.set(year, mont, date);
                        }
                        saveDayLimit = calendar.getTimeInMillis();

                        SharedPreferences.Editor editor1 = mSettings.edit();
                        editor1.putLong(MainActivity.APP_PREFERENCES_SAVE_DATE_LIMIT, saveDayLimit);
                        editor1.apply();
                        isFound = true;
                    }
                }
                if(isFound)
                {
                    Toast.makeText(activityM, R.string.success_key, Toast.LENGTH_LONG).show();

                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(activityM, R.string.invalid_key, Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

}
