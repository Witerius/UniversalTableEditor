package com.wizardmb.witerius.universaltableeditor.model;

import java.util.Date;

/**
 * Created by Witerius on 24.09.2016.
 */

public final class ModelColumn {

    private long timeStamp;
    private String nameColumn;

    private int numColumn;
    private long tsOfTable;

    private int typeOfInput;
    private int height;

    private int width;
    private String variant;

    public final long getTimeStamp() {
        return timeStamp;
    }

    public final void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public final String getNameColumn() {
        return nameColumn;
    }

    public final void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public final int getNumColumn() {
        return numColumn;
    }

    public final void setNumColumn(int numColumn) {
        this.numColumn = numColumn;
    }

    public final long getTsOfTable() {
        return tsOfTable;
    }

    public final void setTsOfTable(long tsOfTable) {
        this.tsOfTable = tsOfTable;
    }

    public final int getTypeOfInput() {
        return typeOfInput;
    }

    public final void setTypeOfInput(int typeOfInput) {
        this.typeOfInput = typeOfInput;
    }

    public final int getHeight() {
        return height;
    }

    public final void setHeight(int height) {
        this.height = height;
    }

    public final int getWidth() {
        return width;
    }

    public final void setWidth(int width) {
        this.width = width;
    }

    public final String getVariant() {
        return variant;
    }

    public final void setVariant(String variant) {
        this.variant = variant;
    }

    public ModelColumn()
    {
        this.timeStamp = new Date().getTime();
    }
    public ModelColumn(long timeStamp, String nameColumn, int numColumn, long tsOfTable, int typeOfInput,
                       int height, int width, String variant) {

        this.timeStamp = timeStamp;
        this.nameColumn = nameColumn;
        this.numColumn = numColumn;
        this.tsOfTable = tsOfTable;

        this.typeOfInput = typeOfInput;
        this.height = height;
        this.width = width;
        this.variant = variant;

    }


}
