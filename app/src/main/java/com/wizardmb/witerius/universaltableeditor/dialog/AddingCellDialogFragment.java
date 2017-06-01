package com.wizardmb.witerius.universaltableeditor.dialog;

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

/**
 * Created by Wizard on 10.03.2017.
 */

public final class AddingCellDialogFragment extends DialogFragment {
    private int tempId = 0;
    private AddingCellListener addingCellListener;
    private String tempData;
    private int dateViewAdd = 0;
    private int timeViewAdd = 0;
    private int radioViewAdd = 0;
    private int checkBoxViewAdd = 0;

    private List<Integer> tempTypeOfData;
    private List<ModelDate> tempTypeOfDate;
    private List<ModelTempData> tempTypeOfTime;
    private List<ModelTempData> tempTypeOfRadio;
    private List<List<String>> tempTypeOfCheckBox;

    private MainActivity activityM;

    public interface AddingCellListener {
        void onCellAdded(ModelCellData newTask);

        void onCellAddingCancel();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addingCellListener = (AddingCellListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddingColumnListener");
        }

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

        builder.setTitle(R.string.dialog_cell_add);
        final ModelCellData modelCellData = new ModelCellData();
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_cell, null);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

        final LinearLayout addCell = (LinearLayout) container.findViewById(R.id.dialog_add_cell);
        tempTypeOfData = new ArrayList<>();
        tempTypeOfDate = new ArrayList<>();
        tempTypeOfTime = new ArrayList<>();
        tempTypeOfRadio = new ArrayList<>();
        tempTypeOfCheckBox = new ArrayList<>();

        for (int i = 0; i < CellSFragment.allDataOfColumn.size(); i++) {
            if (CellSFragment.allDataOfColumn.get(i).getTypeOfInput() == 0 ) {
                tempTypeOfData.add(0);
                LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_text, null);
                textLL.setId(tempId);

                TextView textAddName = (TextView) textLL.findViewById(R.id.textAddName);
                textAddName.setText(CellSFragment.allDataOfColumn.get(i).getNameColumn());

                EditText editText = (EditText) textLL.findViewById(R.id.et_text);
                editText.setInputType(1);
                editText.setCursorVisible(true);
                editText.setFocusableInTouchMode(true);

                tempId++;
                addCell.addView(textLL);
            } else if (CellSFragment.allDataOfColumn.get(i).getTypeOfInput() == 3) {
                tempTypeOfData.add(3);
                String date =  UtilsDate.getDate(calendar.getTimeInMillis());
                final ModelDate modelDate = new ModelDate(tempId, date, dateViewAdd);

                tempTypeOfDate.add(modelDate);

                LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_date, null);
                textLL.setId(tempId);

                TextView textAddName = (TextView) textLL.findViewById(R.id.textAddDate);
                textAddName.setText(CellSFragment.allDataOfColumn.get(i).getNameColumn());

                DatePicker mDatePicker = (DatePicker) textLL.findViewById(R.id.datePicker);
                mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                            @Override
                            public void onDateChanged(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                monthOfYear++;
                                String monthTemp, dayTemp;
                                if(monthOfYear<10)
                                {
                                    monthTemp = "0"+monthOfYear;
                                }
                                else
                                {
                                    monthTemp = ""+ monthOfYear;
                                }

                                if(dayOfMonth<10)
                                {
                                    dayTemp  = "0"+dayOfMonth;
                                }
                                else
                                {
                                    dayTemp = ""+ dayOfMonth;
                                }
                                modelDate.setDateString(dayTemp+"."+monthTemp+"."+year);

                                tempTypeOfDate.set(modelDate.getNum(), modelDate);

                            }
                        });
                dateViewAdd++;
                tempId++;
                addCell.addView(textLL);

            } else if (CellSFragment.allDataOfColumn.get(i).getTypeOfInput() == 4) {
                tempTypeOfData.add(4);
                LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_time, null);
                textLL.setId(tempId);
                final Calendar calendar1 = Calendar.getInstance();
                String tempHour = ""+calendar1.get(Calendar.HOUR_OF_DAY);
                int hourInt = Integer.valueOf(tempHour);
                if(hourInt <10)
                {
                    tempHour = "0" + tempHour;
                }

                String tempMinut = ""+calendar1.get(Calendar.MINUTE);
                int minuteInt = Integer.valueOf(tempMinut);
                if(minuteInt <10)
                {
                    tempMinut = "0" + tempMinut;
                }
                String time = tempHour + " : " + tempMinut;
                final ModelTempData modelTempData = new ModelTempData(tempId, time, timeViewAdd);
                tempTypeOfTime.add(modelTempData);

                TextView textAddName = (TextView) textLL.findViewById(R.id.textAddTime);
                textAddName.setText(CellSFragment.allDataOfColumn.get(i).getNameColumn());

                TimePicker mTimePicker = (TimePicker) textLL.findViewById(R.id.timePicker);

                mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                        String hourTemp, minuteTemp;
                        if(hourOfDay<10)
                        {
                            hourTemp  = "0"+hourOfDay;
                        }
                        else
                        {
                            hourTemp = ""+ hourOfDay;
                        }
                        if(minute<10)
                        {
                            minuteTemp  = "0"+minute;
                        }
                        else
                        {
                            minuteTemp = ""+ minute;
                        }
                        modelTempData.setTempData(hourTemp + " : " + minuteTemp);
                        tempTypeOfTime.set(modelTempData.getNumPosition(), modelTempData);

                    }

                });
                timeViewAdd++;
                tempId++;
                addCell.addView(textLL);

            } else if (CellSFragment.allDataOfColumn.get(i).getTypeOfInput() == 1) {
                tempTypeOfData.add(1);
                LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_radio_b, null);
                textLL.setId(tempId);

                TextView textAddName = (TextView) textLL.findViewById(R.id.textAddGroup);
                textAddName.setText(CellSFragment.allDataOfColumn.get(i).getNameColumn());

                RadioGroup radioGroup = (RadioGroup) textLL.findViewById(R.id.radioGroup);
                radioGroup.setVisibility(View.VISIBLE);

                String tempData = CellSFragment.allDataOfColumn.get(i).getVariant();
                final String[] temp = tempData.split("~##~");

                final ModelTempData modelTempData = new ModelTempData(tempId, radioViewAdd);

                int radioButtonId = 0;

                for (int i1 = 0; i1 < temp.length; i1++) {
                    final RadioButton newRadioButton = new RadioButton(activityM);
                    newRadioButton.setText(temp[i1]);

                    if (i1 == 0) {
                        newRadioButton.setChecked(true);
                        modelTempData.setTempData("0");
                        tempTypeOfRadio.add(modelTempData);

                    }
                    newRadioButton.setId(radioButtonId);
                    radioGroup.addView(newRadioButton);

                    newRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                           if(b) {
                               modelTempData.setTempData(String.valueOf(newRadioButton.getId()));
                               tempTypeOfRadio.set(modelTempData.getNumPosition(), modelTempData);

                           }
                        }
                    });

                    radioButtonId++;

                }
                radioViewAdd++;
                tempId++;
                addCell.addView(textLL);

            } else if (CellSFragment.allDataOfColumn.get(i).getTypeOfInput() == 2) {
                tempTypeOfData.add(2);
                LinearLayout textLL = (LinearLayout) inflater.inflate(R.layout.view_add_radio_b, null);
                textLL.setId(tempId);

                ViewGroup.MarginLayoutParams blp = new ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                blp.setMargins(30, 0, 15, 0);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(blp);
                textLL.setLayoutParams(layoutParams);

                TextView textAddName = (TextView) textLL.findViewById(R.id.textAddGroup);
                textAddName.setText(CellSFragment.allDataOfColumn.get(i).getNameColumn());

                String tempData = CellSFragment.allDataOfColumn.get(i).getVariant();
                final String[] temp = tempData.split("~##~");

                final List<String> tempVariant = new ArrayList<>();

                int checkBoxId = 0;

                for (int i1 = 0; i1 < temp.length; i1++) {
                    final CheckBox checkBox = (CheckBox) inflater.inflate(R.layout.new_checkbox, null);
                    checkBox.setId(checkBoxId);

                    checkBoxId++;
                    checkBox.setText(temp[i1]);
                    final String tempString =  ""+i1;

                    checkBox.setChecked(false);

                    tempVariant.add("~@@#~");

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                tempVariant.set(checkBox.getId(), tempString);
                            } else {
                                tempVariant.set(checkBox.getId(), "~@@#~");
                            }
                        }
                    });
                    textLL.addView(checkBox);
                }
                tempTypeOfCheckBox.add(tempVariant);

                tempId++;
                addCell.addView(textLL);

            }


        }

        builder.setView(container);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dateViewAdd = 0;
                timeViewAdd = 0;
                radioViewAdd = 0;

                for (int i = 0; i < tempTypeOfData.size(); i++) {
                    if (tempTypeOfData.get(i) == 0 ) {
                        LinearLayout textLL = (LinearLayout) addCell.getChildAt(i);

                        EditText editText = (EditText) textLL.findViewById(R.id.et_text);
                        String antiEmpty ="~#&$~";
                        if(editText.length() != 0)
                        {
                            antiEmpty = ""+editText.getText();
                        }
                        if (i == 0) {
                            tempData = "" + antiEmpty;
                        } else {
                            tempData = tempData + "~#@~" + antiEmpty;
                        }

                    } else if (tempTypeOfData.get(i) == 3) {
                        if (i == 0) {
                            tempData = tempTypeOfDate.get(dateViewAdd).getDateString();
                        } else {
                            String date = tempTypeOfDate.get(dateViewAdd).getDateString();

                            tempData = tempData + "~#@~" + date;

                        }
                        dateViewAdd++;
                    } else if (tempTypeOfData.get(i) == 4) {
                        if (i == 0) {
                            tempData = "" + tempTypeOfTime.get(timeViewAdd).getTempData();
                        } else {
                            tempData = tempData + "~#@~" + tempTypeOfTime.get(timeViewAdd).getTempData();

                        }
                        timeViewAdd++;

                    } else if (tempTypeOfData.get(i) == 1) {

                        if (i == 0) {
                            tempData = "" + tempTypeOfRadio.get(radioViewAdd).getTempData();
                        } else {
                            tempData = tempData + "~#@~" + tempTypeOfRadio.get(radioViewAdd).getTempData();

                        }
                        radioViewAdd++;
                    } else if (tempTypeOfData.get(i) == 2) {

                        String tempString = "";
                        boolean isFirst = true;

                           for(int ii1 = 0; ii1<tempTypeOfCheckBox.get(checkBoxViewAdd).size(); ii1++)
                           {
                               if (!tempTypeOfCheckBox.get(checkBoxViewAdd).get(ii1).equals("~@@#~") && isFirst) {

                                   tempString =  tempTypeOfCheckBox.get(checkBoxViewAdd).get(ii1);
                                   isFirst = false;
                               }
                               else if (!tempTypeOfCheckBox.get(checkBoxViewAdd).get(ii1).equals("~@@#~") && !isFirst) {

                                   tempString = tempString + "~@@#~" + tempTypeOfCheckBox.get(checkBoxViewAdd).get(ii1);

                               }

                           }

                        checkBoxViewAdd++;

                        if (i == 0) {
                            tempData = "" + tempString;
                        } else {
                            tempData = tempData + "~#@~" + tempString;
                        }

                    }

                }

                try {
                    modelCellData.setData(tempData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                modelCellData.setTsOfTable(nowOpenTable);
                addingCellListener.onCellAdded(modelCellData);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                addingCellListener.onCellAddingCancel();

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

}
