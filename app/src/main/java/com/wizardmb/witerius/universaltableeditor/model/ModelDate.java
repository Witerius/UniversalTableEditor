package com.wizardmb.witerius.universaltableeditor.model;

/**
 * Created by Wizard on 14.03.2017.
 */

public final class ModelDate
{
    private int id;

    private String dateString;
    private int num;

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public final int getNum() {
        return num;
    }

    public final void setNum(int num) {
        this.num = num;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public ModelDate(int id, String date, int num) {
        this.id = id;
        this.dateString = date;
        this.num = num;
    }
}
