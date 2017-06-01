package com.wizardmb.witerius.universaltableeditor.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.adapter.CellAdapter;
import com.wizardmb.witerius.universaltableeditor.dialog.EditCellDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.InfoCellDialogFragment;
import com.wizardmb.witerius.universaltableeditor.dialog.PasteInCellListener;
import com.wizardmb.witerius.universaltableeditor.iFace.CellRedact;
import com.wizardmb.witerius.universaltableeditor.iFace.ClearedCell;
import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenTable;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.numOfEditedCell;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.positionInAdapter;


/**
 * Created by Witerius on 30.10.2016.
 */

public final class CellSFragment extends Fragment {

    private int rowOfMainCell = 1, columnOfMainCell = 7; //temp, изменяется автоматом из бд данных
    private int cellMainHeight = 100, cellLeftWidth = 130, cellTopHeight = 100; //ИЗМЕНЯЕМЫЕ ПАРАМЕТРЫ

    private MainActivity activity;
    private GridLayout gridLayout, gridLeft, gridTop;
    private CellAdapter adapter;

    private HorizontalScrollView emptyCV;

    private static int rowIndex = 0;
    private int columnIndex = 0;
    private static int indexOfGridL = 0, indexTop = 0, indexLeft = 0; //id of all item on page and Index

    private LayoutInflater inflater;
   // private RelativeLayout admobRL;

    private HorizontalScrollView topPanel, main_horizontal_scroll;
    private ScrollView leftPanel, ll_context;

    public static List<ModelColumn> allDataOfColumn;
    public List<Integer> positionOfVariantCheckBox;

    private List<Integer> numberOfFirstColumnCellPosition; //Номера ячеек в первой колонке
    private List<List<String>> listOfStringVariant;

    private MenuItem myActionEditItem;
    private MenuItem myActionDeleteItem;
    private MenuItem myActionCopyItem;
    private MenuItem myActionPastItem;
  //  private MenuItem myActionClearItem;

    private int indexOfClickedItem= -1;
   // private ClearedCell clearedCell;
    private PasteInCellListener pasteInCellListener;
    private CellRedact cellFragmentReload;

    private String bufferText = "";
    private String bufferTextRaw = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        indexOfGridL = 0;
        indexTop = 0;
        indexLeft = 0;
        rowIndex = 0;
        columnIndex = 0;
        indexOfGridL = 0;
      /*  try {
            clearedCell = (ClearedCell) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingCellListener");
        }*/
        try {
            cellFragmentReload = (CellRedact) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CellRedactListener");
        }

        try {
            pasteInCellListener = (PasteInCellListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PasteInCellListener");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater1, ViewGroup container,
                             Bundle savedInstanceState) {
        indexOfGridL = 0;
        indexTop = 0;
        indexLeft = 0;
        rowIndex = 0;
        columnIndex = 0;

        indexOfGridL = 0;
        positionOfVariantCheckBox = new ArrayList<>();
        numberOfFirstColumnCellPosition = new ArrayList<>();

        adapter = new CellAdapter();
        addDataFromDB(nowOpenSample, nowOpenTable);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }
        View rootView = inflater1.inflate(R.layout.fragment_start, container, false);
        inflater = inflater1;

        emptyCV = (HorizontalScrollView) rootView.findViewById(R.id.empty_cv);
        GridLayout emptyGL = new GridLayout(activity);
        emptyGL.setOrientation(GridLayout.HORIZONTAL);
        emptyCV.addView(emptyGL);

        CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);
        GridLayout.Spec row = GridLayout.spec(0, 1);
        GridLayout.Spec column = GridLayout.spec(0, 1);

        GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);
        gridLayoutParam.setMargins(1, 1, 1, 1);

        gridLayoutParam.height = cellTopHeight;
        gridLayoutParam.width = cellLeftWidth;

        emptyGL.addView(cardView_f, 0, gridLayoutParam);

        ll_context = (ScrollView) rootView.findViewById(R.id.ll_context);
        ll_context.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                leftPanel.scrollTo(main_horizontal_scroll.getScrollX(), ll_context.getScrollY());
            }
        });

        main_horizontal_scroll = (HorizontalScrollView) rootView.findViewById(R.id.main_horizontal_scroll);
        main_horizontal_scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                topPanel.scrollTo(main_horizontal_scroll.getScrollX(), ll_context.getScrollY());
            }
        });
        //  admobRL = (RelativeLayout) rootView.findViewById(R.id.admob_rl);

        topPanel = (HorizontalScrollView) rootView.findViewById(R.id.topPanel);
        leftPanel = (ScrollView) rootView.findViewById(R.id.leftPanel);

        gridLayout = new GridLayout(activity);
        gridLeft = new GridLayout(activity);
        gridTop = new GridLayout(activity);

        gridLayout.setOrientation(GridLayout.HORIZONTAL);
        gridLeft.setOrientation(GridLayout.VERTICAL);
        gridTop.setOrientation(GridLayout.HORIZONTAL);

        gridLayout.setColumnCount(columnOfMainCell);
        gridLayout.setRowCount(rowOfMainCell); //надо назначать

        gridTop.setColumnCount(columnOfMainCell);
        gridTop.setRowCount(1);

        gridLeft.setColumnCount(1);
        gridLeft.setRowCount(rowOfMainCell);

        ll_context.addView(gridLayout);
        topPanel.addView(gridTop);
        leftPanel.addView(gridLeft);

        setGridParametr();
        addDataInCell();

        return rootView;
    }


    final class SelectListener implements View.OnClickListener {
        int idOf =-1;

        public SelectListener(int indexOfGridL) {
            idOf = indexOfGridL;
        }

        @Override
        public void onClick(View view) {
            int position = getPositionInRow(view.getId());

            ModelCellData task = adapter.getItem(position);
            numOfEditedCell = getPositionInModel(idOf);

            DialogFragment infoCellDialogFragment = InfoCellDialogFragment.newInstance(task);
            infoCellDialogFragment.show(getActivity().getSupportFragmentManager(), "InfoCellDialogFragment");

        }
    }


   /* final class CreateAllAT extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

          LinearLayout admob = (LinearLayout) inflater.inflate(R.layout.admob_ll, null);

            NativeExpressAdView adView = (NativeExpressAdView) admob.findViewById(R.id.adView2);

            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("ca-app-pub-3867788656747346~5734215711")
                    .build();
            adView.loadAd(request);

            admobRL.addView(admob);
            //////////

            //  CreateNewGameAT createNewGameAT = new CreateNewGameAT();
            //   createNewGameAT.execute();


            //setChessManOnField();

        }
    }*/

    private void setGridParametr() {

        for (int i = 0; i < columnOfMainCell; i++) {

            CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);
            GridLayout.Spec row = GridLayout.spec(0, 1);
            GridLayout.Spec column = GridLayout.spec(indexTop, 1);

            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);
            gridLayoutParam.setMargins(1, 1, 1, 1);

            cardView_f.setId(indexTop);

            gridLayoutParam.height = cellTopHeight;
            gridLayoutParam.width = allDataOfColumn.get(i).getWidth();

            gridTop.addView(cardView_f, indexTop, gridLayoutParam);
            indexTop++;

        }
        for (int i = 0; i < rowOfMainCell; i++) {
            CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);
            GridLayout.Spec row = GridLayout.spec(indexLeft, 1);
            GridLayout.Spec column = GridLayout.spec(0, 1);

            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);
            gridLayoutParam.setMargins(1, 1, 1, 1);

            cardView_f.setId(indexLeft);

            gridLayoutParam.height = cellMainHeight;
            gridLayoutParam.width = cellLeftWidth;
            gridLeft.addView(cardView_f, indexLeft, gridLayoutParam);
            indexLeft++;

        }
        numberOfFirstColumnCellPosition.add(0);
        for (int i = 0; i < rowOfMainCell; i++) {

            for (int i1 = 0; i1 < columnOfMainCell; i1++) {

                CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);
                GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
                GridLayout.Spec column = GridLayout.spec(columnIndex, 1);

                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);
                gridLayoutParam.setMargins(1, 1, 1, 1);
                cardView_f.setId(indexOfGridL);
                cardView_f.setOnClickListener(new SelectListener(indexOfGridL));
                cardView_f.setOnCreateContextMenuListener(new ContextMenuListenerForCardView());

                gridLayoutParam.height = cellMainHeight;
                gridLayoutParam.width = allDataOfColumn.get(i1).getWidth();
                gridLayout.addView(cardView_f, indexOfGridL, gridLayoutParam);
                indexOfGridL++;

                columnIndex++;
            }
            numberOfFirstColumnCellPosition.add(indexOfGridL);
            columnIndex = 0;
            rowIndex++;

        }
    }

    private int getPositionInModel(int gridPosition) {
        int temp = -1;
        for (int i = 0; i < numberOfFirstColumnCellPosition.size(); i++) {
            if (gridPosition == numberOfFirstColumnCellPosition.get(i)) {
                temp = 0;
                  break;
            }
            else if (gridPosition < numberOfFirstColumnCellPosition.get(1)) {
                temp = gridPosition;
                  break;
            }
            else if (gridPosition > numberOfFirstColumnCellPosition.get(1) &&
                    numberOfFirstColumnCellPosition.get(i) > gridPosition) {
                int tempPosit = i-1;
                temp = gridPosition - numberOfFirstColumnCellPosition.get(tempPosit);
                 break;
            }

        }
        return temp;
    } private int getPositionInRow(int gridPosition) {
        int temp = -1;
        for (int i = 0; i < numberOfFirstColumnCellPosition.size(); i++) {

            if (gridPosition == numberOfFirstColumnCellPosition.get(i)) {
                temp = i;
                break;
            }
            else if (gridPosition < numberOfFirstColumnCellPosition.get(1)) {
                temp = 0;
                  break;
            }
            else if (gridPosition > numberOfFirstColumnCellPosition.get(1) &&
                    numberOfFirstColumnCellPosition.get(i) > gridPosition) {

                temp = i-1;
                  break;
            }

        }
        return temp;
    }

    public final void setTextInCell(int position, String data) {
        CardView startView = (CardView) gridLayout.getChildAt(position);
        TextView textView = (TextView) startView.findViewById(R.id.name_c);

        if(data.equals("~#&$~"))
        {
            textView.setText("");
        }
        else
        {
            textView.setText(data);
        }

    }

    public final void setTextLeft(int position, int i) {
        CardView startView = (CardView) gridLeft.getChildAt(position);
        TextView textView = (TextView) startView.findViewById(R.id.name_c);

        textView.setText(String.valueOf(i));

    }

    public final void setTextTop(int position, ModelColumn modelColumn) {
        CardView startView = (CardView) gridTop.getChildAt(position);
        TextView textView = (TextView) startView.findViewById(R.id.name_c);

        textView.setText(modelColumn.getNameColumn());

    }

    private void addDataFromDB(long id, int idNowOpenTable) {
        allDataOfColumn = new ArrayList<>();
        allDataOfColumn.addAll(dbHelperLLs.queryColumn().getAllData(id));

        Collections.sort(allDataOfColumn, new Comparator<ModelColumn>() {
            public int compare(ModelColumn o1, ModelColumn o2) {
                return o1.getNumColumn() - o2.getNumColumn();
            }
        });

        columnOfMainCell = allDataOfColumn.size();
        cellMainHeight = allDataOfColumn.get(0).getHeight();

        for (int i = 0; i < allDataOfColumn.size(); i++) {

          if (allDataOfColumn.get(i).getTypeOfInput() == 1 || allDataOfColumn.get(i).getTypeOfInput() == 2) {
                positionOfVariantCheckBox.add(i);
           }
        }
        List<ModelCellData> tempList = new ArrayList<>();

        tempList.addAll(dbHelperLLs.queryCell().getAllCellAssociated(idNowOpenTable));
        Collections.sort(tempList, new Comparator<ModelCellData>() {
            public int compare(ModelCellData o1, ModelCellData o2) {
                return (int) (o1.getTimeStamp() - o2.getTimeStamp());
            }
        });
        for (int i = 0; i < tempList.size(); i++) {
            adapter.addItem(tempList.get(i));
        }
        rowOfMainCell = adapter.getItemCount();

        listOfStringVariant = new ArrayList<>();

        for(int i=0; i<allDataOfColumn.size(); i++)
        {
            String tempData = allDataOfColumn.get(i).getVariant();
            try {
                if (!tempData.equals(null)) {
                    String[] temp = tempData.split("~##~");

                    List<String> listOfString = new ArrayList<>();
                    for(int i1=0; i1< temp.length; i1++)
                    {
                       listOfString.add(temp[i1]);
                    }
                    listOfStringVariant.add(listOfString);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addDataInCell() {
        int sizeC = allDataOfColumn.size();

        for (int i = 0; i < sizeC; i++) {

            setTextTop(i, allDataOfColumn.get(i));

        }
        for (int i = 0; i < adapter.getItemCount(); i++) {
            int temp = i + 1;
            setTextLeft(i, temp);

        }
        int positionInGrid = 0;

        boolean haveVariantCheckBox = positionOfVariantCheckBox.size() > 0;

        for (int i = 0; i < adapter.getItemCount(); i++) {

            String tempData = adapter.getItem(i).getData();
            String[] temp = tempData.split("~#@~");

            if(temp.length>0)
            {
                for (int i1 = 0; i1 < temp.length; i1++) {

                   if (haveVariantCheckBox) {
                        boolean isFound = false;
                        String variant = "";
                        for (int povc = 0; povc < positionOfVariantCheckBox.size(); povc++) {
                            if (i1 == positionOfVariantCheckBox.get(povc)) {
                                isFound = true;
                                String[] variantMassive = temp[i1].split("~@@#~");

                                for(int varM =0; varM< variantMassive.length; varM++)
                                {
                                    variant = variant +  listOfStringVariant.get(povc).get(Integer.valueOf(variantMassive[varM]))
                                            + "\n";
                                }

                                break;
                            }
                        }
                        if (isFound) {
                            setTextInCell(positionInGrid, variant);
                        } else {
                            setTextInCell(positionInGrid, temp[i1]);
                        }

                    }
                    else {
                        setTextInCell(positionInGrid, temp[i1]);
                    }
                    positionInGrid++;
                }
            }
            else
            {
                for(int ii=0; ii<allDataOfColumn.size(); ii++)
                {
                    setTextInCell(positionInGrid, "");
                    positionInGrid++;
                }

            }

        }
    }

    public final void addCell(ModelCellData modelCell, boolean saveToDB) {

        Collections.sort(adapter.getAdapter(), new Comparator<ModelCellData>() {
            public int compare(ModelCellData o1, ModelCellData o2) {
                return ((int) o2.getTimeStamp() - (int) o1.getTimeStamp());
            }
        });

        adapter.addItem(modelCell);

        rowOfMainCell = adapter.getItemCount();
        gridLayout.setRowCount(rowOfMainCell);
        gridLeft.setRowCount(rowOfMainCell);

        CardView cardView_f1 = (CardView) inflater.inflate(R.layout.item_card, null);
        GridLayout.Spec row1 = GridLayout.spec(indexLeft, 1);
        GridLayout.Spec column1 = GridLayout.spec(0, 1);

        GridLayout.LayoutParams gridLayoutParam1 = new GridLayout.LayoutParams(row1, column1);
        gridLayoutParam1.setMargins(1, 1, 1, 1);

        cardView_f1.setId(indexLeft);

        gridLayoutParam1.height = cellMainHeight;
        gridLayoutParam1.width = cellLeftWidth;
        gridLeft.addView(cardView_f1, indexLeft, gridLayoutParam1);
        indexLeft++;

        int tempIndexOfGrid = indexOfGridL;
        for (int i1 = 0; i1 < columnOfMainCell; i1++) {

            CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);
            GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
            GridLayout.Spec column = GridLayout.spec(columnIndex, 1);

            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);
            gridLayoutParam.setMargins(1, 1, 1, 1);

            cardView_f.setId(indexOfGridL);
            cardView_f.setOnClickListener(new SelectListener(indexOfGridL));
            cardView_f.setOnCreateContextMenuListener(new ContextMenuListenerForCardView());

            gridLayoutParam.height = cellMainHeight;
            gridLayoutParam.width = allDataOfColumn.get(i1).getWidth();
            gridLayout.addView(cardView_f, indexOfGridL, gridLayoutParam);
            indexOfGridL++;

            columnIndex++;
        }
        numberOfFirstColumnCellPosition.add(indexOfGridL);
        columnIndex = 0;
        rowIndex++;

        int temp1 = rowOfMainCell - 1;
        setTextLeft(temp1, rowOfMainCell);

        String tempData = modelCell.getData();
        String[] temp = tempData.split("~#@~");

        boolean haveVariantCheckBox = positionOfVariantCheckBox.size() > 0;

        for (int i1 = 0; i1 < temp.length; i1++) {
            if (haveVariantCheckBox) {
                boolean isFound = false;
                String variant = "";
                for (int povc = 0; povc < positionOfVariantCheckBox.size(); povc++) {
                    if (i1 == positionOfVariantCheckBox.get(povc)) {
                        isFound = true;
                        String[] variantMassive = temp[i1].split("~@@#~");

                        for(int varM =0; varM< variantMassive.length; varM++)
                        {
                            variant = variant +  listOfStringVariant.get(povc).get(Integer.valueOf(variantMassive[varM]))
                                    + "\n";
                        }

                        break;
                    }
                }
                if (isFound) {
                    setTextInCell(tempIndexOfGrid, variant);
                } else {
                    setTextInCell(tempIndexOfGrid, temp[i1]);
                }

            }
            else {
                setTextInCell(tempIndexOfGrid, temp[i1]);
            }
            tempIndexOfGrid++;
        }

        if (saveToDB) {
            dbHelperLLs.saveCell(modelCell);

        }

    }
    public final void updateCell(ModelCellData task, String data) {
        boolean haveVariantCheckBox = positionOfVariantCheckBox.size() > 0;

        boolean isFound = false;
        String variant = "";
        if (haveVariantCheckBox) {

            for (int povc = 0; povc < positionOfVariantCheckBox.size(); povc++) {
                if (numOfEditedCell == positionOfVariantCheckBox.get(povc)) {
                    isFound = true;
                    String[] variantMassive = data.split("~@@#~");

                    for(int varM =0; varM< variantMassive.length; varM++)
                    {
                        variant = variant +  listOfStringVariant.get(povc).get(Integer.valueOf(variantMassive[varM]))
                                + "\n";
                    }

                    break;
                }
            }

        }
        if (isFound) {
            setTextInCell(indexOfClickedItem,   variant);
        } else {
            setTextInCell(indexOfClickedItem,  data);
        }

        adapter.updateCell(task, positionInAdapter);

    }
   /* public final void updateCellClear(ModelCellData task) {

        adapter.updateCell(task, positionInAdapter);
    }*/


    private final class ContextMenuListenerForCardView implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            numOfEditedCell = getPositionInModel(indexOfClickedItem);
            positionInAdapter = getPositionInRow(indexOfClickedItem);

            ModelCellData modelDataValue = adapter.getItem(positionInAdapter);

            if (item == myActionEditItem) {


                showColumnEditDialog(modelDataValue);

            } /*else if (item == myActionClearItem) {

                removeCellDialog(positionInAdapter, true);

            }*/
            else if (item == myActionDeleteItem) {

                removeCellDialog(positionInAdapter/*, false*/);

            } else if (item == myActionCopyItem) {

                copyItem(modelDataValue);

            }else if (item == myActionPastItem) {
                pasteItem(modelDataValue);

            }
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                indexOfClickedItem = v.getId();

                menu.setHeaderTitle(R.string.select_action);

                    myActionEditItem = menu.add(R.string.dialog_editing_data);
                    myActionCopyItem = menu.add(R.string.copy_data);
                    myActionPastItem = menu.add(R.string.past_data);
                  //  myActionClearItem = menu.add(R.string.clear_data);
                    myActionDeleteItem = menu.add(R.string.dialog_deleting_data);

                    myActionCopyItem.setOnMenuItemClickListener(this);
                    myActionPastItem.setOnMenuItemClickListener(this);
                    //myActionClearItem.setOnMenuItemClickListener(this);
                    myActionEditItem.setOnMenuItemClickListener(this);
                    myActionDeleteItem.setOnMenuItemClickListener(this);

        }
    }


    private void copyItem(ModelCellData modelCellData) {

        String[] tempA = modelCellData.getData().split("~#@~");

        for (int i = 0; i < tempA.length; i++) {
            if(i == numOfEditedCell) {
                bufferText = tempA[i];

                CardView startView = (CardView) gridLayout.getChildAt(indexOfClickedItem);
                TextView textView = (TextView) startView.findViewById(R.id.name_c);
                bufferTextRaw = textView.getText().toString();

                break;
            }
        }

    }
    private void pasteItem(ModelCellData modelCellData) {

       String[] tempA = modelCellData.getData().split("~#@~");
       String tempData = "";
        for (int i = 0; i < tempA.length; i++) {
            if(i == numOfEditedCell) {
                if(allDataOfColumn.get(numOfEditedCell).getTypeOfInput() == 1
                || allDataOfColumn.get(numOfEditedCell).getTypeOfInput() == 2 )
                {
                  //  tempA[i] = bufferText;
                    Toast.makeText(activity, R.string.data_past_impossible, Toast.LENGTH_LONG).show();
                }
                else
                {
                    tempA[i] = bufferTextRaw;
                }

            }
            if (i == 0) {
                tempData = "" + tempA[i];
            } else {
                tempData = tempData + "~#@~" + tempA[i];
            }
        }

        try {
            if(!bufferText.equals("") && !bufferTextRaw.equals(""))
            {
               /* if(allDataOfColumn.get(numOfEditedCell).getTypeOfInput() == 1
                        || allDataOfColumn.get(numOfEditedCell).getTypeOfInput() == 2 )
                {
                    pasteInCellListener.inCellPasted(modelCellData, bufferText);
                }
                else*/
               if(allDataOfColumn.get(numOfEditedCell).getTypeOfInput() == 0)
                {
                    pasteInCellListener.inCellPasted(modelCellData, bufferTextRaw);
                    modelCellData.setData(tempData);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public final void showColumnEditDialog(ModelCellData task) {
        DialogFragment editCellDialogFragment = EditCellDialogFragment.newInstance(task);
        editCellDialogFragment.show(getActivity().getSupportFragmentManager(), "EditCellDialogFragment");
    }

    public final void removeCellDialog(final int location/*, final boolean isClear*/) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message_data);

        ModelCellData removingList = adapter.getItem(location);

        final long timeStamp = removingList.getTimeStamp();

        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
/*
                    if(isClear)
                    {
                        String [] tempA = adapter.getItem(location).getData().split("~#@~");
                        String nullString = "";

                        for(int i=0; i<tempA.length;i++)
                        {
                            if (i == 0) {
                                nullString = "~#&$~";
                            } else {
                                nullString = nullString + "~#@~~#&$~";
                            }
                        }
                        adapter.getItem(location).setData(nullString);

                       clearedCell.onCellCleared(adapter.getItem(location));

                        int startColumnPosition = getFirstPositionInGrid(location); //positionInAdapter

                        for(int i=0; i<allDataOfColumn.size(); i++)
                        {
                            setTextInCell(startColumnPosition, "");
                            startColumnPosition++;
                        }

                    }

                    else
                    {*/
                        MainActivity.dbHelperLLs.removeCellInBase(timeStamp);  // удаление
                        cellFragmentReload.redactCell();
                  //  }


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
