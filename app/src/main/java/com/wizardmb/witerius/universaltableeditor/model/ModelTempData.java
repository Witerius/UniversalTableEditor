package com.wizardmb.witerius.universaltableeditor.model;

/**
 * Created by Wizard on 14.03.2017.
 */

public final class ModelTempData {
    private int id;
    private String tempData;
    private int num;


    public final int getNumPosition() {
        return num;
    }

    public final void setNumPosition(int num) {
        this.num = num;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final String getTempData() {
        return tempData;
    }

    public final void setTempData(String tempData) {
        this.tempData = tempData;
    }

    public ModelTempData(int id,  int num) {
        this.id = id;
        this.num = num;
    }
    public ModelTempData(int id, String tempData, int num) {
        this.id = id;
        this.tempData = tempData;
        this.num = num;
    }
}
