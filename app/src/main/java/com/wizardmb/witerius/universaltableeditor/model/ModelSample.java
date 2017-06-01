package com.wizardmb.witerius.universaltableeditor.model;

import java.util.Date;

/**
 * Created by Witerius on 24.09.2016.
 */

public final class ModelSample {

    private long timeStamp;
    private String nameTable;

    public final String getNameTable() {
        return nameTable;
    }

    public final void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public final long getTimeStamp() {
        return timeStamp;
    }

    public final void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ModelSample()
    {
        this.timeStamp = new Date().getTime();
    }

    public ModelSample(long timeStamp, String nameTable) {

        this.timeStamp = timeStamp;
        this.nameTable = nameTable;
    }


}
