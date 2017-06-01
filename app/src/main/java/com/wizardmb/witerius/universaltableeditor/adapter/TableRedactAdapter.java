package com.wizardmb.witerius.universaltableeditor.adapter;

/**
 * Created by User on 20.03.2016.
 */

import android.content.res.Resources;
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
import com.wizardmb.witerius.universaltableeditor.database.DBHelperA;
import com.wizardmb.witerius.universaltableeditor.dialog.CDialog;
import com.wizardmb.witerius.universaltableeditor.fragment.TableImportFragment;
import com.wizardmb.witerius.universaltableeditor.fragment.TableRedactFragment;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenTable;


public final class TableRedactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MenuItem myActionEditItem;
   // private MenuItem myActionDuplicateItem;
    private MenuItem myActionDeleteItem;

    private List<ModelTable> modelTables;

    private TableRedactFragment tableRedactFragment;
    private TableImportFragment tableImportFragment;

    private int[] colors = new int[2];
    private int nomerCveta = 0;

    private boolean isRedact = true;
    private List<ModelSample> modelSamplesList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_sample_redact, viewGroup, false);
        TextView name = (TextView) v.findViewById(R.id.tvNameTable);
        TextView sample = (TextView) v.findViewById(R.id.tvSampleTable);
        return new SampleViewHolder(v, name, sample);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ModelTable itemModel = modelTables.get(position);
        final Resources resources = viewHolder.itemView.getResources();

        setDataFromBindViewHolder(itemModel, viewHolder, resources);

    }

    private void setDataFromBindViewHolder(final ModelTable modelTable,
                                           RecyclerView.ViewHolder viewHolder, final Resources resources) {

        viewHolder.itemView.setEnabled(true);

        final SampleViewHolder sampleViewHolder = (SampleViewHolder) viewHolder;

        final View itemView = sampleViewHolder.itemView;

        sampleViewHolder.nameTable.setText(modelTable.getNameTable());
        for(int i=0; i<modelSamplesList.size(); i++)
        {
            if(modelSamplesList.get(i).getTimeStamp() == modelTable.getTsSample())
            sampleViewHolder.sample.setText(modelSamplesList.get(i).getNameTable());
        }

        itemView.setVisibility(View.VISIBLE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowOpenTable = modelTable.getTimeStamp();
                nowOpenSample = modelTable.getTsSample();
                if(isRedact)
                {
                    tableRedactFragment.redactOfTable();
                }
                else
                {
                        tableImportFragment.permGet();

                }
            }
        });

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
    private void getSample()
    {
        modelSamplesList = new ArrayList<>();
        modelSamplesList = dbHelperLLs.querySampleTable().getAllSample(null, null, "data_name_sample");

    }

    public TableRedactAdapter(TableRedactFragment tableRedactFragment) {

        this.tableRedactFragment = tableRedactFragment;

        modelTables = new ArrayList<>();
        getSample();
        isRedact = true;
    }
    public TableRedactAdapter(TableImportFragment tableImportFragment) {

        this.tableImportFragment = tableImportFragment;

        modelTables = new ArrayList<>();
        getSample();
        isRedact = false;
    }

    public final ModelTable getItem(int position) {
        return modelTables.get(position);
    }

    public final void addItem(ModelTable item) {

        modelTables.add(item);

        notifyItemInserted(getItemCount() - 1);
    }

    public final void addItem(int location, ModelTable item) {

        modelTables.add(location, item);

        notifyItemInserted(location);
    }

    public final void updateTable(ModelTable modelTable) {
        for (int i = 0; i < getItemCount(); i++) {

            ModelTable task =  getItem(i);

            if (modelTable.getTimeStamp()== task.getTimeStamp()) {

                removeItem(i);

                getTableRedactFragment().addTable(modelTable, false);

            }

        }
    }


    public final void removeItem(int location) {

        if (location >= 0 && location <= getItemCount() -1) {

            modelTables.remove(location);

            notifyItemRemoved(location);
        }

    }


    @Override
    public int getItemCount() {

        return modelTables.size();
    }


    private class SampleViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        TextView nameTable, sample;

        SampleViewHolder(View itemView, TextView nameTable, TextView sample) {
            super(itemView);

            this.nameTable = nameTable;
            this.sample = sample;

            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(R.string.select_action);
            myActionEditItem = menu.add(R.string.dialog_editing_table);
          //  myActionDuplicateItem = menu.add(R.string.duplicate_table);
            myActionDeleteItem = menu.add(R.string.dialog_deleting_table);

            myActionEditItem.setOnMenuItemClickListener(this);
            myActionDeleteItem.setOnMenuItemClickListener(this);
         //   myActionDuplicateItem.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem itemClient) {
           if(isRedact) {
               ModelTable modelTable = getItem(getLayoutPosition());
               if (itemClient == myActionEditItem) {

                   getTableRedactFragment().showTableEditDialog(modelTable);

               }/* else if (itemClient == myActionDuplicateItem) {


            }*/ else if (itemClient == myActionDeleteItem) {

                   getTableRedactFragment().removeTableDialog(getLayoutPosition());

               }
           }
            return true;
        }

    }

    private TableRedactFragment getTableRedactFragment() {
        return tableRedactFragment;
    }

}