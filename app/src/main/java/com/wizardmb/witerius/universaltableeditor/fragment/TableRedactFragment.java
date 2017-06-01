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
import com.wizardmb.witerius.universaltableeditor.adapter.TableRedactAdapter;
import com.wizardmb.witerius.universaltableeditor.dialog.EditTableDialogFragment;
import com.wizardmb.witerius.universaltableeditor.iFace.CellRedact;
import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;


/**
 * A simple {@link Fragment} subclass.
 */
//созданна во втором. фрагмент из таба
public final class TableRedactFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TableRedactAdapter adapter;
    private int tempRemovingTableId = -1;

    private CellRedact cellRedact;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public TableRedactFragment() {
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
            cellRedact = (CellRedact) activity;
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

        adapter = new TableRedactAdapter(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        addTableFromDB();
        // Inflate the layout for this fragment
        return rootView;
    }
    public final void redactOfTable()
    {
        cellRedact.redactCell();
    }

    public final void addTable(ModelTable modelTable, boolean saveToDB) {
        int position = -1;
        int sizeA = adapter.getItemCount();
        for (int i = 0; i < sizeA; i++) {

            ModelTable task =  adapter.getItem(i);

            String str2 = task.getNameTable();
            String str1 = modelTable.getNameTable();
            int res=str1.compareTo(str2);
            if (res < 0) {
                position = i;
                break;
            }

        }
        if (position != -1) {
            adapter.addItem(position, modelTable);

        } else {
            adapter.addItem(modelTable);

        }

        if (saveToDB) {
            dbHelperLLs.saveTable(modelTable);
        }


    }

    private void addTableFromDB() {

        List<ModelTable> modelTables = new ArrayList<>();
        modelTables.addAll(dbHelperLLs.queryTables().getAllTables(null, null, "name_table"));

        int sizeC = modelTables.size();

        for (int i = 0; i < sizeC; i++) {

            addTable(modelTables.get(i), false);

        }

    }

    public final void removeAllAssociationData(int ts)
    {
       RemoveAssociateAT removeAssociateAT = new RemoveAssociateAT();
       removeAssociateAT.execute(ts);


    }
    final class RemoveAssociateAT extends AsyncTask<Integer, Void, Void>
    {
        @Override
        protected Void doInBackground(Integer... ts) {
           List<ModelCellData> associated = new ArrayList<>();
            int tt = ts[0];
            associated.addAll(dbHelperLLs.queryCell().getAllCellAssociated(tt));
            int sizeTemp = associated.size();
            for(int i =0; i<sizeTemp; i++)
            {
                dbHelperLLs.removeCellInBase(associated.get(i).getTimeStamp());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }


    public final void showTableEditDialog(ModelTable task) {  // показывает диалог редактир
        DialogFragment editingTableDialog = EditTableDialogFragment.newInstance(task);
        editingTableDialog.show(getActivity().getSupportFragmentManager(), "EditTableDialogFragment");
    }

    public final void updateTable(ModelTable task) {
        adapter.updateTable(task);
    }

    public final void removeTableDialog(final int location) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message_table);

        final ModelTable removingList = adapter.getItem(location); // удаление Списка
        tempRemovingTableId = removingList.getTimeStamp();

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
                        addTable(removingList, false);
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

                            dbHelperLLs.removeTableInBase(tempRemovingTableId);
                            removeAllAssociationData(tempRemovingTableId);
                            tempRemovingTableId = -1;
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