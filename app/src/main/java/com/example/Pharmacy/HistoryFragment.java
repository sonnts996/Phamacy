package com.example.Pharmacy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Pharmacy.support.ConvertStringToDate;
import com.xu.database.XCursor;

import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HistoryFragment extends Fragment {

    DataManager dataManager;
    View history;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            dataManager = MainActivity.dataManager;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        history = inflater.inflate(R.layout.fr_history, container, false);
        setHasOptionsMenu(true);

        TabLayout tabLayout = history.findViewById(R.id.tab_bar_histories);
        TableLayout tableLayout = history.findViewById(R.id.table_data);
        actionTab(tableLayout, tabLayout.getSelectedTabPosition());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TableLayout tableLayout = history.findViewById(R.id.table_data);
                actionTab(tableLayout, tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        XCursor cursor = dataManager.getHistories();
        int row =0;
        if(cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToNextLine();
                while (!cursor.isAfterLastLine()) {
                    add_data_row(cursor);
                    cursor.moveToNextLine();
                    row++;
                }
            }
            cursor.close();
        }
        TextView countView = history.findViewById(R.id.view_count);
        countView.setText(String.valueOf(row));

        MainActivity.hideProgressBar(getActivity());
        return history;
    }

    private void actionTab(TableLayout tableLayout, int tabPosition) {
        switch (tabPosition) {
            case 0:
                tableLayout.setColumnCollapsed(HistoryTableView.BARCODE, false);
                tableLayout.setColumnCollapsed(HistoryTableView.USER, true);
                tableLayout.setColumnCollapsed(HistoryTableView.SYNC, true);
                break;
            case 1:
                tableLayout.setColumnCollapsed(HistoryTableView.BARCODE, true);
                tableLayout.setColumnCollapsed(HistoryTableView.USER, false);
                tableLayout.setColumnCollapsed(HistoryTableView.SYNC, false);
                break;
            default:
                //pass
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        android.support.v7.app.ActionBar actionBar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            actionBar = ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("Lịch sử kiểm kê");
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

                    XCursor cursor = dataManager.getHistoriesByName(s);
                    if (cursor != null) {
                        cleanTable((TableLayout) history.findViewById(R.id.table_data));
                        TextView dateView = history.findViewById(R.id.view_date);
                        dateView.setText("all day");

                        cursor.moveToFirstLine();
                        int row = 0;
                        while (!cursor.isAfterLastLine()) {
                            add_data_row(cursor);
                            row++;
                            cursor.moveToNextLine();
                        }
                        cursor.close();

                        TextView countView = history.findViewById(R.id.view_count);
                        countView.setText(String.valueOf(row));
                    } else {
                        Toast.makeText(getContext(), "Lỗi!!! Không thể lấy dữ liệu.", Toast.LENGTH_LONG).show();
                    }

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

                        TableLayout tableLayout = history.findViewById(R.id.table_data);
                        cleanTable(tableLayout);

                        TextView dateView = history.findViewById(R.id.view_date);
                        dateView.setText(value);

                        XCursor cursor = dataManager.getHistories(value);
                        cursor.moveToFirstLine();
                        int row = 0;
                        while (!cursor.isAfterLastLine()) {
                            add_data_row(cursor);
                            row++;
                            cursor.moveToNextLine();
                        }
                        cursor.close();

                        TextView countView = history.findViewById(R.id.view_count);
                        countView.setText(String.valueOf(row));
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

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    private void add_data_row(XCursor cursor) {
        TableLayout tableLayout = history.findViewById(R.id.table_data);
        View row = getLayoutInflater().inflate(R.layout.row_data_history, tableLayout, false);
        tableLayout.addView(row);

        if (tableLayout.indexOfChild(row) % 2 == 0)
            row.setBackgroundColor(Color.rgb(200, 200, 200));

        TextView textView1 = row.findViewById(R.id.view_pid);
        String format = "%05d";
        String pid = String.format(Locale.getDefault(), format, cursor.getLine().getInt(TableColumn.HistoriesChecklistTable.PID));
        textView1.setText(pid);
        TextView textView2 = row.findViewById(R.id.view_barcode);
        textView2.setText(cursor.getLine().getString(TableColumn.HistoriesChecklistTable.BARCODE));
        TextView textView3 = row.findViewById(R.id.view_name);
        textView3.setText(cursor.getLine().getString(TableColumn.ProductTable.NAME));
        TextView textView4 = row.findViewById(R.id.view_real);
        textView4.setText(cursor.getLine().getString(TableColumn.HistoriesChecklistTable.REAL));
        TextView textView5 = row.findViewById(R.id.view_unit);
        textView5.setText(cursor.getLine().getString(TableColumn.UnitTable.UNIT));
        TextView textView6 = row.findViewById(R.id.view_synccount);
        textView6.setText(cursor.getLine().getString(TableColumn.HistoriesChecklistTable.SYNCCOUNT));
        TextView textView7 = row.findViewById(R.id.view_warehouse);
        textView7.setText(cursor.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE));
        TextView textView8 = row.findViewById(R.id.view_user);
        textView8.setText(cursor.getLine().getString(TableColumn.HistoriesChecklistTable.SYNCACCOUNT));
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

    static final class HistoryTableView {
        static final int MASP = 0;
        static final int BARCODE = 1;
        static final int TENTHUOC = 2;
        static final int USER = 3;
        static final int SL = 4;
        static final int DVT = 5;
        static final int SYNC = 6;
        static final int KHO = 7;
    }
}
