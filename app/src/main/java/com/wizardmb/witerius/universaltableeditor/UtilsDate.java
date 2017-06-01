package com.wizardmb.witerius.universaltableeditor;

import java.text.SimpleDateFormat;

/**
 * Created by User on 20.03.2016.
 */
public final class UtilsDate {

    public static String getDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

}
