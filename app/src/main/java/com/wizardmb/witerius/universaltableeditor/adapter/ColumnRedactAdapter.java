package com.wizardmb.witerius.universaltableeditor.adapter;

/**
 * Created by User on 20.03.2016.
 */

import android.content.res.Resources;
import android.nfc.Tag;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.dialog.CDialog;
import com.wizardmb.witerius.universaltableeditor.fragment.ColumnRedactFragment;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;


public final class ColumnRedactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MenuItem myActionEditItem;
    private MenuItem myActionDuplicateItem;
    private MenuItem myActionDeleteItem;

    private MainActivity activityM;
    private List<String> typeOfInput;

    private List<ModelColumn> itemColumnModel;

    private ColumnRedactFragment columnRedactFragment;

    private List<ModelTable>  allAssociatedTable;
    private int[] colors = new int[2];
    private int nomerCveta = 0;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_column_redact, viewGroup, false);
        TextView name = (TextView) v.findViewById(R.id.tvNameColumn);
        TextView num = (TextView) v.findViewById(R.id.tvNumColumn);
        TextView input = (TextView) v.findViewById(R.id.tvTypeInput);
        TextView data = (TextView) v.findViewById(R.id.tvTypeData);
        return new ColumnViewHolder(v, name, num, input, data);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ModelColumn itemModel = itemColumnModel.get(position);
        final Resources resources = viewHolder.itemView.getResources();

        setDataFromBindViewHolder(itemModel, viewHolder, resources);

    }

    private void setDataFromBindViewHolder(ModelColumn modelColumn,
                                           RecyclerView.ViewHolder viewHolder, final Resources resources) {

        viewHolder.itemView.setEnabled(true);

        final ColumnViewHolder columnViewHolder = (ColumnViewHolder) viewHolder;

        final View itemView = columnViewHolder.itemView;

        try {
            columnViewHolder.nameTable.setText(modelColumn.getNameColumn());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            columnViewHolder.numTable.setText(String.valueOf(modelColumn.getNumColumn()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(modelColumn.getTypeOfInput() ==0)
            {
                columnViewHolder.inputTable.setText(typeOfInput.get(0));
            }
            else if(modelColumn.getTypeOfInput() ==1)
            {
                columnViewHolder.inputTable.setText(typeOfInput.get(1));
            }
            else if(modelColumn.getTypeOfInput() ==2)
            {
                columnViewHolder.inputTable.setText(typeOfInput.get(2));
            }
            else if(modelColumn.getTypeOfInput() ==3)
            {
                columnViewHolder.inputTable.setText(typeOfInput.get(3));
            }
            else if(modelColumn.getTypeOfInput() ==4)
            {
                columnViewHolder.inputTable.setText(typeOfInput.get(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        itemView.setVisibility(View.VISIBLE);

        colors[0] = resources.getColor(R.color.gray_50);
        colors[1] = resources.getColor(R.color.gray_60);

        itemView.setBackgroundColor(colors[nomerCveta]);
        if(nomerCveta==0)
        {
            nomerCveta=1;
        }
        else
        {
            nomerCveta=0;
        }

    }


    public ColumnRedactAdapter(ColumnRedactFragment columnRedactFragment) {

        this.columnRedactFragment = columnRedactFragment;

        if (columnRedactFragment.getActivity() != null) {
            activityM = (MainActivity) columnRedactFragment.getActivity();

        }

        itemColumnModel = new ArrayList<>();

        typeOfInput = new ArrayList<>();
        typeOfInput.add(activityM.getResources().getString(R.string.type_input_1));
        typeOfInput.add(activityM.getResources().getString(R.string.type_input_2));
        typeOfInput.add(activityM.getResources().getString(R.string.type_input_3));
        typeOfInput.add(activityM.getResources().getString(R.string.type_data_4));
        typeOfInput.add(activityM.getResources().getString(R.string.type_data_5));

        allAssociatedTable = new ArrayList<>();
        allAssociatedTable.addAll(dbHelperLLs.queryTables().getAllTablesSampleAssociated(nowOpenSample));

    }

    public final List<ModelColumn> getAdapter()
    {
        return itemColumnModel;
    }
    public final ModelColumn getItem(int position) {
        return itemColumnModel.get(position);
    }

    public final void addItem(ModelColumn item) {

        itemColumnModel.add(item);

        notifyItemInserted(getItemCount() - 1);
    }

    public final void updateColumn(ModelColumn modelColumn) {
        for (int i = 0; i < getItemCount(); i++) {

            ModelColumn task =  getItem(i);

            if (modelColumn.getTimeStamp()== task.getTimeStamp()) {

                removeItem(i);

                getColumnRedactFragment().addColumn(modelColumn, false);

            }

        }
    }


    public final void removeItem(int location) {

        if (location >= 0 && location <= getItemCount() -1) {

            itemColumnModel.remove(location);

            notifyItemRemoved(location);
        }

    }

    @Override
    public int getItemCount() {

        return itemColumnModel.size();
    }

    private class ColumnViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        TextView nameTable;
        TextView numTable;
        TextView dataTable;
        TextView inputTable;

        ColumnViewHolder(View itemView, TextView nameTable, TextView numTable, TextView inputTable, TextView dataTable) {
            super(itemView);

            this.nameTable = nameTable;
            this.numTable = numTable;
            this.dataTable = dataTable;
            this.inputTable = inputTable;

            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(R.string.select_action);
            myActionEditItem = menu.add(R.string.dialog_editing_column);
            myActionDuplicateItem = menu.add(R.string.duplicate_column);
            myActionDeleteItem = menu.add(R.string.dialog_deleting_column);

            myActionEditItem.setOnMenuItemClickListener(this);
            myActionDeleteItem.setOnMenuItemClickListener(this);
            myActionDuplicateItem.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem itemClient) {
            ModelColumn modelColumn = getItem(getLayoutPosition());
            if (itemClient == myActionEditItem) {

                if(allAssociatedTable.size()==0)
                {
                    getColumnRedactFragment().showColumnEditDialog(modelColumn);
                }
                else
                {
                    CDialog cDialog = new CDialog();
                    FragmentManager tempFM = activityM.getSupportFragmentManager();
                    cDialog.show(tempFM, "NotDel");
                }
            } else if (itemClient == myActionDuplicateItem) {

                getColumnRedactFragment().duplicateColumn(modelColumn);

            }else if (itemClient == myActionDeleteItem) {

                if(allAssociatedTable.size()==0)
                {
                    getColumnRedactFragment().removeColumnDialog(getLayoutPosition());
                }
                else
                {
                    CDialog cDialog = new CDialog();
                    FragmentManager tempFM = activityM.getSupportFragmentManager();
                    cDialog.show(tempFM, "NotDel");
                }
            }
            return true;
        }

    }

    private ColumnRedactFragment getColumnRedactFragment() {
        return columnRedactFragment;
    }

}