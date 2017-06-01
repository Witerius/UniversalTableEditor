package com.wizardmb.witerius.universaltableeditor.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

/**
 * Created by User on 24.03.2016.
 */

public final class DBUpdateManager_Tables {

    private SQLiteDatabase database;

    DBUpdateManager_Tables(SQLiteDatabase database) {
        this.database = database;

    }

    private void nameTable(int timeStamp, String name) {

        update(DBHelperA.DATA_NAME_TABLES, timeStamp, name);
    }

   private void tsSample(int timeStamp, long ts) {

        update(DBHelperA.DATA_TS_SAMPLE_TABLES, timeStamp, ts);
    }


    public final void refreshTableMethod(ModelTable modelAllData) {

        nameTable(modelAllData.getTimeStamp(), modelAllData.getNameTable());
        tsSample(modelAllData.getTimeStamp(), modelAllData.getTsSample());

    }


    private void update(String column, int key, String value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_TABLES, cv, DBHelperA.DATA_TIME_STAMP_TABLES + " = " + key, null);
    }

    private void update(String column, int key, long value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_TABLES, cv, DBHelperA.DATA_TIME_STAMP_TABLES + " = " + key, null);
    }


}