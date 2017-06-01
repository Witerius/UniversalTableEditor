package com.wizardmb.witerius.universaltableeditor.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.dialog.ADialog;
import com.wizardmb.witerius.universaltableeditor.dialog.DDialog;
import com.wizardmb.witerius.universaltableeditor.dialog.EDialog;
import com.wizardmb.witerius.universaltableeditor.iFace.StartMenuButton;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dateProverka;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.mSettings;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.saveActivated;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.saveDayLimit;


public final class StartMenu extends Fragment {

    private StartMenuButton startMenuButton;

    public StartMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start_menu, container, false);

        final List<ModelSample> modelSamples = new ArrayList<>();
        modelSamples.addAll(dbHelperLLs.querySampleTable().getAllSample(null, null, "data_name_sample"));

        final List<ModelTable> modelTables = new ArrayList<>();
        modelTables.addAll(dbHelperLLs.queryTables().getAllTables(null, null, "name_table"));

        Button redactTable = (Button) rootView.findViewById(R.id.editing_table);
        redactTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelSamples.size() > 0) {
                    if(dateProverka < saveDayLimit)
                    {
                        startMenuButton.redactTable();
                    }
                    else
                    {
                        ADialog dDialog = new ADialog();
                        dDialog.show(getFragmentManager(), "dialog");
                    }
                } else {
                    DDialog dDialog = new DDialog();
                    dDialog.show(getFragmentManager(), "dialog");
                }
            }
        });

        Button redactSample = (Button) rootView.findViewById(R.id.editing_templates);
        redactSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateProverka < saveDayLimit) {
                    startMenuButton.redactSimple();
                }
                else
                {
                    ADialog dDialog = new ADialog();
                    dDialog.show(getFragmentManager(), "dialog");
                }
            }
        });

        Button export = (Button) rootView.findViewById(R.id.export_xml);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelTables.size() > 0) {
                    if (dateProverka < saveDayLimit) {
                    startMenuButton.exportExl();
                }
                else
                {
                    ADialog dDialog = new ADialog();
                    dDialog.show(getFragmentManager(), "dialog");
                }
                } else {
                    EDialog dDialog = new EDialog();
                    dDialog.show(getFragmentManager(), "dialog");
                }
            }
        });

        Button help = (Button) rootView.findViewById(R.id.export_import);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenuButton.help();
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        dateProverka = calendar.getTimeInMillis();

        if (saveActivated == -1) {
            saveActivated = 0;

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(MainActivity.APP_PREFERENCES_SAVE_ACTIVATED, saveActivated);
            editor.apply();

            int date = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);
            int mont = calendar.get(Calendar.MONTH);

            if (date > 21) {
                mont++;
                date = 7;
            } else {
                date = date + 7;
            }

            if (mont == 11) {
                mont = 0;
                year++;
            }
            calendar.set(year, mont, date);

            long dateIs = calendar.getTimeInMillis();

            SharedPreferences.Editor editor1 = mSettings.edit();
            editor1.putLong(MainActivity.APP_PREFERENCES_SAVE_DATE_LIMIT, dateIs);
            editor1.apply();
        }

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            startMenuButton = (StartMenuButton) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
