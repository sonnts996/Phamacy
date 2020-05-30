package com.example.Pharmacy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Pharmacy.support.ConvertStringToDate;
import com.xu.database.XCursor;

import java.util.Locale;
import java.util.Objects;

public class ConfirmFragment extends Fragment {

    View confirmView;
    DataManager dataManager;

    public ConfirmFragment() {
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
        confirmView = inflater.inflate(R.layout.fr_confirm, container, false);
        setHasOptionsMenu(true);

        TabLayout tabLayout = confirmView.findViewById(R.id.tab_bar_confirm);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TableLayout tableLayout = confirmView.findViewById(R.id.confirm_table_data);
                switch (tab.getPosition()) {
                    case 0:
                        tableLayout.setColumnCollapsed(7, true);
                        tableLayout.setColumnCollapsed(2, false);
                        break;
                    case 1:
                        tableLayout.setColumnCollapsed(7, false);
                        tableLayout.setColumnCollapsed(2, true);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TableLayout tableLayout = confirmView.findViewById(R.id.confirm_table_data);
        tableLayout.setColumnCollapsed(7, true);
        tableLayout.setColumnCollapsed(2, false);

        Button confirmBtn = confirmView.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout tableLayout = confirmView.findViewById(R.id.confirm_table_data);
                for (int i = 1; i < tableLayout.getChildCount(); i++) {
                    View row = tableLayout.getChildAt(i);
                    TextView confirmBox = row.findViewById(R.id.confirm_view_slxn);
                    TextView confirmCid = row.findViewById(R.id.confirm_hide_cid);


                    boolean rs = dataManager.updateColumn(confirmCid.getText().toString(), confirmBox.getText().toString());
                    if (rs) {
                        Toast.makeText(getContext(), "Đã lưu", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Lỗi!!! Không thể lưu", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        MainActivity.hideProgressBar(getActivity());
        return confirmView;
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
            actionBar.setTitle("Xác nhận kiểm kê");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.show();

            MenuItem menuItem_date = menu.findItem(R.id.menuitem_date);
            menuItem_date.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    action_show_datePicker();
                    return false;
                }
            });
            menuItem_date.setVisible(true);

            MenuItem menuItem_barcode = menu.findItem(R.id.menuitem_barcode);
            menuItem_barcode.setVisible(false);

            MenuItem menuItem_save = menu.findItem(R.id.menuitem_save);
            menuItem_save.setVisible(false);

            MenuItem menuItem_refresh = menu.findItem(R.id.menuitem_refresh);
            menuItem_refresh.setVisible(false);

            MenuItem menuItem_search = menu.findItem(R.id.app_bar_search);
            SearchView searchView = (SearchView) menuItem_search.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public boolean onQueryTextSubmit(String s) {
                    MainActivity.showProgressBar(getActivity());

                    XCursor cursor = dataManager.searchConfirmData(s);
                    TableLayout tableLayout = confirmView.findViewById(R.id.confirm_table_data);
                    cleanTable(tableLayout);
                    TextView dateView = confirmView.findViewById(R.id.confirm_view_date);
                    dateView.setText("all day");

                    searchData(tableLayout, cursor);
                    MainActivity.hideProgressBar(getActivity());
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

    public void action_show_datePicker() {
        MainActivity.showProgressBar(getActivity());
        if (!show_datePicker()) {
            Log.e("DATEPICKER", "Can not init datepicker!!!");
        } else {
            Log.i("DATEPICKER", "Datepicker is showing!!!");
        }
    }

    private boolean show_datePicker() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                final DatePickerDialog selectDate = new DatePickerDialog(Objects.requireNonNull(getContext()));
                selectDate.show();
                selectDate.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        MainActivity.hideProgressBar(getActivity());
                    }
                });
                selectDate.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        MainActivity.hideProgressBar(getActivity());
                    }
                });
                selectDate.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        MainActivity.showProgressBar(getActivity());

                        ConvertStringToDate date = new ConvertStringToDate(
                                selectDate.getDatePicker().getDayOfMonth(),
                                (selectDate.getDatePicker().getMonth() + 1),
                                selectDate.getDatePicker().getYear()
                        );
                        String value = date.getInvert();

                        TableLayout tableLayout = confirmView.findViewById(R.id.confirm_table_data);
                        cleanTable(tableLayout);
                        TextView dateView = confirmView.findViewById(R.id.confirm_view_date);
                        dateView.setText(value);

                        XCursor cursor = dataManager.getChecklistCardByDate(value);
                        searchData(tableLayout, cursor);
                        MainActivity.hideProgressBar(getActivity());
                    }
                });
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void searchData(TableLayout tableLayout, XCursor cursor) {
        if (cursor != null) {
            cursor.moveToFirstLine();
            int row = 0;
            while (!cursor.isAfterLastLine()) {
                addDataRow(tableLayout, cursor);
                row++;
                cursor.moveToNextLine();
            }
            cursor.close();

            TextView countView = confirmView.findViewById(R.id.confirm_view_count);
            countView.setText(String.valueOf(row));
        } else {
            Toast.makeText(getContext(), "Lỗi!!! Không thể lấy dữ liệu.", Toast.LENGTH_LONG).show();
        }

    }

    private void addDataRow(TableLayout tableLayout, XCursor cursor) {
        View row = getLayoutInflater().inflate(R.layout.row_confirm_data, tableLayout, false);
        tableLayout.addView(row);

        if (tableLayout.indexOfChild(row) % 2 == 0)
            row.setBackgroundColor(Color.rgb(200, 200, 200));

        TextView cid = row.findViewById(R.id.confirm_hide_cid);
        cid.setText(cursor.getLine().getString(TableColumn.ChecklistTable.CID));
        TextView name = row.findViewById(R.id.confirm_view_name);
        name.setText(cursor.getLine().getString(TableColumn.ProductTable.NAME));
        TextView warehouse = row.findViewById(R.id.confirm_view_warehouse);
        warehouse.setText(cursor.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE));
        TextView slht = row.findViewById(R.id.confirm_view_slht);
        slht.setText(cursor.getLine().getString(TableColumn.ProductInStockTable.TOTAL));
        TextView slkk = row.findViewById(R.id.confirm_view_slkk);
        slkk.setText(cursor.getLine().getString(TableColumn.ChecklistTable.REAL));
        TextView sll = row.findViewById(R.id.confirm_view_sll);
        int lech = cursor.getLine().getInt(TableColumn.ChecklistTable.REAL) - cursor.getLine().getInt(TableColumn.ProductInStockTable.TOTAL);
        sll.setText(String.valueOf(lech));
        EditText cfm = row.findViewById(R.id.confirm_view_slxn);
        cfm.setText(cursor.getLine().getString(TableColumn.ChecklistTable.CONFIRM));
        TextView giatri = row.findViewById(R.id.confirm_view_giatri);
        int price = cursor.getLine().getInt(TableColumn.ChecklistTable.REAL) * cursor.getLine().getInt(TableColumn.ProductInStockTable.PRICE);
        giatri.setText(String.format(Locale.getDefault(),"%,d₫", price));
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}
