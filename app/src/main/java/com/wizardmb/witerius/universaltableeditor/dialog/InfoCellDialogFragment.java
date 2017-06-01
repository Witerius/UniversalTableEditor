package com.wizardmb.witerius.universaltableeditor.dialog;

/**
 * Created by User on 29.03.2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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

import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenTable;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.numOfEditedCell;
import static com.wizardmb.witerius.universaltableeditor.fragment.CellSFragment.allDataOfColumn;


public final class InfoCellDialogFragment extends DialogFragment {

    private int tempId = 0;

    private String[] tempA;

    public static InfoCellDialogFragment newInstance(ModelCellData modelCellData) {
        InfoCellDialogFragment editDialogFragment = new InfoCellDialogFragment();
        Bundle args = new Bundle();

        args.putLong("timeStamp", modelCellData.getTimeStamp());
        args.putString("data", modelCellData.getData());
        args.putInt("table", modelCellData.getTsOfTable());

        editDialogFragment.setArguments(args);
        return editDialogFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String data = args.getString("data");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_cell_info);

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_cell, null);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

        final LinearLayout addCell = (LinearLayout) container.findViewById(R.id.dialog_add_cell);

        tempA = new String[0];
        try {
            tempA = data.split("~#@~");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (allDataOfColumn.get(numOfEditedCell).getTypeOfInput() != 2 &&
                allDataOfColumn.get(numOfEditedCell).getTypeOfInput() != 1) {

            LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_text, null);
            textLL.setId(tempId);

            TextView textAddName = (TextView) textLL.findViewById(R.id.textAddName);
            textAddName.setText(allDataOfColumn.get(numOfEditedCell).getNameColumn());

            EditText editText = (EditText) textLL.findViewById(R.id.et_text);
            editText.setInputType(0);
            editText.setCursorVisible(false);
            editText.setFocusableInTouchMode(false);
            if (!tempA[numOfEditedCell].equals("~#&$~")) {
                editText.setText(tempA[numOfEditedCell]);
            }

            tempId++;
            addCell.addView(textLL);

        }
        else if (allDataOfColumn.get(numOfEditedCell).getTypeOfInput() == 1) {

            LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_text, null);
            textLL.setId(tempId);

            TextView textAddName = (TextView) textLL.findViewById(R.id.textAddName);
            textAddName.setText(allDataOfColumn.get(numOfEditedCell).getNameColumn());

            EditText editText = (EditText) textLL.findViewById(R.id.et_text);
            editText.setInputType(0);
            editText.setCursorVisible(false);
            editText.setFocusableInTouchMode(false);

            String tempData = allDataOfColumn.get(numOfEditedCell).getVariant();
            final String[] temp = tempData.split("~##~");

            String variant = "";
            for (int i1 = 0; i1 < temp.length; i1++) {

                if(!tempA[numOfEditedCell].equals("~#&$~"))
                {
                    if (i1 == Integer.valueOf(tempA[numOfEditedCell]))
                    {
                        variant = variant +  temp[i1]  + "\n";
                    }
                }

            }
            editText.setText( variant);

            tempId++;
            addCell.addView(textLL);

        }
        else
        {
            LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_text, null);
            textLL.setId(tempId);

            TextView textAddName = (TextView) textLL.findViewById(R.id.textAddName);
            textAddName.setText(allDataOfColumn.get(numOfEditedCell).getNameColumn());

            EditText editText = (EditText) textLL.findViewById(R.id.et_text);
            editText.setInputType(0);
            editText.setCursorVisible(false);
            editText.setFocusableInTouchMode(false);

            String tempData = allDataOfColumn.get(numOfEditedCell).getVariant();
            final String[] temp =  tempA[numOfEditedCell].split("~@@#~");
            final String[] tempVariant = tempData.split("~##~");

            String variant = "";
            int max = temp.length - 1;
            for (int i1 = 0; i1 < temp.length; i1++) {

                if(!tempA[numOfEditedCell].equals("~#&$~") )
                {
                        if(i1<max)
                        variant = variant +  tempVariant[ Integer.valueOf(temp[i1])]  + "; ";
                        else
                            variant = variant +  tempVariant[ Integer.valueOf(temp[i1])];

                }
            }
            editText.setText( variant);
            tempId++;
            addCell.addView(textLL);

        }

        builder.setView(container);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }


}