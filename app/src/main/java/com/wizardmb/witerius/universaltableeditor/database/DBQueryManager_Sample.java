package com.wizardmb.witerius.universaltableeditor.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.universaltableeditor.model.ModelSample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.03.2016.
 */

public final class DBQueryManager_Sample {

    private SQLiteDatabase database;

    DBQueryManager_Sample(SQLiteDatabase database) {
        this.database = database;
        Cursor c;
        c = database.query(DBHelperA.TABLE_BASE_SAMPLE, null, null, null, null, null, null);
        logCursor(c);
        c.close();

    }

    // вывод в лог данных из курсора
    private void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }

                } while (c.moveToNext());
            }
        }
    }

    public final List<ModelSample> getAllSample(String selection, String[] selectionArgs, String orderBy) {

            List<ModelSample> listModel = new ArrayList<>();

            Cursor cursor = database.query(DBHelperA.TABLE_BASE_SAMPLE, null, selection, selectionArgs, null, null, orderBy);
            if (cursor.moveToFirst()) {
                do {
                    long ts = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_SAMPLE));
                    String name = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_SAMPLE));

                    ModelSample modelSample = new ModelSample(ts, name);
                    listModel.add(modelSample);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return listModel;
        }


}