package com.wizardmb.witerius.universaltableeditor.adapter;

/**
 * Created by User on 20.03.2016.
 */


import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;

import java.util.ArrayList;
import java.util.List;

public final class CellAdapter {
    private List<ModelCellData> itemList;

    public CellAdapter() {

        itemList = new ArrayList<>();

    }

    public final ModelCellData getItem(int position) {

            return itemList.get(position);
    }

    public final void addItem(ModelCellData item) {

            this.itemList.add(item);
    }

    public final Integer getItemCount()
    {
        return itemList.size();
    }

    public final List<ModelCellData> getAdapter()
    {
        return itemList;
    }


    public final void updateCell(ModelCellData task, int positionInAdapter) {
        itemList.get(positionInAdapter).setData(task.getData());
    }
}
