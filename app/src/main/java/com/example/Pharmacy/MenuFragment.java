package com.example.Pharmacy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_menu, container, false);
        setHasOptionsMenu(true);

        final Button dsPhieu = view.findViewById(R.id.btn_list_checklist);
        Button kiemKe = view.findViewById(R.id.btn_checklist);

        final Button lichSu = view.findViewById(R.id.btn_histories);
        Button tonKho = view.findViewById(R.id.btn_inventory);
        Button xacNhan = view.findViewById(R.id.btn_confirm);

        dsPhieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showProgressBar(getActivity());
                // Begin the transaction
                FragmentTransaction ft;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fr_menu, new ListCardFragment());
                    ft.commit();
                }
            }
        });

        kiemKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showProgressBar(getActivity());
                // Begin the transaction
                FragmentTransaction ft;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fr_menu, new RecordCardFragment());
                    ft.commit();
                }

            }
        });

        lichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showProgressBar(getActivity());
                // Begin the transaction
                FragmentTransaction ft;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fr_menu, new HistoryFragment());
                    ft.commit();
                }
            }
        });

        tonKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showProgressBar(getActivity());
                // Begin the transaction
                FragmentTransaction ft;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fr_menu, new InventoryFragment());
                    ft.commit();
                }
            }
        });

        xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showProgressBar(getActivity());
                // Begin the transaction
                FragmentTransaction ft;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fr_menu, new ConfirmFragment());
                    ft.commit();
                }
            }
        });

        MainActivity.hideProgressBar(getActivity());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        android.support.v7.app.ActionBar actionBar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            actionBar = ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("Menu kiểm kê");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.show();

            MenuItem menuItem_date = menu.findItem(R.id.menuitem_date);
            menuItem_date.setVisible(false);
            MenuItem menuItem_barcode = menu.findItem(R.id.menuitem_barcode);
            menuItem_barcode.setVisible(false);
            MenuItem menuItem_save = menu.findItem(R.id.menuitem_save);
            menuItem_save.setVisible(false);
            MenuItem menuItem_refresh = menu.findItem(R.id.menuitem_refresh);
            menuItem_refresh.setVisible(false);
            MenuItem menuItem_search = menu.findItem(R.id.app_bar_search);
            menuItem_search.setVisible(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
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
