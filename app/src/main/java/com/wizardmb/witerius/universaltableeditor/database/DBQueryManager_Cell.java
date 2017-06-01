package com.wizardmb.witerius.universaltableeditor.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.DATA_CELL;
import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.DATA_TIME_STAMP_CELL;
import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.DATA_TS_TABLE_CELL;
import static com.wizardmb.witerius.universaltableeditor.database.DBHelperA.TABLE_BASE_CELL;

/**
 * Created by User on 24.03.2016.
 */

public final class DBQueryManager_Cell {

    private SQLiteDatabase database;

    DBQueryManager_Cell(SQLiteDatabase database) {
        this.database = database;
        Cursor c;
        c = database.query(TABLE_BASE_CELL, null, null, null, null, null, null);
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

  /*  public final List<ModelCellData> getAllCell(String selection, String[] selectionArgs, String orderBy) {

            List<ModelCellData> listModel = new ArrayList<>();

            Cursor cursor = database.query(TABLE_BASE_CELL, null, selection, selectionArgs, null, null, orderBy);
            if (cursor.moveToFirst()) {
                do {
                    int tsTable = cursor.getInt(cursor.getColumnIndex(DATA_TS_TABLE_CELL));
                    long tsCell = cursor.getLong(cursor.getColumnIndex(DATA_TIME_STAMP_CELL));
                    String data = cursor.getString(cursor.getColumnIndex(DATA_CELL));

                    ModelCellData modelCellData = new ModelCellData(data, tsCell, tsTable);
                    listModel.add(modelCellData);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return listModel;
        }*/

    public final List<ModelCellData> getAllCellAssociated(int idTable) {
        List<ModelCellData> listModelItems = new ArrayList<>();
        String apartmentIdIs = String.valueOf(idTable);
        String sqlQuery = "SELECT * FROM " + TABLE_BASE_CELL + " WHERE " + DATA_TS_TABLE_CELL + " LIKE ? ";

        Cursor cursor = database.rawQuery(sqlQuery, new String[] {apartmentIdIs});
        if (cursor.moveToFirst()) {
            do {
                int tsTable = cursor.getInt(cursor.getColumnIndex(DATA_TS_TABLE_CELL));
                long tsCell = cursor.getLong(cursor.getColumnIndex(DATA_TIME_STAMP_CELL));
                String data = cursor.getString(cursor.getColumnIndex(DATA_CELL));

                ModelCellData modelCellData = new ModelCellData(data, tsCell, tsTable);

                listModelItems.add(modelCellData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelItems;
    }


}