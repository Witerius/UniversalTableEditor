package com.wizardmb.witerius.universaltableeditor.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.03.2016.
 */

public final class DBQueryManager_Column {

    private SQLiteDatabase database;


    DBQueryManager_Column(SQLiteDatabase database) {
        this.database = database;
        Cursor c;
        c = database.query(DBHelperA.TABLE_BASE_COLUMN, null, null, null, null, null, null);
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

    public final List<ModelColumn> getAllData(long tsOfTable) {
        List<ModelColumn> listModelItems = new ArrayList<>();
        String startC = String.valueOf(tsOfTable);

        String sqlQuery = "SELECT * FROM base_column WHERE data_ts_of_table_column = ?";

        Cursor cursor = database.rawQuery(sqlQuery, new String[] {startC});
        if (cursor.moveToFirst()) {
            do {
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_COLUMN));
                String nameColumn = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_COLUMN));
                int numColumn = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_NUM_COLUMN));

                long tsOfTable1 = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TS_OF_SAMPLE_COLUMN));
                int typeOfInput = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_TYPE_INPUT_COLUMN));

                int height = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_HEIGHT_COLUMN));
                int width = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_WIDTH_COLUMN));
                String variant = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_VARIANTS_COLUMN));

                ModelColumn modelAllData = new ModelColumn( timeStamp, nameColumn, numColumn, tsOfTable1,
                        typeOfInput, height, width, variant);

                listModelItems.add(modelAllData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelItems;
    }
    public final List<ModelColumn> getAllColumnSampleAssociated(long timeStampTable) {
        List<ModelColumn> listModelItems = new ArrayList<>();
        String apartmentIdIs = String.valueOf(timeStampTable);
        String sqlQuery = "SELECT * FROM base_column WHERE data_ts_of_table_column LIKE ? ";

        Cursor cursor = database.rawQuery(sqlQuery, new String[] {apartmentIdIs});
        if (cursor.moveToFirst()) {
            do {
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_COLUMN));
                String nameColumn = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_COLUMN));
                int numColumn = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_NUM_COLUMN));

                long tsOfTable1 = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TS_OF_SAMPLE_COLUMN));

                int typeOfInput = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_TYPE_INPUT_COLUMN));

                int height = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_HEIGHT_COLUMN));
                int width = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_WIDTH_COLUMN));
                String variant = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_VARIANTS_COLUMN));

                ModelColumn modelAllData = new ModelColumn( timeStamp, nameColumn,  numColumn, tsOfTable1,
                        typeOfInput, height, width, variant);

                listModelItems.add(modelAllData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelItems;
    }

}