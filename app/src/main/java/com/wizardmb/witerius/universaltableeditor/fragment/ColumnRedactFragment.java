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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.adapter.ColumnRedactAdapter;
import com.wizardmb.witerius.universaltableeditor.dialog.EditColumnDialogFragment;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.isNumChanged;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.numOfColumnBeforeEditing;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.numOfColumnOpenTable;


/**
 * A simple {@link Fragment} subclass.
 */
//созданна во втором. фрагмент из таба
public final class ColumnRedactFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static int heightColumn;
    private ColumnRedactAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public ColumnRedactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      /*  if(MainActivity.isFirstStart_s && MainActivity.saveNumHelp_s == 1) {
            ADialog aDialog = new ADialog();
            aDialog.show(MainActivity.fragmentManager, "Help1");
        }*/
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
        MainActivity.isHeightChanged = false;

        adapter = new ColumnRedactAdapter(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        try {
            addColumnFromDB(nowOpenSample);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            numOfColumnOpenTable = adapter.getItemCount();
        } catch (Exception e) {
            e.printStackTrace();
            numOfColumnOpenTable = 0;
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public final void addColumn(ModelColumn modelColumn, boolean saveToDB) {

        int size = adapter.getItemCount() - 1;
        if ( MainActivity.isHeightChanged && isNumChanged)
        {
            MainActivity.isHeightChanged = false;
            isNumChanged = false;

            for (int i = size; i >= 0; i--) {
                adapter.getItem(i).setHeight(modelColumn.getHeight());

                if (adapter.getItem(i).getNumColumn() >= modelColumn.getNumColumn()) {
                    if (!saveToDB && numOfColumnBeforeEditing >= adapter.getItem(i).getNumColumn() ||
                            saveToDB) {
                        int temp = adapter.getItem(i).getNumColumn() + 1;
                        adapter.getItem(i).setNumColumn(temp);

                    }

                }
                updateColumn(adapter.getItem(i));
                dbHelperLLs.updateColumn().refreshColumnMethod(adapter.getItem(i));

            }

        }

        else if ( MainActivity.isHeightChanged) {
            MainActivity.isHeightChanged = false;
            for (int i = size; i >= 0; i--) {
                adapter.getItem(i).setHeight(modelColumn.getHeight());

                updateColumn(adapter.getItem(i));
                dbHelperLLs.updateColumn().refreshColumnMethod(adapter.getItem(i));

            }

        }
        else if (isNumChanged) {
            isNumChanged = false;
            for (int i = size; i >= 0; i--) {
                if (adapter.getItem(i).getNumColumn() >= modelColumn.getNumColumn()) {
                    if (!saveToDB && numOfColumnBeforeEditing >= adapter.getItem(i).getNumColumn() ||
                            saveToDB) {
                        int temp = adapter.getItem(i).getNumColumn() + 1;
                        adapter.getItem(i).setNumColumn(temp);

                        updateColumn(adapter.getItem(i));
                        dbHelperLLs.updateColumn().refreshColumnMethod(adapter.getItem(i));
                    }

                }
            }

        }


        adapter.addItem(modelColumn);

        Collections.sort(adapter.getAdapter(), compareByNum);
        if (saveToDB) {
            dbHelperLLs.saveColumn(modelColumn);

        }

        adapter.notifyDataSetChanged();
    }

    private void changeNumIfDelete(int position)
    {
        for(int i=0; i<adapter.getItemCount();i++)
        {
            if(adapter.getItem(i).getNumColumn()> position)
            {
                int temp = adapter.getItem(i).getNumColumn()-1;
                adapter.getItem(i).setNumColumn(temp);

                updateColumn(adapter.getItem(i));
                dbHelperLLs.updateColumn().refreshColumnMethod(adapter.getItem(i));
            }
        }
       Collections.sort(adapter.getAdapter(), compareByNum);
       adapter.notifyDataSetChanged();
    }

    private Comparator<ModelColumn> compareByNum = new Comparator<ModelColumn>() {
        @Override
        public int compare(ModelColumn lhs, ModelColumn rhs) {
            return lhs.getNumColumn() - rhs.getNumColumn();
        }
    };

    private void addColumnFromDB(long tsTable) {

        List<ModelColumn> modelColumns = new ArrayList<>();
        modelColumns.addAll(dbHelperLLs.queryColumn().getAllData(tsTable));

        try {
            heightColumn = modelColumns.get(0).getHeight();
        } catch (Exception e) {
            e.printStackTrace();
            heightColumn = 100;
        }
        int sizeC = modelColumns.size();
        if (modelColumns.size() > 0) {
            for (int i = 0; i < sizeC; i++) {

                addColumn(modelColumns.get(i), false);

            }

        }

    }

    public final void getColumnCount() {
        try {
            numOfColumnOpenTable = adapter.getItemCount();

        } catch (Exception e) {
            e.printStackTrace();
            numOfColumnOpenTable = 0;
        }

    }
    public final void duplicateColumn(ModelColumn modelColumn)
    {
        ModelColumn newModelColumn = new ModelColumn();

        int num = adapter.getItemCount()+1;
        newModelColumn.setNumColumn(num);
        newModelColumn.setHeight(modelColumn.getHeight());
        newModelColumn.setWidth(modelColumn.getWidth());

        newModelColumn.setVariant(modelColumn.getVariant());
        newModelColumn.setNameColumn(modelColumn.getNameColumn());
        newModelColumn.setTsOfTable(modelColumn.getTsOfTable());
        newModelColumn.setTypeOfInput(modelColumn.getTypeOfInput());

        addColumn(newModelColumn, true);
        numOfColumnOpenTable++;
        showColumnEditDialog(newModelColumn);
    }

    public final void showColumnEditDialog(ModelColumn task) {  // показывает диалог редактир
        DialogFragment editingColumnDialogFragment = EditColumnDialogFragment.newInstance(task);
        editingColumnDialogFragment.show(getActivity().getSupportFragmentManager(), "EditColumnDialogFragment");
    }

    public final void updateColumn(ModelColumn task) {
        adapter.updateColumn(task);
    }

    public final void removeColumnDialog(final int location) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message_column);

        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MainActivity.dbHelperLLs.removeColumnInBase(adapter.getItem(location).getTimeStamp());
                adapter.removeItem(location);
                changeNumIfDelete(location);
                //ДОБАВИТЬ ОТМЕНУ УДАЛЕНИЯ??
                Toast.makeText(getActivity(), R.string.removed, Toast.LENGTH_SHORT).show();
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