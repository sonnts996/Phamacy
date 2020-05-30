package com.example.Pharmacy;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xu.database.XCursor;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String resultType = "SEARCH_LIST";

    private View searchResult;
    private LinearLayout pane;
    private OnActionListener mAction;
    private OnDataChangeListener mData;
    private OnReadyListener mReady;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resultType Parameter 1.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String resultType) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, resultType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resultType = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchResult = inflater.inflate(R.layout.fragment_search_result, container, false);
        pane = searchResult.findViewById(R.id.search_list_card);
        TextView msg = searchResult.findViewById(R.id.search_view_msg);
        Button addNew = searchResult.findViewById(R.id.search_button_new);
        Button cancel = searchResult.findViewById(R.id.search_button_cancel);
        if (resultType.equals(ResultType.SEARCH_LIST.toString())) {
            msg.setText(R.string.msg_ket_qua_tim_kiem);
            addNew.setClickable(false);
            addNew.setEnabled(false);
        } else if (resultType.equals(ResultType.DUPLICATE_LIST.toString())) {
            msg.setText(R.string.msg_phieu_trung);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                }
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAction.onActionListener(ButtonAction.CREATE, null, -1);
            }
        });

        mData = new OnDataChangeListener() {
            @Override
            public void onDataChange(XCursor data) {
                addCard(data);
            }
        };

        mReady.onReadyListener();

        MainActivity.hideProgressBar(getActivity());
        return searchResult;
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
    public void onDismiss(DialogInterface dialog) {
        getFragmentManager().popBackStackImmediate();
        super.onDismiss(dialog);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnActionListener(OnActionListener actionListener) {
        mAction = actionListener;
    }

    public void addCard(final XCursor cursor) {
        if (resultType.equals(ResultType.DUPLICATE_LIST.toString())) {

            cursor.moveToFirstLine();
            while (!cursor.isAfterLastLine()) {
                View card = getLayoutInflater().inflate(R.layout.duplicate_card, this.pane, false);
                this.pane.addView(card);
                final int pos = cursor.getLineIndex();

                Button update = card.findViewById(R.id.search_button_update);
                Button view = card.findViewById(R.id.search_button_view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAction.onActionListener(ButtonAction.VIEW, cursor, pos);
                    }
                });
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAction.onActionListener(ButtonAction.UPDATE, cursor, pos);
                    }
                });

                ((TextView) card.findViewById(R.id.search_view_cid)).setText(cursor.getLine().getString(TableColumn.ChecklistTable.CID));
                ((TextView) card.findViewById(R.id.search_view_soluong)).setText(cursor.getLine().getString(TableColumn.ChecklistTable.REAL));
                ((TextView) card.findViewById(R.id.search_view_name)).setText(cursor.getLine().getString(TableColumn.ProductTable.NAME));
                ((TextView) card.findViewById(R.id.search_view_date)).setText(cursor.getLine().getString(TableColumn.ChecklistTable.DATE));
                ((TextView) card.findViewById(R.id.search_view_kho)).setText(cursor.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE));

                cursor.moveToNextLine();
            }
        } else if (resultType.equals(ResultType.SEARCH_LIST.toString())) {

            cursor.moveToFirstLine();
            while (!cursor.isAfterLastLine()) {
                View card = getLayoutInflater().inflate(R.layout.search_card, this.pane, false);
                this.pane.addView(card);
                final int pos = cursor.getLineIndex();

                Button view = card.findViewById(R.id.search_button_view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAction.onActionListener(ButtonAction.VIEW, cursor, pos);
                    }
                });

                ((TextView) card.findViewById(R.id.search_view_cid)).setText(cursor.getLine().getString(TableColumn.ChecklistTable.PID));
                ((TextView) card.findViewById(R.id.search_view_soluong)).setText(cursor.getLine().getString(TableColumn.ProductInStockTable.TOTAL));
                ((TextView) card.findViewById(R.id.search_view_name)).setText(cursor.getLine().getString(TableColumn.ProductTable.NAME));
                ((TextView) card.findViewById(R.id.search_view_kho)).setText(cursor.getLine().getString(TableColumn.WarehouseTable.WAREHOUSE));

                cursor.moveToNextLine();
            }
        } else {
            throw new RuntimeException("Undefine result type");
        }
    }

    void setData(XCursor data){
        mData.onDataChange(data);
    }

    void setOnReadyListener(OnReadyListener readyListener){
        mReady = readyListener;
    }

    enum ResultType {
        SEARCH_LIST,
        DUPLICATE_LIST
    }

    enum ButtonAction {
        UPDATE,
        VIEW,
        CANCEL,
        CREATE
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

    public interface OnActionListener {
        void onActionListener(ButtonAction buttonAction, XCursor cursor, int position);
    }

    public interface OnDataChangeListener {
        void onDataChange(XCursor data);
    }

    public interface OnReadyListener{
        void onReadyListener();
    }
}
