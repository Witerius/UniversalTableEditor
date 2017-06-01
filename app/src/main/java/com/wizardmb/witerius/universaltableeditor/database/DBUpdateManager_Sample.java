package com.wizardmb.witerius.universaltableeditor.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.universaltableeditor.model.ModelSample;

/**
 * Created by User on 24.03.2016.
 */

public final class DBUpdateManager_Sample {

    private SQLiteDatabase database;

    DBUpdateManager_Sample(SQLiteDatabase database) {
        this.database = database;

    }

    private void nameTable(long timeStamp, String name) {

        update(DBHelperA.DATA_NAME_SAMPLE, timeStamp, name);
    }


    public final void refreshSampleMethod(ModelSample modelAllData) {

        nameTable(modelAllData.getTimeStamp(), modelAllData.getNameTable());

    }


    private void update(String column, long key, String value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_SAMPLE, cv, DBHelperA.DATA_TIME_STAMP_SAMPLE + " = " + key, null);
    }

}