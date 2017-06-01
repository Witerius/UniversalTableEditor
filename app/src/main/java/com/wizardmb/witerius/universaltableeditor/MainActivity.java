package com.wizardmb.witerius.universaltableeditor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.multidex.MultiDex;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wizardmb.witerius.universaltableeditor.database.DBHelperA;
import com.wizardmb.witerius.universaltableeditor.dialog.AddingCellDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.AddingColumnDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.AddingKeyDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.AddingSampleDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.AddingTableDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.DuplicateColumnInt;
import com.wizardmb.witerius.universaltableeditor.dialog.EditingCellListener;
import com.wizardmb.witerius.universaltableeditor.dialog.EditingColumnListener;
import com.wizardmb.witerius.universaltableeditor.dialog.EditingSampleListener;
import com.wizardmb.witerius.universaltableeditor.dialog.EditingTableListener;
import com.wizardmb.witerius.universaltableeditor.dialog.PasteInCellListener;
import com.wizardmb.witerius.universaltableeditor.fragment.CellSFragment;

import com.wizardmb.witerius.universaltableeditor.fragment.ColumnRedactFragment;
import com.wizardmb.witerius.universaltableeditor.fragment.SampleRedactFragment;
import com.wizardmb.witerius.universaltableeditor.fragment.StartMenu;
import com.wizardmb.witerius.universaltableeditor.fragment.TableImportFragment;
import com.wizardmb.witerius.universaltableeditor.fragment.TableRedactFragment;
import com.wizardmb.witerius.universaltableeditor.iFace.ColumnRedact;
import com.wizardmb.witerius.universaltableeditor.iFace.StartMenuButton;
import com.wizardmb.witerius.universaltableeditor.iFace.CellRedact;
import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;
import io.fabric.sdk.android.Fabric;

public final class MainActivity extends AppCompatActivity implements StartMenuButton, AddingSampleDialogFragment.AddingSampleListener,
        ColumnRedact, AddingColumnDialogFragment.AddingColumnListener, CellRedact, AddingTableDialogFragment.AddingTableListener,
        AddingCellDialogFragment.AddingCellListener, EditingSampleListener, EditingColumnListener, EditingTableListener,
        EditingCellListener, /*ClearedCell, */PasteInCellListener, DuplicateColumnInt {
    private boolean isRestoreAfterChangeMonitore = false;
    public static DBHelperA dbHelperLLs;
    private int nomerTaba = 0;
    private StartMenu startMenu;
    private CellSFragment cellSFragment;
    private SampleRedactFragment sampleRedactFragment;
    private ColumnRedactFragment columnRedactFragment;
    private TableRedactFragment tableRedactFragment;
    private TableImportFragment tableImportFragment;
    private FloatingActionButton fab;

    public static long nowOpenSample; // при редактировании параметров шаблонов, какой шаблон редактируешь
    public static int nowOpenTable; // при редактировании параметров табл, какую табл редактируешь

    public static int numOfColumnOpenTable = 0; //число колон в открытом шаблоне

    public static boolean isNumChanged = false; //для определения того, что при добавлении колонки, был сменен номер на другой,
    public static boolean isHeightChanged = false; //для определения, что высота сменилась при редакт или добавлении
    // чтоб сменить позиции у других колонок

    private FragmentManager fragmentManager;

    public static final String APP_PREFERENCES = "mysettings";
    public static SharedPreferences mSettings;

    public static final String APP_PREFERENCES_SAVE_ACTIVATED = "saveActivated";
    public static int saveActivated = -1; // 0 не актив, 1-10 актив до определенного числа, 11 активирован

    public static final String APP_PREFERENCES_SAVE_DATE_LIMIT = "saveDayLimit";
    public static long saveDayLimit;

    public static int numOfEditedCell=0;
    public static int numOfColumnBeforeEditing = 0, positionInAdapter;
    public static long dateProverka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if (!isRestoreAfterChangeMonitore) {
            reloadActivity();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onBackPressed() {

        if (nomerTaba == 1 || nomerTaba == 3 || nomerTaba == 5) {

            nomerTaba = 0;
            fab.setVisibility(View.INVISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.container, startMenu);
            ft.commit();
        } else if (nomerTaba == 2) {
            nomerTaba = 1;

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.container, tableRedactFragment);
            ft.commit();
        } else if (nomerTaba == 4) {
            nomerTaba = 3;

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.container, sampleRedactFragment);
            ft.commit();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(APP_PREFERENCES_SAVE_ACTIVATED)) {
            saveActivated = mSettings.getInt(APP_PREFERENCES_SAVE_ACTIVATED, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_SAVE_DATE_LIMIT)) {
            saveDayLimit = mSettings.getLong(APP_PREFERENCES_SAVE_DATE_LIMIT, 0);
        }

    }

    public final void reloadActivity() {
        try {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.app_name);
            dbHelperLLs = new DBHelperA(getApplicationContext());
            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

            if (mSettings.contains(APP_PREFERENCES_SAVE_ACTIVATED)) {
                saveActivated = mSettings.getInt(APP_PREFERENCES_SAVE_ACTIVATED, 0);
            }
            if (mSettings.contains(APP_PREFERENCES_SAVE_DATE_LIMIT)) {
                saveDayLimit = mSettings.getLong(APP_PREFERENCES_SAVE_DATE_LIMIT, 0);
            }

            fragmentManager = getSupportFragmentManager();
            fab = (FloatingActionButton) findViewById(R.id.fab);

            fab.setImageResource(R.drawable.ic_add_white_24dp);
            fab.setVisibility(View.INVISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (nomerTaba == 1) {
                            DialogFragment addingTableDialogFragment = new AddingTableDialogFragment();
                            addingTableDialogFragment.show(fragmentManager, "AddingTableDialogFragment");
                        } else if (nomerTaba == 2) {
                            DialogFragment addingCellDialogFragment = new AddingCellDialogFragment();
                            addingCellDialogFragment.show(fragmentManager, "AddingTableDialogFragment");
                        } else if (nomerTaba == 3) {
                            DialogFragment addingSampleDialogFragment = new AddingSampleDialogFragment();
                            addingSampleDialogFragment.show(fragmentManager, "AddingSampleDialogFragment");
                        } else if (nomerTaba == 4) {
                            columnRedactFragment.getColumnCount();
                            DialogFragment addingColumnDialogFragment = new AddingColumnDialogFragment();
                            addingColumnDialogFragment.show(fragmentManager, "AddingColumnDialogFragment");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

            FragmentManager fragmentManager = getSupportFragmentManager();

            startMenu = new StartMenu();

            // при первом запуске программы
            FragmentTransaction ft = fragmentManager
                    .beginTransaction();
            // добавляем в контейнер при помощи метода add()

            ft.add(R.id.container, startMenu);
            ft.commit();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isRestoreAfterChangeMonitore = true;
        outState.putBoolean("isRestoreAfterChangeMonitore", isRestoreAfterChangeMonitore);

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isRestoreAfterChangeMonitore = savedInstanceState.getBoolean("isRestoreAfterChangeMonitore");

    }

    @Override
    public void redactTable() {
        tableRedactFragment = new TableRedactFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        nomerTaba = 1;
        fab.setVisibility(View.VISIBLE);
        ft1.replace(R.id.container, tableRedactFragment);
        ft1.commit();

    }

    @Override
    public void redactCell() {
        cellSFragment = new CellSFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        nomerTaba = 2;
        fab.setVisibility(View.VISIBLE);
        ft1.replace(R.id.container, cellSFragment);
        ft1.commit();

    }

    @Override
    public void redactSimple() {

        sampleRedactFragment = new SampleRedactFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        nomerTaba = 3;
        fab.setVisibility(View.VISIBLE);
        ft1.replace(R.id.container, sampleRedactFragment);
        ft1.commit();

    }

    @Override
    public void redactColumn() {
        columnRedactFragment = new ColumnRedactFragment();

        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        nomerTaba = 4;
        fab.setVisibility(View.VISIBLE);
        ft1.replace(R.id.container, columnRedactFragment);
        ft1.commit();

    }

    @Override
    public void exportExl() {
        tableImportFragment = new TableImportFragment();

        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        nomerTaba = 5;
        fab.setVisibility(View.INVISIBLE);
        ft1.replace(R.id.container, tableImportFragment);
        ft1.commit();
    }

    @Override
    public void help() {
        DialogFragment addingKeyDialogFragment = new AddingKeyDialogFragment();
        addingKeyDialogFragment.show(getSupportFragmentManager(), "AddingKeyDialogFragment");
    }

    @Override
    public void onSampleAdded(ModelSample newTask) {
        sampleRedactFragment.addSample(newTask, true);
        sampleRedactFragment.redactOfColumn();
        columnRedactFragment.getColumnCount();
        DialogFragment addingColumnDialogFragment = new AddingColumnDialogFragment();
        addingColumnDialogFragment.show(fragmentManager, "AddingSampleDialogFragment");
    }

    @Override
    public void onTableAdded(ModelTable newTask) {
        tableRedactFragment.addTable(newTask, true);
        nowOpenTable = newTask.getTimeStamp();
        nowOpenSample = newTask.getTsSample();
        tableRedactFragment.redactOfTable();

        DialogFragment addingCellDialogFragment = new AddingCellDialogFragment();
        addingCellDialogFragment.show(fragmentManager, "AddingTableDialogFragment");
    }

    @Override
    public void onColumnAdded(ModelColumn newTask) {
        columnRedactFragment.addColumn(newTask, true);
    }

    @Override
    public void onColumnAddingCancel() {
        Toast.makeText(this, R.string.column_adding_cancel, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSampleAddingCancel() {
        Toast.makeText(this, R.string.sample_adding_cancel, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTableAddingCancel() {
        Toast.makeText(this, R.string.table_adding_cancel, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onCellAdded(ModelCellData newTask) {
        cellSFragment.addCell(newTask, true);
    }

    @Override
    public void onCellAddingCancel() {
        Toast.makeText(this, R.string.data_adding_cancel, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSampleEdited(ModelSample updatedTask) {
        sampleRedactFragment.updateSample(updatedTask);
        dbHelperLLs.updateSampleTable().refreshSampleMethod(updatedTask);
    }

    @Override
    public void onColumnEdited(ModelColumn updatedTask) {
        columnRedactFragment.updateColumn(updatedTask);
        dbHelperLLs.updateColumn().refreshColumnMethod(updatedTask);
    }

    @Override
    public void onTableEdited(ModelTable updatedTask) {
        tableRedactFragment.updateTable(updatedTask);
        dbHelperLLs.updateTables().refreshTableMethod(updatedTask);
    }

    @Override
    public void onCellEdited(ModelCellData updatedTask, String data) {
        cellSFragment.updateCell(updatedTask, data);
        dbHelperLLs.updateCell().refreshTableMethod(updatedTask);
    }
    @Override
    public void inCellPasted(ModelCellData updatedTask, String data) {
        cellSFragment.updateCell(updatedTask, data);
        dbHelperLLs.updateCell().refreshTableMethod(updatedTask);

    }

   /* @Override
    public void onCellCleared(ModelCellData updatedTask) {
        cellSFragment.updateCellClear(updatedTask);
        dbHelperLLs.updateCell().refreshTableMethod(updatedTask);
    }
*/
    @Override
    public void onColumnDuplicate(ModelColumn newTask) {

        dbHelperLLs.saveColumn(newTask);
    }

}
