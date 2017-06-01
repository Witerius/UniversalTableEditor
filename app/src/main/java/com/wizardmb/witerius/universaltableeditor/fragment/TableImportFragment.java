package com.wizardmb.witerius.universaltableeditor.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wizardmb.witerius.universaltableeditor.MainActivity;
import com.wizardmb.witerius.universaltableeditor.R;
import com.wizardmb.witerius.universaltableeditor.adapter.TableRedactAdapter;
import com.wizardmb.witerius.universaltableeditor.model.ModelCellData;
import com.wizardmb.witerius.universaltableeditor.model.ModelColumn;
import com.wizardmb.witerius.universaltableeditor.model.ModelTable;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.wizardmb.witerius.universaltableeditor.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenSample;
import static com.wizardmb.witerius.universaltableeditor.MainActivity.nowOpenTable;


/**
 * A simple {@link Fragment} subclass.
 */

public final class TableImportFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TableRedactAdapter adapter;

    final String DIRECTORY_PATH = "UniversalTE";

    final String EXCEL_XLS = "UniversalTE";
    final String EXTEND_XLS = ".xls";

    private List<ModelColumn> allDataOfColumnTI;
    public List<Integer> positionOfVariantCheckBox;
    private List<List<String>> listOfStringVariant;

    private List<ModelCellData> modelCellDatas;
    private static final int PERMISSION_REQUEST_CODE = 123;
    MainActivity activityM;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public TableImportFragment() {
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
            activityM = (MainActivity) activity;
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

    public final void addTable(ModelTable modelTable) {
        int position = -1;
        int sizeA = adapter.getItemCount();
        for (int i = 0; i < sizeA; i++) {

            ModelTable task = adapter.getItem(i);

            String str2 = task.getNameTable();
            String str1 = modelTable.getNameTable();
            int res = str1.compareTo(str2);
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

    }

    private void addTableFromDB() {

        List<ModelTable> modelTables = new ArrayList<>();
        modelTables.addAll(dbHelperLLs.queryTables().getAllTables(null, null, "name_table"));

        int sizeC = modelTables.size();

        for (int i = 0; i < sizeC; i++) {

            addTable(modelTables.get(i));

        }

    }

    private FileOutputStream outputStream;

    final void writeFile() {
        try {
            File f;
            File folderC;
            boolean success = true;

            String extend = EXTEND_XLS;
            String fileNameIs = EXCEL_XLS;
            String path;

            try {

                try {
                    path = Environment.getExternalStorageDirectory() +
                            File.separator + DIRECTORY_PATH;
                    folderC = new File(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        folderC = new File("/storage/extSdCard/UniversalTE");
                        path = "/storage/extSdCard/UniversalTE";
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        try {
                            folderC = new File("/storage/emulated/0/UniversalTE");
                            path = "/storage/emulated/0/UniversalTE";

                        } catch (Exception e2) {
                            e2.printStackTrace();

                            folderC = new File("/storage/sdcard/UniversalTE");
                            path = "/storage/sdcard/UniversalTE";
                        }
                    }
                }


                if (!folderC.exists()) {
                    success = folderC.mkdir();
                }
                if (success) {
                    f = new File(path +
                            File.separator + fileNameIs + extend);
                    if (f.exists()) {
                        int i = 0;
                        while (f.exists()) {
                            i += 1;
                            f = new File(path +
                                    File.separator + fileNameIs + i + extend);
                        }
                    }
                    outputStream = new FileOutputStream(f);
                } else {
                    Toast.makeText(getActivity(), R.string.insert_sd_card, Toast.LENGTH_LONG).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.file_not_found, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.file_write_failed, Toast.LENGTH_LONG).show();
        }
    }


    public final void saveExcelFile() {
        addDataFromDB(nowOpenSample, nowOpenTable); // в адаптере сделать, чтоб вводилось
        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        writeFile();
        sheet1 = wb.createSheet("myOrder");
        int sizeC = allDataOfColumnTI.size();

        boolean haveVariantCheckBox = false;
        try {
            haveVariantCheckBox = positionOfVariantCheckBox.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < modelCellDatas.size(); i++) {

            // Generate column headings
            Row row = sheet1.createRow(i);

            String tempData = modelCellDatas.get(i).getData();
            String[] temp = tempData.split("~#@~");

            if (i == 0) {

                for (int i1 = 0; i1 < sizeC; i1++) {
                    c = row.createCell(i1);
                    c.setCellValue(allDataOfColumnTI.get(i1).getNameColumn());
                    c.setCellStyle(cs);
                }
            } else {
                if (temp.length > 0) {
                    for (int i1 = 0; i1 < temp.length; i1++) {

                        if (haveVariantCheckBox) {
                            boolean isFound = false;
                            String variant = "";
                            for (int povc = 0; povc < positionOfVariantCheckBox.size(); povc++) {
                                if (i1 == positionOfVariantCheckBox.get(povc)) {
                                    isFound = true;
                                    String[] variantMassive = temp[i1].split("~@@#~");

                                    for (int varM = 0; varM < variantMassive.length; varM++) {
                                        variant = variant + listOfStringVariant.get(povc).get(Integer.valueOf(variantMassive[varM]))
                                                + "\n";
                                    }

                                    break;
                                }
                            }
                            if (isFound) {
                                c = row.createCell(i1);
                                c.setCellValue(variant);
                                c.setCellStyle(cs);
                            } else {
                                c = row.createCell(i1);
                                c.setCellValue(temp[i1]);
                                c.setCellStyle(cs);
                            }

                        } else {
                            c = row.createCell(i1);
                            c.setCellValue(temp[i1]);
                            c.setCellStyle(cs);
                        }

                    }
                }
            }
        }
        for (int i = 0; i < modelCellDatas.size(); i++) {
            sheet1.setColumnWidth(i, (15 * 500));
        }

        try {

            wb.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e1) {

        } finally {
            try {
                if (null != outputStream)
                    outputStream.close();
                Toast.makeText(getActivity(), R.string.success_export, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(getActivity(), R.string.export_filed, Toast.LENGTH_LONG).show();
            }
        }

    }


    private void addDataFromDB(long id, int idNowOpenTable) {
        allDataOfColumnTI = new ArrayList<>();
        allDataOfColumnTI.addAll(dbHelperLLs.queryColumn().getAllData(id));
        positionOfVariantCheckBox = new ArrayList<>();
        Collections.sort(allDataOfColumnTI, new Comparator<ModelColumn>() {
            public int compare(ModelColumn o1, ModelColumn o2) {
                return o1.getNumColumn() - o2.getNumColumn();
            }
        });

        for (int i = 0; i < allDataOfColumnTI.size(); i++) {

            if (allDataOfColumnTI.get(i).getTypeOfInput() == 1 || allDataOfColumnTI.get(i).getTypeOfInput() == 2) {
                positionOfVariantCheckBox.add(i);
            }
        }
        modelCellDatas = new ArrayList<>();

        modelCellDatas.addAll(dbHelperLLs.queryCell().getAllCellAssociated(idNowOpenTable));
        Collections.sort(modelCellDatas, new Comparator<ModelCellData>() {
            public int compare(ModelCellData o1, ModelCellData o2) {
                return (int) (o1.getTimeStamp() - o2.getTimeStamp());
            }
        });

        listOfStringVariant = new ArrayList<>();

        for (int i = 0; i < allDataOfColumnTI.size(); i++) {
            String tempData = allDataOfColumnTI.get(i).getVariant();
            try {
//                if (!tempData.equals(null)) {
                    String[] temp = tempData.split("~##~");

                    List<String> listOfString = new ArrayList<>();
                    for (int i1 = 0; i1 < temp.length; i1++) {
                        listOfString.add(temp[i1]);
                    }
                    listOfStringVariant.add(listOfString);
           //     }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public final void permGet()
    {
        if (hasPermissions()){
            // our app has permissions.
            saveExcelFile();
        }
        else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale();
        }
    }
    private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE };

        for (String perms : permissions){
            res = activityM.checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            saveExcelFile();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getActivity(), R.string.denied_storage, Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }

    }



    public final void showNoStoragePermissionSnackbar() {
        Snackbar.make(getActivity().findViewById(R.id.coordinator), R.string.storage_isnt_granted , Snackbar.LENGTH_LONG)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getActivity().getApplicationContext(),
                                R.string.grant_permiss,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    public final void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            saveExcelFile();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public final void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            final String message = getString(R.string.show_files_count);
            Snackbar.make(getActivity().findViewById(R.id.coordinator), message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.grant, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPerms();
                        }
                    })
                    .show();
        } else {
            requestPerms();
        }
    }

}