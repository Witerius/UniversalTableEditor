package com.wizardmb.witerius.universaltableeditor.model;

import android.content.SharedPreferences;

import com.wizardmb.witerius.universaltableeditor.MainActivity;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.mSettings;

/**
 * Created by Witerius on 24.09.2016.
 */

public final class ModelTable {

    private int timeStamp;
    private long tsSample;

    private String nameTable;

    public final String getNameTable() {
        return nameTable;
    }

    public final void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public final int getTimeStamp() {
        return timeStamp;
    }

    public final void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public final long getTsSample() {
        return tsSample;
    }

    public final void setTsSample(long tsSample) {
        this.tsSample = tsSample;
    }

    public ModelTable() {
        MainActivity.saveActivated++;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(MainActivity.APP_PREFERENCES_SAVE_ACTIVATED, MainActivity.saveActivated);
        editor.apply();
        this.timeStamp = MainActivity.saveActivated;
    }

    public ModelTable(int timeStamp, long tsSample, String nameTable) {

        this.timeStamp = timeStamp;

        this.tsSample = tsSample;

        this.nameTable = nameTable;
    }


}
