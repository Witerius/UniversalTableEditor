package com.wizardmb.witerius.universaltableeditor.dialog;

import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;

/**
 * Created by User on 09.04.2016.
 */
public interface EditingCellListener {
    void onCellEdited(ModelCellData updatedTask, String data);
}