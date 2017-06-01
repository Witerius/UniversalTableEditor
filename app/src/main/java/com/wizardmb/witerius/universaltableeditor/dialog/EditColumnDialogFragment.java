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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.fragment.CellSFragment;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.isNumChanged;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.numOfColumnBeforeEditing;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.numOfColumnOpenTable;
import static com.wizardmb.witerius.universaltableeditor.fragment.ColumnRedactFragment.heightColumn;


public final class EditColumnDialogFragment extends DialogFragment {

    private EditingColumnListener editingColumnListener;
    private MainActivity activityM;
    private LinearLayout llVariants;

    private int tempId = 0;
    private List<RelativeLayout> tempRL;

    private List<String> typeOfInput;

    public static EditColumnDialogFragment newInstance(ModelColumn modelColumn) {
        EditColumnDialogFragment editDialogFragment = new EditColumnDialogFragment();
        Bundle args = new Bundle();
        args.putLong("timeStamp", modelColumn.getTimeStamp());
        args.putString("nameColumn", modelColumn.getNameColumn());
        args.putInt("numColumn", modelColumn.getNumColumn());

        args.putLong("tsOfTablet", modelColumn.getTsOfTable());
        args.putInt("typeOfInput", modelColumn.getTypeOfInput());

        args.putInt("height", modelColumn.getHeight());
        args.putInt("width", modelColumn.getWidth());
        args.putString("variant", modelColumn.getVariant());

        editDialogFragment.setArguments(args);
        return editDialogFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            editingColumnListener = (EditingColumnListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingColumnListener");
        }
        try {
            activityM = (MainActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        long timeStamp = args.getLong("timeStamp");
        String nameColumn = args.getString("nameColumn");
        int numColumn = args.getInt("numColumn");

        long tsOfTable = args.getLong("tsOfTablet");
        int typeOfInput1 = args.getInt("typeOfInput");

        int height = args.getInt("height");
        int width = args.getInt("width");
        String variant = args.getString("variant");

        final ModelColumn modelColumn = new ModelColumn( timeStamp, nameColumn,  numColumn,  tsOfTable,
                typeOfInput1, height, width,  variant);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_column_edit);
        tempRL = new ArrayList<>();
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_column, null);

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.tilDialogColumnName);
        final EditText etName = tilName.getEditText();
        try {
            etName.setText(modelColumn.getNameColumn());
        } catch (Exception e) {
            e.printStackTrace();
        }

        llVariants = (LinearLayout) container.findViewById(R.id.llVariants);

        final TextInputLayout tilHeight = (TextInputLayout) container.findViewById(R.id.tilDialogHeight);
        final EditText etHeight = tilHeight.getEditText();
        try {
            etHeight.setText(String.valueOf(modelColumn.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final TextInputLayout tilWidth = (TextInputLayout) container.findViewById(R.id.tilDialogWidth);
        final EditText etWidth = tilWidth.getEditText();
        try {
            etWidth.setText(String.valueOf(modelColumn.getWidth()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        tilName.setHint(getResources().getString(R.string.name_column));
        tilHeight.setHint(getResources().getString(R.string.height));
        tilWidth.setHint(getResources().getString(R.string.width));

        typeOfInput = new ArrayList<>();
        typeOfInput.add(activityM.getResources().getString(R.string.type_input_1));
        typeOfInput.add(activityM.getResources().getString(R.string.type_input_2));
        typeOfInput.add(activityM.getResources().getString(R.string.type_input_3));
        typeOfInput.add(activityM.getResources().getString(R.string.type_data_4));
        typeOfInput.add(activityM.getResources().getString(R.string.type_data_5));
        ArrayAdapter<String> selectTypeInputAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, typeOfInput);

        Spinner spTypeInput = (Spinner) container.findViewById(R.id.spTypeOfInput);
        spTypeInput.setAdapter(selectTypeInputAdapter);

        spTypeInput.setSelection(modelColumn.getTypeOfInput());

        spTypeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                llVariants.removeAllViews();
                tempId = 0;

                tempRL = new ArrayList<>();

                if (position == 0) {
                    modelColumn.setTypeOfInput(0);

                } else if (position == 1) {
                    modelColumn.setTypeOfInput(1);

                } else if (position == 2) {
                    modelColumn.setTypeOfInput(2);

                }else if (position == 3) {
                    modelColumn.setTypeOfInput(3);

                } else if (position == 4) {
                    modelColumn.setTypeOfInput(4);

                }
                if (position == 1 || position == 2) {
                    llVariants.setVisibility(View.VISIBLE);

                    LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_text, null);

                    TextView text = (TextView) textLL.findViewById(R.id.textText);
                    text.setText(activityM.getResources().getString(R.string.add_options));

                    llVariants.addView(textLL);

                    String tempData = modelColumn.getVariant();
                    final String[] temp;
                    try {
                        temp = tempData.split("~##~");
                        addNewView(inflater, true, true, temp[0]);

                        for (int i1 = 1; i1 < temp.length; i1++) {

                            addNewView(inflater, false, true, temp[i1]);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        addNewView(inflater, true, true, "");
                    }


                } else {

                    llVariants.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> numOfColumn = new ArrayList<>();
        try {
            for (int i = 0; i < numOfColumnOpenTable; i++) {
                int t = i + 1;
                numOfColumn.add("" + t);
            }

        } catch (Exception e) {
            e.printStackTrace();
            numOfColumn.add("" + 1);

        }
        ArrayAdapter<String> selectNumAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, numOfColumn);

        Spinner spNum = (Spinner) container.findViewById(R.id.spNumOfColumn);
        spNum.setAdapter(selectNumAdapter);

        spNum.setSelection(modelColumn.getNumColumn()-1);


        spNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int tempNum = position + 1;
                numOfColumnBeforeEditing = modelColumn.getNumColumn();

                modelColumn.setNumColumn(tempNum);

                isNumChanged = true;

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
                    modelColumn.setNameColumn(etName.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modelColumn.setTsOfTable(nowOpenSample);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (etHeight.length() != 0) {
                        modelColumn.setHeight(Integer.valueOf(etHeight.getText().toString()));
                    } else {
                        modelColumn.setHeight(100);
                    }
                    if(heightColumn != modelColumn.getHeight())
                    {
                        MainActivity.isHeightChanged = true;
                        heightColumn = modelColumn.getHeight();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (etWidth.length() != 0) {
                        modelColumn.setWidth(Integer.valueOf(etWidth.getText().toString()));
                    } else {
                        modelColumn.setWidth(300);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (modelColumn.getTypeOfInput() == 1 || modelColumn.getTypeOfInput() == 2)
                {
                    String temp = "";
                    temp = getTextFromNewView(0, temp, true);

                    for (int i = 1; i < tempRL.size(); i++) {

                        temp = getTextFromNewView(i, temp, false);
                    }
                    modelColumn.setVariant(temp);
                }


                editingColumnListener.onColumnEdited(modelColumn);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isNumChanged = false;

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
                    tilName.setError(getResources().getString(R.string.dialog_error_empty_name_column));
                }

                etName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilName.setError(getResources().getString(R.string.dialog_error_empty_name_column));
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
    private void addNewView(final LayoutInflater inflater, final boolean isStart, final boolean addText,
                            final String textVariant) {

        final RelativeLayout alternative = (RelativeLayout) inflater.inflate(R.layout.view_add_alternative, null);
        alternative.setId(tempId);

        if(addText)
        {
            EditText etAlternative = (EditText) alternative.findViewById(R.id.etAlternative);
            etAlternative.setText(textVariant);

        }

        ImageView plus = (ImageView) alternative.findViewById(R.id.iv_plus1);
        if (isStart) {
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNewView(inflater, false, false, null);
                }
            });
        } else {
            plus.setImageResource(R.drawable.minus);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    llVariants.removeView(alternative);

                    tempRL.remove(alternative);
                }
            });
        }
        tempRL.add(alternative);
        llVariants.addView(alternative);
        tempId++;

    }

    private String getTextFromNewView(int id, String tempVariant, boolean first) {

        final RelativeLayout alternative = tempRL.get(id);

        EditText tvAlternative = (EditText) alternative.findViewById(R.id.etAlternative);
        if (first) {
            try {
                tempVariant = String.valueOf(tvAlternative.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                tempVariant = tempVariant + "~##~" + String.valueOf(tvAlternative.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tempVariant;
    }
}