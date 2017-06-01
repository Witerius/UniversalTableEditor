package com.wizardmb.witerius.universaltableeditor.adapter;

/**
 * Created by User on 20.03.2016.
 */

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.dialog.CDialog;
import com.wizardmb.witerius.universaltableeditor.fragment.SampleRedactFragment;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;


public final class SampleRedactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MenuItem myActionEditItem;
    private MenuItem myActionDuplicateItem;
    private MenuItem myActionDeleteItem;

    private List<ModelSample> itemSampleModel;

    private SampleRedactFragment sampleRedactFragment;

    private int[] colors = new int[2];
    private int nomerCveta = 0;
    private MainActivity activityM;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_sample_redact, viewGroup, false);
        TextView name = (TextView) v.findViewById(R.id.tvNameTable);
        return new SampleViewHolder(v, name);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ModelSample itemModel = itemSampleModel.get(position);
        final Resources resources = viewHolder.itemView.getResources();

        setDataFromBindViewHolder(itemModel, viewHolder, resources);

    }

    private void setDataFromBindViewHolder(final ModelSample modelSample,
                                           RecyclerView.ViewHolder viewHolder, final Resources resources) {

        viewHolder.itemView.setEnabled(true);

        final SampleViewHolder sampleViewHolder = (SampleViewHolder) viewHolder;

        final View itemView = sampleViewHolder.itemView;

        sampleViewHolder.nameTable.setText(modelSample.getNameTable());

        itemView.setVisibility(View.VISIBLE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowOpenSample = modelSample.getTimeStamp();
                sampleRedactFragment.redactOfColumn();
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


    public SampleRedactAdapter(SampleRedactFragment sampleRedactFragment) {

        this.sampleRedactFragment = sampleRedactFragment;

        itemSampleModel = new ArrayList<>();
        if (sampleRedactFragment.getActivity() != null) {
            activityM = (MainActivity) sampleRedactFragment.getActivity();

        }

    }

    public final ModelSample getItem(int position) {
        return itemSampleModel.get(position);
    }

    public final void addItem(ModelSample item) {

        itemSampleModel.add(item);

        notifyItemInserted(getItemCount() - 1);
    }

    public final void addItem(int location, ModelSample item) {

        itemSampleModel.add(location, item);

        notifyItemInserted(location);
    }

    public final void updateSample(ModelSample modelSample) {
        for (int i = 0; i < getItemCount(); i++) {

            ModelSample task =  getItem(i);

            if (modelSample.getTimeStamp()== task.getTimeStamp()) {

                removeItem(i);

                getSampleRedactFragment().addSample(modelSample, false);

            }

        }
    }


    public final void removeItem(int location) {

        if (location >= 0 && location <= getItemCount() -1) {

            itemSampleModel.remove(location);

            notifyItemRemoved(location);
        }

    }

    @Override
    public int getItemCount() {

        return itemSampleModel.size();
    }

    private class SampleViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        TextView nameTable;

        SampleViewHolder(View itemView, TextView nameTable) {
            super(itemView);

            this.nameTable = nameTable;

            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(R.string.select_action);
            myActionEditItem = menu.add(R.string.dialog_editing_sample);
            myActionDuplicateItem = menu.add(R.string.duplicate_sample);
            myActionDeleteItem = menu.add(R.string.dialog_deleting_sample);

            myActionEditItem.setOnMenuItemClickListener(this);
            myActionDeleteItem.setOnMenuItemClickListener(this);
            myActionDuplicateItem.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem itemClient) {
            ModelSample modelSample = getItem(getLayoutPosition());
            if (itemClient == myActionEditItem) {

                List<ModelTable>  allAssociatedTable = new ArrayList<>();
                allAssociatedTable.addAll(dbHelperLLs.queryTables().getAllTablesSampleAssociated(modelSample.getTimeStamp()));

                if(allAssociatedTable.size() ==0)
                {
                    getSampleRedactFragment().showSampleEditDialog(modelSample);

                }
                else
                {
                    CDialog cDialog = new CDialog();
                    FragmentManager tempFM = activityM.getSupportFragmentManager();
                    cDialog.show(tempFM, "NotDel");
                }
            } else if (itemClient == myActionDuplicateItem) {

               getSampleRedactFragment().duplicateSample(modelSample);

            }else if (itemClient == myActionDeleteItem) {

                List<ModelTable>  allAssociatedTable = new ArrayList<>();
                allAssociatedTable.addAll(dbHelperLLs.queryTables().getAllTablesSampleAssociated(modelSample.getTimeStamp()));

                if(allAssociatedTable.size() ==0)
                {
                    getSampleRedactFragment().removeColumnDialog(getLayoutPosition());

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

    private SampleRedactFragment getSampleRedactFragment() {
        return sampleRedactFragment;
    }

}