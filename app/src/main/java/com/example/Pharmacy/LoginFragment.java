package com.example.Pharmacy;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private DataManager dataManager;

    public LoginFragment() {
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
        final View view = inflater.inflate(R.layout.fr_login, container, false);
        setHasOptionsMenu(true);

        final Button button = view.findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showProgressBar(getActivity());
                view.setPadding(0, 8, 0, 0);

                EditText edit_loginUser = view.findViewById(R.id.edit_loginuser);
                EditText edit_loginPassword = view.findViewById(R.id.edit_loginpassword);
                Log.i("USERNAME", edit_loginUser.getText().toString());
                Log.i("PASSWORD", edit_loginPassword.getText().toString());

                dataManager.login(edit_loginUser.getText().toString(), edit_loginPassword.getText().toString());

                if (dataManager.checkLogin()) {
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "CHANNEL_ID")
//                            .setSmallIcon(R.mipmap.ic_launcher_round)
//                            .setContentTitle("Pharmacy")
//                            .setContentText(edit_loginUser.getText().toString() + " login")
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify(1, builder.build());

                    MainActivity.hideKeyboard(getContext());
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.fr_login, new MenuFragment());
                    ft.commit();
                } else {
                    Toast.makeText(getActivity(), "Tên đăng nhập hoặc mật khẩu không chính xác.", Toast.LENGTH_LONG).show();
                    MainActivity.hideProgressBar(getActivity());
                    view.setPadding(0, 0, 0, 0);
                }
            }
        });

        TextView password = view.findViewById(R.id.edit_loginpassword);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    button.callOnClick();
                }
                return false;
            }
        });

        MainActivity.hideProgressBar(getActivity());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        android.support.v7.app.ActionBar actionBar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            actionBar = ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
            assert actionBar != null;
            actionBar.hide();
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
