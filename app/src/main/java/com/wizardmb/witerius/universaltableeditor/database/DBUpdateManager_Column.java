package com.wizardmb.witerius.universaltableeditor.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;

/**
 * Created by User on 24.03.2016.
 */

public final class DBUpdateManager_Column {

    private SQLiteDatabase database;

    DBUpdateManager_Column(SQLiteDatabase database) {
        this.database = database;

    }

    private void typeOfInput(long timeStamp, int typeOfInput) {

        update(DBHelperA.DATA_TYPE_INPUT_COLUMN, timeStamp, typeOfInput);
    }

    private void height(long timeStamp, int height) {

        update(DBHelperA.DATA_HEIGHT_COLUMN, timeStamp, height);
    }

    private void width(long timeStamp, int width) {

        update(DBHelperA.DATA_WIDTH_COLUMN, timeStamp, width);
    }

    private void variant(long timeStamp, String variant) {

        update(DBHelperA.DATA_VARIANTS_COLUMN, timeStamp, variant);
    }

    private void name(long timeStamp, String name) {

        update(DBHelperA.DATA_NAME_COLUMN, timeStamp, name);
    }

    private void num(long timeStamp, int num) {

        update(DBHelperA.DATA_NUM_COLUMN, timeStamp, num);
    }

    private void tsOfTable(long timeStamp, long tsOfTable) {

        update(DBHelperA.DATA_TS_OF_SAMPLE_COLUMN, timeStamp, tsOfTable);
    }


    public final void refreshColumnMethod(ModelColumn modelAllData) {

        typeOfInput(modelAllData.getTimeStamp(),modelAllData.getTypeOfInput());

        height(modelAllData.getTimeStamp(), modelAllData.getHeight());

        width(modelAllData.getTimeStamp(), modelAllData.getWidth());

        variant(modelAllData.getTimeStamp(), modelAllData.getVariant());

        name(modelAllData.getTimeStamp(), modelAllData.getNameColumn());

        num(modelAllData.getTimeStamp(), modelAllData.getNumColumn());

        tsOfTable(modelAllData.getTimeStamp(), modelAllData.getTsOfTable());

    }


    private void update(String column, long key, String value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_COLUMN, cv, DBHelperA.DATA_TIME_STAMP_COLUMN + " = " + key, null);
    }

    private void update(String column, long key, long value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_COLUMN, cv, DBHelperA.DATA_TIME_STAMP_COLUMN + " = " + key, null);
    }

    private void update(String column, long key, int value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_COLUMN, cv, DBHelperA.DATA_TIME_STAMP_COLUMN + " = " + key, null);
    }

}