package com.example.Pharmacy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Pharmacy.support.ConvertStringToDate;
import com.xu.database.XCursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordCardFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ID";
    private DataManager dataManager;
    private View recordCard;
    private int ID = 0;
    private List<String> nameSave = new ArrayList<>();
    private List<SearchResultFragment> searchResultFragments = new ArrayList<>();

    public RecordCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RecordCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordCardFragment newInstance(String param1) {
        RecordCardFragment fragment = new RecordCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mID = getArguments().getString(ARG_PARAM1);
            assert mID != null;
            setID(Integer.valueOf(mID));
        }

        dataManager = MainActivity.dataManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recordCard = inflater.inflate(R.layout.fr_record_card, container, false);
        setHasOptionsMenu(true);

        Button save = recordCard.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(recordCard);
            }
        });
        save.setFocusable(true);
        save.setFocusableInTouchMode(true);///add this line

        final TextView calendar = recordCard.findViewById(R.id.edit_date);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionShowDatePicker(v);
            }
        });

        final AutoCompleteTextView name = recordCard.findViewById(R.id.edit_name);
        loadProductName(name);
        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XCursor res = dataManager.getProductData(parent.getItemAtPosition(position).toString());
                loadSpinnerData(res);
                loadTotal(res);
            }
        });

        EditText editReal = recordCard.findViewById(R.id.edit_real);
        editReal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadDifference();
            }

            @Override
            public void afterTextChanged(Editable s) {
                loadDifference();
            }
        });
        editReal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    showDatePicker(calendar);
                }
                return false;
            }
        });

        if (getID() != 0) {
            loadData(recordCard);
        }

        EditText editBarCode = recordCard.findViewById(R.id.edit_barcode);
        editBarCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        loadBarCode(name);
                    }
                });
            }
        });

        MainActivity.hideProgressBar(getActivity());
        return recordCard;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        android.support.v7.app.ActionBar actionBar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            actionBar = ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("Số phiếu: " + getID());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.show();

            MenuItem menuItem_date = menu.findItem(R.id.menuitem_date);
            menuItem_date.setVisible(false);

            MenuItem menuItem_barcode = menu.findItem(R.id.menuitem_barcode);
            menuItem_barcode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MainActivity.showProgressBar(getActivity());
                    // Begin the transaction
                    BarcodeFragment barCodeScanner = new BarcodeFragment();
                    barCodeScanner.setTargetFragment(RecordCardFragment.this, 0);
                    barCodeScanner.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), null);
                    final EditText barcode = recordCard.findViewById(R.id.edit_barcode);
                    barCodeScanner.setOnBarCodeDetect(new BarcodeFragment.OnBarCodeDetect() {
                        @Override
                        public void onBarCodeDetect(String code) {
                            try {
                                barcode.setText(code);
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                                Toast.makeText(getContext(), "Lỗi!!! Không thể lấy BarCode.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    return false;
                }
            });
            menuItem_barcode.setVisible(true);

            MenuItem menuItem_new = menu.findItem(R.id.menuitem_save);
            menuItem_new.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    resetChecklist(recordCard);
                    return false;
                }
            });
            menuItem_new.setVisible(true);

            MenuItem menuItem_refresh = menu.findItem(R.id.menuitem_refresh);
            menuItem_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (getID() != 0) {
                        loadData(recordCard);
                    }
                    return false;
                }
            });
            menuItem_refresh.setVisible(true);

            MenuItem menuItem_search = menu.findItem(R.id.app_bar_search);
            final SearchView searchView = (SearchView) menuItem_search.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    final XCursor cursor = dataManager.searchProductData(s);
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            MainActivity.showProgressBar(getActivity());

                            final FragmentTransaction ft;
                            ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                            final SearchResultFragment searchResult = SearchResultFragment.newInstance(SearchResultFragment.ResultType.SEARCH_LIST.toString());
                            ft.addToBackStack(null);
                            ft.add(R.id.fr_phieu, searchResult);
                            ft.commit();
                            searchResultFragments.add(searchResult);

                            searchResult.setOnReadyListener(new SearchResultFragment.OnReadyListener() {
                                @Override
                                public void onReadyListener() {
                                    searchResult.setData(cursor);
                                }
                            });

                            searchResult.setOnActionListener(new SearchResultFragment.OnActionListener() {
                                @Override
                                public void onActionListener(SearchResultFragment.ButtonAction buttonAction, XCursor res, int position) {
                                    Log.i("TEST 279", String.valueOf(position));
                                    if (buttonAction == SearchResultFragment.ButtonAction.VIEW) {
                                        AutoCompleteTextView name = recordCard.findViewById(R.id.edit_name);
                                        String nme = res.getLine(position).getString(TableColumn.ProductTable.NAME);
                                        name.setText(nme);
                                        XCursor res2 = dataManager.getProductData(nme);
                                        XCursor.XData hashMap = res.getLine(position);
                                        loadSpinnerData(res2,
                                                hashMap.getString(TableColumn.ProductInStockTable.UNIT_ID),
                                                hashMap.getString(TableColumn.WarehouseTable.WAREHOUSE),
                                                hashMap.getString(TableColumn.QueueTable.QUEUE),
                                                hashMap.getString(TableColumn.CategoriesTable.CATEGORIES));
                                        loadTotal(res2);
                                    }

                                    List<SearchResultFragment> removed = new ArrayList<>();
                                    for (SearchResultFragment searchResultFragment : searchResultFragments) {
                                        searchResultFragment.dismiss();
                                        removed.add(searchResultFragment);
                                    }
                                    searchResultFragments.remove(removed);
                                }
                            });
                            return false;
                        } else {
                            cursor.close();
                        }
                    }
                    Toast.makeText(getContext(), "Không tìm thấy kết quả", Toast.LENGTH_LONG).show();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
            menuItem_search.setVisible(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void loadProductName(final AutoCompleteTextView autoCompleteTextView) {
        if (autoCompleteTextView.getAdapter() == null) {
            Cursor cursor = dataManager.getNameList();
            cursor.moveToFirst();
            String[] fruits = new String[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                fruits[i] = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();

            ArrayAdapter<String> suggestText;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                suggestText = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.dropdown, fruits);
                suggestText.setDropDownViewResource(R.layout.dropdown);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(suggestText);
            }
        }
    }

    Spinner addAdapterToSpinner(int id, String[] list, final XCursor cursor) {
        Spinner spinner = recordCard.findViewById(id);
        spinner.setOnItemSelectedListener(null);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.dropdown, list);
            adapter.setDropDownViewResource(R.layout.dropdown);
            spinner.setAdapter(adapter);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTotal(cursor);
                loadUnitSpinner(cursor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return spinner;
    }

    void loadSpinnerData(final XCursor res) {
        loadSpinnerData(res, null, null, null, null);
    }

    void loadSpinnerData(final XCursor res, String unitData, String warehouseData, String queueData, String categoriesData) {
        res.moveToFirstLine();
        List<String> warehouseList = new ArrayList<>();
        List<String> queueList = new ArrayList<>();
        List<String> categoriesList = new ArrayList<>();

        while (!res.isAfterLastLine()) {
            String warehouse = res.getLine().get(TableColumn.WarehouseTable.WAREHOUSE);
            String queue = res.getLine().get(TableColumn.QueueTable.QUEUE);
            String categories = res.getLine().get(TableColumn.CategoriesTable.CATEGORIES);

            if (!warehouseList.contains(warehouse)) {
                warehouseList.add(warehouse);
            }
            if (!categoriesList.contains(categories)) {
                categoriesList.add(categories);
            }
            if (!queueList.contains(queue)) {
                queueList.add(queue);
            }

            res.moveToNextLine();
        }

        String[] warehouseArray = warehouseList.toArray(new String[0]);
        String[] categoriesArray = categoriesList.toArray(new String[0]);
        String[] queueArray = queueList.toArray(new String[0]);


        Spinner warehouseSpinner = addAdapterToSpinner(R.id.edit_warehouse, warehouseArray, res);
        Spinner categoriesSpinner = addAdapterToSpinner(R.id.edit_categories, categoriesArray, res);
        Spinner queueSpinner = addAdapterToSpinner(R.id.edit_queue, queueArray, res);

        if (warehouseData != null)
            warehouseSpinner.setSelection(warehouseList.indexOf(warehouseData));
        if (categoriesData != null)
            categoriesSpinner.setSelection(categoriesList.indexOf(categoriesData));
        if (queueData != null) queueSpinner.setSelection(queueList.indexOf(queueData));

        loadUnitSpinner(res, unitData);
    }

    void loadUnitSpinner(final XCursor cursor, String unitData) {
        String name = ((AutoCompleteTextView) recordCard.findViewById(R.id.edit_name)).getText().toString();
        String warehouse = ((Spinner) recordCard.findViewById(R.id.edit_warehouse)).getSelectedItem().toString();

        XCursor res = dataManager.getUnitInStock(name, warehouse);
        res.moveToFirstLine();
        List<String> unitList = new ArrayList<>();

        while (!res.isAfterLastLine()) {
            String unit = res.getLine().getString(TableColumn.ProductInStockTable.UNIT_ID);
            if (!unitList.contains(unit)) {
                Log.i("TEST 479", unit);
                unitList.add(unit);
            }
            res.moveToNextLine();
        }

        Spinner unitSpinner = recordCard.findViewById(R.id.edit_unit);
        unitSpinner.setAdapter(null);

        for (String unit_id : unitList) {
            loadUnit(unitSpinner, unit_id);
        }

        if (unitData != null) {
            unitSpinner.setSelection(unitList.indexOf(unitData));
        }

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTotal(cursor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void loadUnitSpinner(final XCursor cursor) {
        loadUnitSpinner(cursor, null);
    }

    void loadUnit(Spinner unitSpinner, String unit_id) {
        SpinnerAdapter unitAdapter = unitSpinner.getAdapter();
        int count = unitAdapter == null ? 0 : unitAdapter.getCount();

        XCursor cursor = dataManager.getUnit(unit_id);
        XCursor.XData firstLine = cursor.getFirstLine();
        String[] fruits = new String[count + 3];

        for (int i = 0; i < count; i++) {
            Log.i("TEST 364", unitAdapter.getItem(i).toString());
            fruits[i] = unitAdapter.getItem(i).toString();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fruits[fruits.length - 3] = unit_id.concat("-").concat(firstLine.getString(TableColumn.UnitTable.UNIT))
                    .concat("- ").concat(firstLine.getString(TableColumn.UnitTable.UNIT3)).concat(" x")
                    .concat(firstLine.getString(TableColumn.UnitTable.FACTOR3)).concat(firstLine.getString(TableColumn.UnitTable.UNIT)).concat(" x")
                    .concat(firstLine.getString(TableColumn.UnitTable.FACTOR2)).concat(firstLine.getString(TableColumn.UnitTable.UNIT2));


            fruits[fruits.length - 2] = unit_id.concat("-").concat(firstLine.getString(TableColumn.UnitTable.UNIT2))
                    .concat("- ").concat(firstLine.getString(TableColumn.UnitTable.UNIT3)).concat(" x")
                    .concat(firstLine.getString(TableColumn.UnitTable.FACTOR3)).concat(firstLine.getString(TableColumn.UnitTable.UNIT)).concat(" x")
                    .concat(firstLine.getString(TableColumn.UnitTable.FACTOR2)).concat(firstLine.getString(TableColumn.UnitTable.UNIT2));

            fruits[fruits.length - 1] = unit_id.concat("-").concat(firstLine.getString(TableColumn.UnitTable.UNIT3))
                    .concat("- ").concat(firstLine.getString(TableColumn.UnitTable.UNIT3)).concat(" x")
                    .concat(firstLine.getString(TableColumn.UnitTable.FACTOR3)).concat(firstLine.getString(TableColumn.UnitTable.UNIT)).concat(" x")
                    .concat(firstLine.getString(TableColumn.UnitTable.FACTOR2)).concat(firstLine.getString(TableColumn.UnitTable.UNIT2));
        }
        cursor.close();

        ArrayAdapter<String> suggestText;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            suggestText = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.dropdown, fruits);
            suggestText.setDropDownViewResource(R.layout.dropdown);
            unitSpinner.setAdapter(suggestText);
        }
    }

    void loadTotal(XCursor cursor) {
        Spinner unitSpinner = recordCard.findViewById(R.id.edit_unit);
        if (unitSpinner.getAdapter() == null) return;
        Spinner warehouseSpinner = recordCard.findViewById(R.id.edit_warehouse);
        if (warehouseSpinner.getAdapter() == null) return;
        Spinner categoriesSpinner = recordCard.findViewById(R.id.edit_categories);
        if (categoriesSpinner.getAdapter() == null) return;
        Spinner queueSpinner = recordCard.findViewById(R.id.edit_queue);
        if (queueSpinner.getAdapter() == null) return;
        TextView editText = recordCard.findViewById(R.id.edit_total);

        AutoCompleteTextView autoCompleteTextView = recordCard.findViewById(R.id.edit_name);

        String name = autoCompleteTextView.getText().toString();
        String unit = unitSpinner.getSelectedItem().toString();
        String warehouse = warehouseSpinner.getSelectedItem().toString();
        String categories = categoriesSpinner.getSelectedItem().toString();
        String queue = queueSpinner.getSelectedItem().toString();

        String unit_id = unit.split("-")[0];
        String unit_val = unit.split("-")[1];

        cursor.moveToFirstLine();
        int total = 0;

        while (!cursor.isAfterLastLine()) {
            if (cursor.getLine().getString(TableColumn.ProductTable.NAME).equals(name) &&
                    cursor.getLine().getString(TableColumn.ProductInStockTable.UNIT_ID).equals(unit_id) &&
                    cursor.getLine().getString(TableColumn.QueueTable.QUEUE).equals(queue) &&
                    cursor.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE).equals(warehouse) &&
                    cursor.getLine().getString(TableColumn.CategoriesTable.CATEGORIES).equals(categories)) {

                total += cursor.getLine().getInt(TableColumn.ProductInStockTable.TOTAL);

            }
            cursor.moveToNextLine();
        }

        XCursor res = dataManager.getUnit(unit_id);
        XCursor.XData firstLine = res.getFirstLine();
        if (unit_val.equals(firstLine.getString(TableColumn.UnitTable.UNIT))) {
            editText.setText(String.valueOf(total));
        } else if (unit_val.equals(firstLine.get(TableColumn.UnitTable.UNIT2))) {
            editText.setText(total / firstLine.getInt(TableColumn.UnitTable.FACTOR2));
        } else if (unit_val.equals(firstLine.get(TableColumn.UnitTable.UNIT3))) {
            editText.setText(String.valueOf(total / firstLine.getInt(TableColumn.UnitTable.FACTOR3)));
        }
        res.close();

        loadDifference();
    }

    void loadDifference() {
        TextView editTotal = recordCard.findViewById(R.id.edit_total);
        TextView editDiff = recordCard.findViewById(R.id.edit_diff);
        EditText editReal = recordCard.findViewById(R.id.edit_real);

        int total = 0;
        int real = 0;
        try {
            total = Integer.parseInt(editTotal.getText().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            real = Integer.parseInt(editReal.getText().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int diff = real - total;

        editDiff.setText(String.valueOf(diff));
    }

    private void loadBarCode(AutoCompleteTextView autoCompleteTextView) {
        EditText barCode = recordCard.findViewById(R.id.edit_barcode);
        String code = barCode.getText().toString();
        String name = dataManager.getBarCodeProduct(code);
        if (!name.equals("")) {
            autoCompleteTextView.setText(name);
            XCursor res = dataManager.getProductData(name);
            loadSpinnerData(res);
        }
    }

    void loadData(View v) {
        XCursor res = dataManager.getChecklistCard(getID());
        if (res != null) {
            res.moveToFirstLine();

            EditText barcode = v.findViewById(R.id.edit_barcode);
            barcode.setText("");

            TextView date = v.findViewById(R.id.edit_date);
            date.setText(res.getLine().getString(TableColumn.ChecklistTable.DATE));

            EditText real = v.findViewById(R.id.edit_real);
            real.setText(res.getLine().getString(TableColumn.ChecklistTable.REAL));

            TextView total = v.findViewById(R.id.edit_total);
            total.setText(res.getLine().getString(TableColumn.ProductInStockTable.TOTAL));

            AutoCompleteTextView name = v.findViewById(R.id.edit_name);
            name.setText(res.getLine().getString(TableColumn.ProductTable.NAME));

            XCursor cursor = dataManager.getProductData(res.getLine().getString(TableColumn.ProductTable.NAME));
            loadSpinnerData(cursor);

            Spinner unit = v.findViewById(R.id.edit_unit);
            unit.setSelection(getIndex(unit, res.getLine().getString(TableColumn.UnitTable.ID)));

            Spinner queue = v.findViewById(R.id.edit_queue);
            queue.setSelection(getIndex(queue, res.getLine().getString(TableColumn.QueueTable.QUEUE)));


            Spinner warehouse = v.findViewById(R.id.edit_warehouse);
            warehouse.setSelection(getIndex(warehouse, res.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE)));

            Spinner categories = v.findViewById(R.id.edit_categories);
            categories.setSelection(getIndex(categories, res.getLine().getString(TableColumn.CategoriesTable.CATEGORIES)));

            nameSave.add(res.getLine().getString(TableColumn.ProductTable.NAME)); //name
            nameSave.add(res.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE)); //warehouse
            nameSave.add(res.getLine().getString(TableColumn.QueueTable.QUEUE)); //queue
            nameSave.add(res.getLine().getString(TableColumn.CategoriesTable.CATEGORIES)); //categories

            res.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
            }
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    //TODO: cần tối ưu code hàm saveData()
    public void saveData(final View view) {
        final EditText barCode = view.findViewById(R.id.edit_barcode);
        final Spinner unit = view.findViewById(R.id.edit_unit);
        final Spinner queue = view.findViewById(R.id.edit_queue);
        final Spinner warehouse = view.findViewById(R.id.edit_warehouse);
        final Spinner categories = view.findViewById(R.id.edit_categories);
        final TextView date = view.findViewById(R.id.edit_date);
        final EditText real = view.findViewById(R.id.edit_real);
        EditText name = view.findViewById(R.id.edit_name);

        final String pid = saveData_checkInput(name.getText().toString(), date.getText().toString());
        if (pid == null) return;

        int num = 0;
        if (!real.getText().toString().equals("")) {
            num = Integer.valueOf(real.getText().toString());
        }

        final String unit_id = unit.getSelectedItem().toString().split("-")[0];
        String unit_val = unit.getSelectedItem().toString().split("-")[1];

        XCursor res = dataManager.getUnit(unit_id);
        XCursor.XData firstLine = res.getFirstLine();

        if (unit_val.equals(firstLine.get(TableColumn.UnitTable.UNIT2))) {
            num = num * firstLine.getInt(TableColumn.UnitTable.FACTOR2);
        } else if (unit_val.equals(firstLine.getString(TableColumn.UnitTable.UNIT3))) {
            num = num * firstLine.getInt(TableColumn.UnitTable.FACTOR3);
        }
        res.close();

        final int finalNum = num;

        if (getID() == 0) {
            final XCursor cursor = checkSaved();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    MainActivity.showProgressBar(getActivity());

                    FragmentTransaction ft;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                        final SearchResultFragment searchResult = SearchResultFragment.newInstance(SearchResultFragment.ResultType.DUPLICATE_LIST.toString());
                        ft.addToBackStack(null);
                        ft.add(R.id.fr_phieu, searchResult);
                        ft.commit();
                        searchResultFragments.add(searchResult);

                        searchResult.setOnReadyListener(new SearchResultFragment.OnReadyListener() {
                            @Override
                            public void onReadyListener() {
                                searchResult.setData(cursor);
                            }
                        });

                        searchResult.setOnActionListener(new SearchResultFragment.OnActionListener() {
                            @Override
                            public void onActionListener(SearchResultFragment.ButtonAction buttonAction, XCursor res, int position) {
                                boolean rs;
                                switch (buttonAction) {
                                    case CREATE:
                                        rs = dataManager.insertData(
                                                pid,
                                                barCode.getText().toString(),
                                                String.valueOf(finalNum),
                                                unit_id,
                                                warehouse.getSelectedItem().toString(),
                                                queue.getSelectedItem().toString(),
                                                categories.getSelectedItem().toString(),
                                                date.getText().toString()
                                        );
                                        saveData_message(rs);
                                        searchResult.dismiss();
                                        break;
                                    case UPDATE:
                                        rs = dataManager.updateData(
                                                res.getLine(position).getString(TableColumn.ChecklistTable.CID),
                                                pid,
                                                barCode.getText().toString(),
                                                String.valueOf(finalNum),
                                                unit_id,
                                                warehouse.getSelectedItem().toString(),
                                                queue.getSelectedItem().toString(),
                                                categories.getSelectedItem().toString(),
                                                date.getText().toString()
                                        );
                                        saveData_message(rs);
                                        searchResult.dismiss();
                                        break;
                                    case VIEW:
                                        MainActivity.showProgressBar(getActivity());
                                        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                                        RecordCardFragment phieu = RecordCardFragment.newInstance(res.getLine(position).getString(TableColumn.ChecklistTable.CID));
                                        ft.addToBackStack(null);
                                        ft.add(R.id.fr_phieu, phieu);
                                        ft.commit();
                                        break;
                                    default:
                                        searchResult.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Lỗi hệ thống.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    cursor.close();
                    boolean rs = dataManager.insertData(
                            pid,
                            barCode.getText().toString(),
                            String.valueOf(num),
                            unit_id,
                            warehouse.getSelectedItem().toString(),
                            queue.getSelectedItem().toString(),
                            categories.getSelectedItem().toString(),
                            date.getText().toString()
                    );
                    saveData_message(rs);
                }
            } else {
                boolean rs = dataManager.insertData(
                        pid,
                        barCode.getText().toString(),
                        String.valueOf(num),
                        unit_id,
                        warehouse.getSelectedItem().toString(),
                        queue.getSelectedItem().toString(),
                        categories.getSelectedItem().toString(),
                        date.getText().toString()
                );
                saveData_message(rs);
            }

        } else {
            if (nameSave.contains(name.getText().toString())
                    && nameSave.contains(warehouse.getSelectedItem().toString())
                    && nameSave.contains(categories.getSelectedItem().toString())
                    && nameSave.contains(queue.getSelectedItem().toString())) {
                boolean rs = dataManager.updateData(
                        String.valueOf(getID()),
                        pid,
                        barCode.getText().toString(),
                        String.valueOf(num),
                        unit_id,
                        warehouse.getSelectedItem().toString(),
                        queue.getSelectedItem().toString(),
                        categories.getSelectedItem().toString(),
                        date.getText().toString()
                );
                saveData_message(rs);
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Lưu ý:");
                alertDialog.setMessage("Sản phẩm đã bị thay đổi, bạn có muốn tiếp tục cập nhật phiếu?");
                alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        boolean rs = dataManager.updateData(
                                String.valueOf(getID()),
                                pid,
                                barCode.getText().toString(),
                                String.valueOf(finalNum),
                                unit_id,
                                warehouse.getSelectedItem().toString(),
                                queue.getSelectedItem().toString(),
                                categories.getSelectedItem().toString(),
                                date.getText().toString()
                        );
                        saveData_message(rs);
                    }
                });
                alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }
    }

    private String saveData_checkInput(String name, String date) {
        String pid;
        if (name.equals("")) {
            Toast.makeText(getContext(), "Tên sản phẩm không được bỏ trống.", Toast.LENGTH_LONG).show();
            return null;
        } else {
            pid = dataManager.getPID(name);
        }

        if (pid.equals("")) {
            Toast.makeText(getContext(), "Lỗi!!! Không tìm thấy hoặc nhiều hơn 1 kết quả.", Toast.LENGTH_LONG).show();
            return null;
        }

        if (date.equals("")) {
            Toast.makeText(getContext(), "Lỗi!!! Ngày cận date không được bỏ trống.", Toast.LENGTH_LONG).show();
            return null;
        }

        return pid;
    }

    private void saveData_message(boolean rs) {
        if (rs) {
            resetChecklist(recordCard);
            Toast.makeText(getContext(), "Đã lưu.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Lỗi không xác định.", Toast.LENGTH_LONG).show();
        }
    }

    private void resetChecklist(View view) {
        setID(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        }

        EditText barcode = view.findViewById(R.id.edit_barcode);
        Spinner unit = view.findViewById(R.id.edit_unit);
        Spinner queue = view.findViewById(R.id.edit_queue);
        Spinner warehouse = view.findViewById(R.id.edit_warehouse);
        Spinner categories = view.findViewById(R.id.edit_categories);
        TextView date = view.findViewById(R.id.edit_date);
        EditText real = view.findViewById(R.id.edit_real);
        TextView diff = view.findViewById(R.id.edit_diff);
        TextView total = view.findViewById(R.id.edit_total);
        AutoCompleteTextView name = view.findViewById(R.id.edit_name);

        ScrollView scrollView = view.findViewById(R.id.phieu_scroll);

        barcode.setText("");
        unit.setAdapter(null);
        queue.setAdapter(null);
        warehouse.setAdapter(null);
        categories.setAdapter(null);
        date.setText("");
        real.setText("");
        diff.setText("");
        total.setText("");
        name.setText("");

        scrollView.scrollTo(0, 0);
    }

    public void actionShowDatePicker(View view) {
        if (!showDatePicker(view)) {
            Log.e("DATEPICKER", "Can not init datepicker!!!");
        } else {
            Log.i("DATEPICKER", "Datepicker is showing!!!");
        }
    }

    private boolean showDatePicker(View view) {
        MainActivity.showProgressBar(getActivity());
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                final DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getContext()));
                dialog.show();

                TextView textView = (TextView) view;
                try {
                    if (textView.getText().length() == 10) {
                        ConvertStringToDate date = new ConvertStringToDate(textView.getText().toString());
                        dialog.getDatePicker().updateDate(date.getYear(), date.getMonth() - 1, date.getDay());
                    }
                } catch (Exception ex) {
                    //pass
                    Log.i("DATEPICKER", "No init date.");
                }

                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ConvertStringToDate date = new ConvertStringToDate(dayOfMonth, month + 1, year);
                        String value = date.getInvert();
                        Log.i("DATE", value);
                        TextView textView = Objects.requireNonNull(getActivity()).findViewById(R.id.edit_date);
                        textView.setText(value);
                        Button save = recordCard.findViewById(R.id.btn_save);
                        save.requestFocus();
                        MainActivity.hideKeyboard(getActivity());
                    }
                });
                MainActivity.hideKeyboard(getActivity());
                return true;
            } else {
                MainActivity.hideKeyboard(getActivity());
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MainActivity.hideKeyboard(getActivity());
            return false;
        }
    }

    XCursor checkSaved() {
        Spinner queue = recordCard.findViewById(R.id.edit_queue);
        Spinner warehouse = recordCard.findViewById(R.id.edit_warehouse);
        Spinner categories = recordCard.findViewById(R.id.edit_categories);
        Spinner unit = recordCard.findViewById(R.id.edit_unit);
        TextView date = recordCard.findViewById(R.id.edit_date);
        AutoCompleteTextView name = recordCard.findViewById(R.id.edit_name);

        return dataManager.getChecklistCard(
                name.getText().toString(),
                unit.getSelectedItem().toString().split("-")[0],
                warehouse.getSelectedItem().toString(),
                categories.getSelectedItem().toString(),
                queue.getSelectedItem().toString(),
                date.getText().toString());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
