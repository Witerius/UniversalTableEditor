package com.wizardmb.witerius.universaltableeditor.dialog;

/**
 * Created by User on 29.03.2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenTable;


public final class EditTableDialogFragment extends DialogFragment {

    private EditingTableListener editingTableListener;

    public static EditTableDialogFragment newInstance(ModelTable modelTable) {
        EditTableDialogFragment editDialogFragment = new EditTableDialogFragment();
        Bundle args = new Bundle();
        args.putInt("timeStamp", modelTable.getTimeStamp());
        args.putLong("tsTable", modelTable.getTsSample());
        args.putString("nameTable", modelTable.getNameTable());

        editDialogFragment.setArguments(args);
        return editDialogFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            editingTableListener = (EditingTableListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingTableListener");
        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        int timeStamp = args.getInt("timeStamp");
        long tsTable = args.getLong("tsTable");
        String nameTable = args.getString("nameTable");

        final ModelTable modelTable = new ModelTable(timeStamp, tsTable, nameTable);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_table_edit);
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_table, null);

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.tilDialogTableName);
        final EditText etName  = tilName.getEditText();

        tilName.setHint(getResources().getString(R.string.name_table));
        try {
            etName.setText(modelTable.getNameTable());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final List<ModelSample> modelSamples = new ArrayList<>();
        modelSamples.addAll(dbHelperLLs.querySampleTable().getAllSample(null, null, "data_name_sample"));

        List<String> nameOfSample = new ArrayList<>();
        for(int i=0; i<modelSamples.size(); i++)
        {
            nameOfSample.add(modelSamples.get(i).getNameTable());

        }
        ArrayAdapter<String> selectNumAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, nameOfSample);
        Spinner spNum = (Spinner) container.findViewById(R.id.spListOfSample);
        spNum.setAdapter(selectNumAdapter);

        for(int i=0; i<modelSamples.size(); i++)
        {
            if(modelTable.getTsSample() == modelSamples.get(i).getTimeStamp())
            {
                spNum.setSelection(i);
            }
        }
        spNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                modelTable.setTsSample(modelSamples.get(position).getTimeStamp());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(container);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    modelTable.setNameTable(etName.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nowOpenTable = modelTable.getTimeStamp();

                editingTableListener.onTableEdited(modelTable);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
                    tilName.setError(getResources().getString(R.string.dialog_error_empty_name_table));
                }

                etName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilName.setError(getResources().getString(R.string.dialog_error_empty_name_table));
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