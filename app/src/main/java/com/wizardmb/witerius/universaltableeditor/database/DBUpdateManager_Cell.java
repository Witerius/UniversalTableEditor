package com.wizardmb.witerius.universaltableeditor.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.media.midi.MidiOutputPort;
import android.util.Log;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.DATA_CELL;
import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.DATA_TIME_STAMP_CELL;
import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.DATA_TS_TABLE_CELL;
import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.TABLE_BASE_CELL;

/**
 * Created by User on 24.03.2016.
 */

public final class DBUpdateManager_Cell {

    private SQLiteDatabase database;

    DBUpdateManager_Cell(SQLiteDatabase database) {
        this.database = database;

    }

    private void data(long timeStamp, String data) {

        update(DATA_CELL, timeStamp, data);

    }


    public final void refreshTableMethod(ModelCellData modelAllData) {

        data(modelAllData.getTimeStamp(), modelAllData.getData());

    }


    private void update(String column, long key, String value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(TABLE_BASE_CELL, cv, DATA_TIME_STAMP_CELL  + " = " + key, null);
    }


}