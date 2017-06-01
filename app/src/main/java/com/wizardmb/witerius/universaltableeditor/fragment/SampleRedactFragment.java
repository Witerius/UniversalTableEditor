package com.wizardmb.witerius.universaltableeditor.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.adapter.SampleRedactAdapter;
import com.wizardmb.witerius.universaltableeditor.dialog.AddingColumnDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.DuplicateColumnInt;
import com.wizardmb.witerius.universaltableeditor.dialog.EditSampleDialogFragment;
import com.wizardmb.witerius.universaltableeditor.iFace.ColumnRedact;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;
import com.wizardmb.witerius.universaltableeditor.model.ModelSample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;


/**
 * A simple {@link Fragment} subclass.
 */
//созданна во втором. фрагмент из таба
public final class SampleRedactFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private SampleRedactAdapter adapter;
    private long tempRemovingSampleId = -1;

    private ColumnRedact columnRedactInt;
    private DuplicateColumnInt duplicateColumnInt;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public SampleRedactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      /*  if(MainActivity.isFirstStart_s && MainActivity.saveNumHelp_s == 1) {
            ADialog aDialog = new ADialog();
            aDialog.show(MainActivity.fragmentManager, "Help1");
        }*/
        try {
            columnRedactInt = (ColumnRedact) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            duplicateColumnInt = (DuplicateColumnInt) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_sample_redact, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvSampleRedact);

        int wrapContent = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams lParams1 = new RelativeLayout.LayoutParams(
                wrapContent, wrapContent);

        lParams1.setMargins(0, 0, 0, 50);
        recyclerView.setLayoutParams(lParams1);

        adapter = new SampleRedactAdapter(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        addSampleFromDB();
        // Inflate the layout for this fragment
        return rootView;
    }
    public final void redactOfColumn()
    {
        columnRedactInt.redactColumn();
    }

    public final void addSample(ModelSample modelSample, boolean saveToDB) {
        int position = -1;
        int sizeA = adapter.getItemCount();
        for (int i = 0; i < sizeA; i++) {

            ModelSample task =  adapter.getItem(i);

            String str2 = task.getNameTable();
            String str1 = modelSample.getNameTable();
            int res=str1.compareTo(str2);

            if (res < 0) {
                position = i;
                break;
            }

        }
        if (position != -1) {
            adapter.addItem(position, modelSample);

        } else {
            adapter.addItem(modelSample);

        }

        if (saveToDB) {
            dbHelperLLs.saveSampleTable(modelSample);
        }


    }

    private void addSampleFromDB() {

        List<ModelSample> modelSamples = new ArrayList<>();
        modelSamples.addAll(dbHelperLLs.querySampleTable().getAllSample(null, null, "data_name_sample"));

        int sizeC = modelSamples.size();

        for (int i = 0; i < sizeC; i++) {

            addSample(modelSamples.get(i), false);

        }

    }

    public final void removeAllAssociationData(Long ts)
    {
       RemoveAssociateAT removeAssociateAT = new RemoveAssociateAT();
       removeAssociateAT.execute(ts);


    }
    final class RemoveAssociateAT extends AsyncTask<Long, Void, Void>
    {
        @Override
        protected Void doInBackground(Long... ts) {
            List<ModelColumn> associated = new ArrayList<>();
            Long tt = ts[0];
            associated.addAll(dbHelperLLs.queryColumn().getAllColumnSampleAssociated(tt));
            int sizeTemp = associated.size();
            for(int i =0; i<sizeTemp; i++)
            {
                dbHelperLLs.removeColumnInBase(associated.get(i).getTimeStamp());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
    public final void duplicateSample(ModelSample modelSample)
    {
        ModelSample newModelSample = new ModelSample();

        newModelSample.setNameTable(modelSample.getNameTable()+1);

        addSample(newModelSample, true);
        DuplicateColumnStartAT duplicateColumnStartAT = new DuplicateColumnStartAT();
        duplicateColumnStartAT.execute(modelSample, newModelSample);

        showSampleEditDialog(newModelSample);

    }
    final class DuplicateColumnStartAT extends AsyncTask<ModelSample, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(ModelSample... params) {
            List<ModelColumn> duplicateColumn = new ArrayList<>();
            duplicateColumn.addAll(dbHelperLLs.queryColumn().getAllData(params[0].getTimeStamp()));
            for(int i=0; i<duplicateColumn.size(); i++)
            {
                duplicateColumn.get(i).setTimeStamp(new Date().getTime());
                duplicateColumn.get(i).setTsOfTable(params[1].getTimeStamp());
                duplicateColumnInt.onColumnDuplicate(duplicateColumn.get(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
    public final void showSampleEditDialog(ModelSample task) {  // показывает диалог редактир
        DialogFragment editSampleDialogFragment = EditSampleDialogFragment.newInstance(task);
        editSampleDialogFragment.show(getActivity().getSupportFragmentManager(), "EditSampleDialogFragment");
    }

    public final void updateSample(ModelSample task) {
        adapter.updateSample(task);
    }

    public final void removeColumnDialog(final int location) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message_sample);

        final ModelSample removingList = adapter.getItem(location); // удаление Списка
        tempRemovingSampleId = removingList.getTimeStamp();

        final boolean[] isRemoved = {false};
        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                adapter.removeItem(location);

                isRemoved[0] = true;
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                        R.string.removed, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSample(removingList, false);
                        isRemoved[0] = false;
                    }
                });
                snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {

                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        if (isRemoved[0]) {

                            dbHelperLLs.removeSampleTableInBase(tempRemovingSampleId);
                            removeAllAssociationData(tempRemovingSampleId);
                            tempRemovingSampleId = -1;

                        }
                    }
                });
                snackbar.show();
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        dialogBuilder.show();
    }

}