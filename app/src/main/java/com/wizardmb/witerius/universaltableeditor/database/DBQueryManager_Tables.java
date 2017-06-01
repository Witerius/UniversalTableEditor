package com.wizardmb.witerius.universaltableeditor.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.03.2016.
 */

public final class DBQueryManager_Tables {

    private SQLiteDatabase database;

    DBQueryManager_Tables(SQLiteDatabase database) {
        this.database = database;
        Cursor c;
        c = database.query(DBHelperA.TABLE_BASE_TABLES, null, null, null, null, null, null);
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

    public final List<ModelTable> getAllTables(String selection, String[] selectionArgs, String orderBy) {

            List<ModelTable> listModel = new ArrayList<>();

            Cursor cursor = database.query(DBHelperA.TABLE_BASE_TABLES, null, selection, selectionArgs, null, null, orderBy);
            if (cursor.moveToFirst()) {
                do {
                    int ts = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_TABLES));
                    long tsSample = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TS_SAMPLE_TABLES));
                    String name = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_TABLES));

                    ModelTable modelTable = new ModelTable(ts, tsSample, name);
                    listModel.add(modelTable);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return listModel;
        }

   public final List<ModelTable> getAllTablesSampleAssociated(long timeStampSample) {
       List<ModelTable> listModel = new ArrayList<>();
       String apartmentIdIs = String.valueOf(timeStampSample);
       String sqlQuery = "SELECT * FROM base_tables WHERE data_ts_sample_tables LIKE ? ";

       Cursor cursor = database.rawQuery(sqlQuery, new String[] {apartmentIdIs});
       if (cursor.moveToFirst()) {
           do {
               int ts = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_TABLES));
               long tsSample = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TS_SAMPLE_TABLES));
               String name = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_TABLES));

               ModelTable modelTable = new ModelTable(ts, tsSample, name);
               listModel.add(modelTable);
           } while (cursor.moveToNext());
       }
       cursor.close();
       return listModel;
   }

}