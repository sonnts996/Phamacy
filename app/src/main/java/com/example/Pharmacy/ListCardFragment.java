package com.example.Pharmacy;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListCardFragment extends Fragment {

    private static final String ARG_PARAM1 = "data";
    private String searchData = "";
    private String lastCommand;
    private CommandType commandType;
    private View listCard;
    private DataManager dataManager;

    public ListCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ListCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListCardFragment newInstance(String param1) {
        ListCardFragment fragment = new ListCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchData = getArguments().getString(ARG_PARAM1);
        }

        try {
            dataManager = MainActivity.dataManager;
            Log.i("USERNAME", dataManager.getUsername());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listCard = inflater.inflate(R.layout.fr_list_card, container, false);
        setHasOptionsMenu(true);

        final SearchView searchView = listCard.findViewById(R.id.search_list_checklist);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                searchView.requestFocus();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                MainActivity.showProgressBar(getActivity());

                searchListCard(s);

                MainActivity.hideProgressBar(getActivity());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        if (!searchData.equals("")) {
            searchListCard(searchData);
        } else {
            getListCard();
        }

        MainActivity.hideProgressBar(getActivity());
        return listCard;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        android.support.v7.app.ActionBar actionBar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            actionBar = ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("Danh sách phiếu kiểm kê");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.show();

            MenuItem menuItem_date = menu.findItem(R.id.menuitem_date);
            menuItem_date.setVisible(false);

            MenuItem menuItem_barcode = menu.findItem(R.id.menuitem_barcode);
            menuItem_barcode.setVisible(false);

            MenuItem menuItem_save = menu.findItem(R.id.menuitem_save);
            menuItem_save.setVisible(false);

            MenuItem menuItem_refresh = menu.findItem(R.id.menuitem_refresh);
            menuItem_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MainActivity.showProgressBar(getActivity());
                    if (commandType.equals(CommandType.INIT)) {
                        getListCard();
                    } else if (commandType.equals(CommandType.SEARCH)) {
                        searchListCard(lastCommand);
                    }
                    MainActivity.hideProgressBar(getActivity());
                    return false;
                }
            });
            menuItem_refresh.setVisible(true);

            MenuItem menuItem_search = menu.findItem(R.id.app_bar_search);
            menuItem_search.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onOptionsItemSelected(item);
        }
        return true;
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

    public void setLastCommand(CommandType function, String query) {
        this.commandType = function;
        this.lastCommand = query;
    }

    public void setLastCommand(CommandType function) {
        this.commandType = function;
        this.lastCommand = "";
    }

    private void add_data_card(View v, final String sophieu, String vitri, String ngaythang, String loai) {
        LinearLayout linearLayout = v.findViewById(R.id.list_data);
        View cardView = getLayoutInflater().inflate(R.layout.card_data_card, linearLayout, false);
        linearLayout.addView(cardView);
        TextView textView1 = cardView.findViewById(R.id.view_cid);
        textView1.setText(sophieu);
        TextView textView2 = cardView.findViewById(R.id.view_position);
        textView2.setText(vitri);
        TextView textView3 = cardView.findViewById(R.id.view_category);
        textView3.setText(loai);
        TextView textView4 = cardView.findViewById(R.id.view_date);
        textView4.setText(ngaythang);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    RecordCardFragment phieu = RecordCardFragment.newInstance(sophieu);
                    ft.addToBackStack(null);
                    ft.add(R.id.fr_ds_phieu, phieu);
                    ft.commit();
                }
            }
        });
    }

    private void add_data_card(View v, final Cursor cursor) {
        LinearLayout linearLayout = v.findViewById(R.id.list_data);
        View cardView = getLayoutInflater().inflate(R.layout.card_data_card, linearLayout, false);
        linearLayout.addView(cardView);
        TextView textView1 = cardView.findViewById(R.id.view_cid);
        final String cid = cursor.getString(0);
        textView1.setText(cid);
        TextView textView2 = cardView.findViewById(R.id.view_position);
        textView2.setText("unknown");
        TextView textView3 = cardView.findViewById(R.id.view_category);
        final String cate = (cursor.getString(8).equals("") ? "" : ("KK " + cursor.getString(7))) + " - KK tay";
        textView3.setText(cate);
        TextView textView4 = cardView.findViewById(R.id.view_date);
        textView4.setText(cursor.getString(9));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    MainActivity.showProgressBar(getActivity());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    RecordCardFragment phieu = RecordCardFragment.newInstance(cid);
                    ft.addToBackStack(null);
                    ft.add(R.id.fr_ds_phieu, phieu);
                    ft.commit();
                }
            }
        });
    }

    void searchListCard(String value) {
        Cursor cursor = dataManager.searchChecklistCard(value);
        if (cursor != null) {
            LinearLayout sr = listCard.findViewById(R.id.list_data);
            sr.removeAllViews();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                add_data_card(listCard, cursor);
                cursor.moveToNext();
            }
            cursor.close();
            setLastCommand(CommandType.SEARCH, value);
        } else {
            Toast.makeText(getContext(), "Lỗi!!! Không thể lấy dữ liệu.", Toast.LENGTH_LONG).show();
        }
    }

    void getListCard() {
        Cursor cursor = dataManager.getChecklistCard();
        if (cursor != null) {
            LinearLayout sr = listCard.findViewById(R.id.list_data);
            sr.removeAllViews();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                add_data_card(listCard, cursor);
                cursor.moveToNext();
            }
            cursor.close();
            setLastCommand(CommandType.INIT);
        } else {
            Toast.makeText(getContext(), "Lỗi!!! Không thể lấy dữ liệu.", Toast.LENGTH_LONG).show();
        }
    }

    enum CommandType {
        SEARCH, INIT
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
