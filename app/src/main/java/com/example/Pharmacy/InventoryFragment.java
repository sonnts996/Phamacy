package com.example.Pharmacy;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Pharmacy.test.data.ProductInStockData;
import com.xu.database.XCursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryFragment extends Fragment {

    View invertory;
    DataManager dataManager;

    public InventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = MainActivity.dataManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        invertory = inflater.inflate(R.layout.fr_inventory, container, false);
        setHasOptionsMenu(true);

        AutoCompleteTextView name = invertory.findViewById(R.id.inv_edit_name);
        loadProductName(name);
        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        XCursor res = dataManager.getWarehouseData();
        loadSpinnerData(res);

        Button search = invertory.findViewById(R.id.inv_btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView name = invertory.findViewById(R.id.inv_edit_name);
                Spinner stock = invertory.findViewById(R.id.inv_edit_stock);

                XCursor cursor = dataManager.getInventorStock(name.getText().toString(), stock.getSelectedItem().toString());
                addDataRow(cursor);
            }
        });

        MainActivity.hideProgressBar(getActivity());
        return invertory;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        android.support.v7.app.ActionBar actionBar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            actionBar = ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("Xem tồn kho");
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
                    final BarcodeFragment barCodeScanner = new BarcodeFragment();
                    barCodeScanner.setTargetFragment(InventoryFragment.this, 0);
                    barCodeScanner.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), null);
                    barCodeScanner.setOnBarCodeDetect(new BarcodeFragment.OnBarCodeDetect() {
                        @Override
                        public void onBarCodeDetect(String code) {
                            try {
                                String name = dataManager.getBarCodeProduct(code);
                                AutoCompleteTextView edit = invertory.findViewById(R.id.inv_edit_name);
                                Log.i("TEST 146", name);
                                edit.setText(name);
                                if (!name.equals("")) {
                                    XCursor res = dataManager.getProductData(name);
                                    loadSpinnerData(res);
                                }
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

            MenuItem menuItem_save = menu.findItem(R.id.menuitem_save);
            menuItem_save.setVisible(false);

            MenuItem menuItem_refresh = menu.findItem(R.id.menuitem_refresh);
            menuItem_refresh.setVisible(false);

            MenuItem menuItem_search = menu.findItem(R.id.app_bar_search);
            menuItem_search.setVisible(false);
        }
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

    void loadSpinnerData(final XCursor res) {
        res.moveToFirstLine();
        List<String> warehouseList = new ArrayList<>();

        warehouseList.add("");
        while (!res.isAfterLastLine()) {
            String warehouse = res.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE);

            if (!warehouseList.contains(warehouse)) {
                warehouseList.add(warehouse);
            }
            res.moveToNextLine();
        }

        String[] warehouseArray = warehouseList.toArray(new String[0]);


        addAdapterToSpinner(warehouseArray);
    }

    void addAdapterToSpinner(String[] list) {
        Spinner spinner = invertory.findViewById(R.id.inv_edit_stock);
        spinner.setOnItemSelectedListener(null);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.dropdown, list);
            adapter.setDropDownViewResource(R.layout.dropdown);
            spinner.setAdapter(adapter);
        }

    }

    void addDataRow(XCursor cursor) {
        if (cursor == null) return;
        cursor.moveToFirstLine();
        TableLayout tableLayout = invertory.findViewById(R.id.inv_table_data);
        cleanTable(tableLayout);

        while (!cursor.isAfterLastLine()) {
            View row = getLayoutInflater().inflate(R.layout.row_inventory, tableLayout, false);
            tableLayout.addView(row);

            if (tableLayout.indexOfChild(row) % 2 == 0)
                row.setBackgroundColor(Color.rgb(200, 200, 200));

            TextView masp = row.findViewById(R.id.inv_view_pid);
            TextView tensp = row.findViewById(R.id.inv_view_name);
            TextView soluong1 = row.findViewById(R.id.inv_view_sl1);
            TextView soluong3 = row.findViewById(R.id.inv_view_sl3);
            TextView warehouse = row.findViewById(R.id.inv_view_warehouse);

            masp.setText(cursor.getLine().getString(TableColumn.ProductInStockTable.PID));
            tensp.setText(cursor.getLine().getString(TableColumn.ProductTable.NAME));
            warehouse.setText(cursor.getLine().getString(TableColumn.ProductInStockTable.STOCK_ID).concat(" - ").concat(cursor.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE)));

            XCursor unitCursor = dataManager.getUnit(cursor.getLine().getString(TableColumn.ProductInStockTable.UNIT_ID));
            int sl = cursor.getLine().getInt(TableColumn.ProductInStockTable.TOTAL);
            int sl1 = sl / unitCursor.getFirstLine().getInt(TableColumn.UnitTable.FACTOR3);
            soluong1.setText(String.valueOf(sl1).concat(unitCursor.getFirstLine().getString(TableColumn.UnitTable.UNIT3)));
            soluong3.setText(String.valueOf(sl).concat(unitCursor.getFirstLine().getString(TableColumn.UnitTable.UNIT)));
            unitCursor.close();

            cursor.moveToNextLine();
        }
        cursor.close();

        TextView numRow = invertory.findViewById(R.id.inv_view_numrow);
        numRow.setText(String.valueOf(tableLayout.getChildCount() - 1));
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}
