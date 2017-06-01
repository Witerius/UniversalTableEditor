package com.wizardmb.witerius.universaltableeditor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

/**
 * Created by User on 29.04.2016.
 */
public final class DBHelperA extends SQLiteOpenHelper
{
    static  final String F_IN = " INTEGER";
    static  final String F_ST = " STRING";
    static  final String F_LN = " LONG";
    static  final String F_ZP = ", ";
    static  final String F_SCOB_OP = " (";
    static  final String F_SCOB_CL = " );";
    static  final String F_AUTO_INCR = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
    static  final String F_CREATE_TABLE= "CREATE TABLE ";

    private static final int DBHelperA = 1;
    private static final String DATABASE_NAME = "universaltableeditor_database";

    static final String TABLE_BASE_SAMPLE = "base_sample";
    static final String TABLE_BASE_COLUMN = "base_column";

    static final String TABLE_BASE_CELL = "base_cell";
    static final String TABLE_BASE_TABLES = "base_tables";

    public DBHelperA(Context context) {
        super(context, DATABASE_NAME, null, DBHelperA);

        queryManager_column = new DBQueryManager_Column(getReadableDatabase());
        updateManager_column = new DBUpdateManager_Column(getWritableDatabase());

        queryManager_sample = new DBQueryManager_Sample(getReadableDatabase());
        updateManager_sample = new DBUpdateManager_Sample(getWritableDatabase());

        queryManager_tables = new DBQueryManager_Tables(getReadableDatabase());
        updateManager_tables = new DBUpdateManager_Tables(getWritableDatabase());

        queryManager_cell = new DBQueryManager_Cell(getReadableDatabase());
        updateManager_cell = new DBUpdateManager_Cell(getWritableDatabase());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(BASE_SAMPLE_TABLE_CREATE_SCRIPT);
        db.execSQL(BASE_COLUMN_TABLE_CREATE_SCRIPT);
        db.execSQL(BASE_TABLES_TABLE_CREATE_SCRIPT);
        db.execSQL(BASE_CELL_TABLE_CREATE_SCRIPT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + TABLE_BASE_SAMPLE);
        db.execSQL("DROP TABLE " + TABLE_BASE_COLUMN);
        db.execSQL("DROP TABLE " + TABLE_BASE_TABLES);
        db.execSQL("DROP TABLE " + TABLE_BASE_CELL);

        onCreate(db);

    }

    ////////////////////////////////////////////////
    static final String DATA_TIME_STAMP_COLUMN = "data_time_stamp_column";
    static final String DATA_NAME_COLUMN = "data_name_column";
    static final String DATA_NUM_COLUMN = "data_num_column";    //номер колонны
    static final String DATA_TS_OF_SAMPLE_COLUMN = "data_ts_of_table_column";

    static final String DATA_TYPE_INPUT_COLUMN = "data_type_input_column";
    static final String DATA_VARIANTS_COLUMN = "data_variants_column";// варианты выбора в string, перечисленные через запятую
    static final String DATA_WIDTH_COLUMN = "data_width_column";// ширина ячейки
    static final String DATA_HEIGHT_COLUMN = "data_height_column";// высота

    private String BASE_COLUMN_TABLE_CREATE_SCRIPT = F_CREATE_TABLE
            + TABLE_BASE_COLUMN + F_SCOB_OP + BaseColumns._ID
            + F_AUTO_INCR + DATA_TIME_STAMP_COLUMN + F_LN + F_ZP
            + DATA_NAME_COLUMN + F_ST + F_ZP +DATA_NUM_COLUMN + F_IN + F_ZP
            + DATA_TS_OF_SAMPLE_COLUMN + F_LN + F_ZP
            + DATA_TYPE_INPUT_COLUMN + F_IN + F_ZP + DATA_VARIANTS_COLUMN + F_ST + F_ZP
            + DATA_WIDTH_COLUMN + F_IN + F_ZP + DATA_HEIGHT_COLUMN + F_IN
            + F_SCOB_CL;


    private static final String SELECTION_TIME_STAMP_COLUMN = DATA_TIME_STAMP_COLUMN + " = ?";


    private DBQueryManager_Column queryManager_column;

    private DBUpdateManager_Column updateManager_column;

    public final  void saveColumn(ModelColumn task) {
        ContentValues newValues = new ContentValues();

        newValues.put(DATA_TIME_STAMP_COLUMN, task.getTimeStamp());
        newValues.put(DATA_NAME_COLUMN, task.getNameColumn());
        newValues.put(DATA_NUM_COLUMN, task.getNumColumn());
        newValues.put(DATA_TS_OF_SAMPLE_COLUMN, task.getTsOfTable());

        newValues.put(DATA_TYPE_INPUT_COLUMN, task.getTypeOfInput());
        newValues.put(DATA_VARIANTS_COLUMN, task.getVariant());
        newValues.put(DATA_HEIGHT_COLUMN, task.getHeight());
        newValues.put(DATA_WIDTH_COLUMN, task.getWidth());

        getWritableDatabase().insert(TABLE_BASE_COLUMN, null, newValues);

    }

    public final DBQueryManager_Column queryColumn() {

        return queryManager_column;

    }

    public final DBUpdateManager_Column updateColumn() {

        return updateManager_column;
    }

    public final void removeColumnInBase(long timeStamp) {

        getWritableDatabase().delete(TABLE_BASE_COLUMN, SELECTION_TIME_STAMP_COLUMN, new String[]{Long.toString(timeStamp)});

    }


    ////////////////////////////////////////////////

    static final String DATA_TIME_STAMP_SAMPLE = "data_time_stamp_sample";
    static final String DATA_NAME_SAMPLE = "data_name_sample";

    private String BASE_SAMPLE_TABLE_CREATE_SCRIPT = F_CREATE_TABLE
            + TABLE_BASE_SAMPLE + F_SCOB_OP + BaseColumns._ID
            + F_AUTO_INCR + DATA_TIME_STAMP_SAMPLE + F_LN + F_ZP
            + DATA_NAME_SAMPLE + F_ST
            + F_SCOB_CL;


    private static final String SELECTION_TIME_STAMP_SAMPLE = DATA_TIME_STAMP_SAMPLE + " = ?";


    private DBQueryManager_Sample queryManager_sample;

    private DBUpdateManager_Sample updateManager_sample;

    public final  void saveSampleTable(ModelSample task) {
        ContentValues newValues = new ContentValues();

        newValues.put(DATA_TIME_STAMP_SAMPLE, task.getTimeStamp());
        newValues.put(DATA_NAME_SAMPLE, task.getNameTable());

        getWritableDatabase().insert(TABLE_BASE_SAMPLE, null, newValues);

    }

    public final DBQueryManager_Sample querySampleTable() {

        return queryManager_sample;

    }

    public final DBUpdateManager_Sample updateSampleTable() {

        return updateManager_sample;
    }

    public final void removeSampleTableInBase(long timeStamp) {

        getWritableDatabase().delete(TABLE_BASE_SAMPLE, SELECTION_TIME_STAMP_SAMPLE, new String[]{Long.toString(timeStamp)});

    }


    ////////////////////////////////////////////////

    static final String DATA_TIME_STAMP_TABLES = "data_time_stamp_tables";
    static final String DATA_TS_SAMPLE_TABLES = "data_ts_sample_tables";
    static final String DATA_NAME_TABLES = "name_table";


    private static final String SELECTION_TIME_STAMP_TABLES = DATA_TIME_STAMP_TABLES + " = ?";

    private String BASE_TABLES_TABLE_CREATE_SCRIPT = F_CREATE_TABLE
            + TABLE_BASE_TABLES + F_SCOB_OP + BaseColumns._ID
            + F_AUTO_INCR + DATA_TIME_STAMP_TABLES + F_IN + F_ZP
            + DATA_TS_SAMPLE_TABLES + F_LN + F_ZP
            + DATA_NAME_TABLES + F_ST
            + F_SCOB_CL;
    private DBQueryManager_Tables queryManager_tables;

    private DBUpdateManager_Tables updateManager_tables;

    public final  void saveTable(ModelTable task) {
        ContentValues newValues = new ContentValues();

        newValues.put(DATA_TIME_STAMP_TABLES, task.getTimeStamp());
        newValues.put(DATA_TS_SAMPLE_TABLES, task.getTsSample());
        newValues.put(DATA_NAME_TABLES, task.getNameTable());

        getWritableDatabase().insert(TABLE_BASE_TABLES, null, newValues);

    }

    public final DBQueryManager_Tables queryTables() {

        return queryManager_tables;

    }

    public final DBUpdateManager_Tables updateTables() {

        return updateManager_tables;
    }

    public final void removeTableInBase(long timeStamp) {

        getWritableDatabase().delete(TABLE_BASE_TABLES, SELECTION_TIME_STAMP_TABLES, new String[]{Long.toString(timeStamp)});

    }


    ////////////////////////////////////////////////

    static final String DATA_TIME_STAMP_CELL = "data_time_stamp_cell";
    static final String DATA_TS_TABLE_CELL = "data_ts_table_cell";//INTEGER
    static final String DATA_CELL = "data_cell";

    private String BASE_CELL_TABLE_CREATE_SCRIPT = F_CREATE_TABLE + TABLE_BASE_CELL + F_SCOB_OP + BaseColumns._ID
                + F_AUTO_INCR + DATA_TS_TABLE_CELL + F_IN + F_ZP
                + DATA_TIME_STAMP_CELL  + F_LN + F_ZP + DATA_CELL
                + F_ST + F_SCOB_CL;



    private static final String SELECTION_TIME_STAMP_CELL = DATA_TIME_STAMP_CELL + " = ?";

    private DBQueryManager_Cell queryManager_cell;

    private DBUpdateManager_Cell updateManager_cell;

    public final  void saveCell(ModelCellData task) {
        ContentValues newValues = new ContentValues();

        newValues.put(DATA_TIME_STAMP_CELL, task.getTimeStamp());
        newValues.put(DATA_TS_TABLE_CELL, task.getTsOfTable());
        newValues.put(DATA_CELL, task.getData());

        getWritableDatabase().insert(TABLE_BASE_CELL, null, newValues);

    }

    public final DBQueryManager_Cell queryCell() {

        return queryManager_cell;

    }

    public final DBUpdateManager_Cell updateCell() {

        return updateManager_cell;
    }

    public final void removeCellInBase(long timeStamp) {

        getWritableDatabase().delete(TABLE_BASE_CELL,
                SELECTION_TIME_STAMP_CELL, new String[]{Long.toString(timeStamp)});

    }


    ////////////////////////////////////////////////

}
