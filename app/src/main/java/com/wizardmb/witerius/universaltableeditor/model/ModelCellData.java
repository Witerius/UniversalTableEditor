package com.wizardmb.witerius.universaltableeditor.model;

import java.util.Date;

/**
 * Created by Witerius on 24.09.2016.
 */

public final class ModelCellData {

    private String data;
    private long timeStamp;
    private int tsOfTable;

    public final String getData() {
        return data;
    }

    public final void setData(String data) {
        this.data = data;
    }

    public final long getTimeStamp() {
        return timeStamp;
    }

    public final void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public final int getTsOfTable() {
        return tsOfTable;
    }

    public final void setTsOfTable(int tsOfTable) {
        this.tsOfTable = tsOfTable;
    }

    public ModelCellData() {
        this.timeStamp = new Date().getTime();
    }

    public ModelCellData(String data, long timeStamp, int tsOfTable) {

        this.tsOfTable = tsOfTable;
        this.timeStamp = timeStamp;
        this.data = data;
    }


}
