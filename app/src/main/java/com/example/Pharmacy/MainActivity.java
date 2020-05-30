package com.example.Pharmacy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.example.Pharmacy.BarcodeFragment.REQUEST_CAMERA;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        ListCardFragment.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener,
        BarcodeFragment.OnFragmentInteractionListener,
        RecordCardFragment.OnFragmentInteractionListener,
        SearchResultFragment.OnFragmentInteractionListener {

    public static DataManager dataManager;

    public static void showProgressBar(Activity activity) {
        if (activity == null) return;
        MainActivity.hideKeyboard(activity);
        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(Activity activity) {
        if (activity == null) return;
        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onFragmentInteraction(String code) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            dataManager = new DataManager(getApplicationContext());
        } catch (RuntimeException re) {
            re.printStackTrace();
            Toast.makeText(getApplicationContext(), "Lỗi!!! Không thể truy cập cơ sở dữ liệu.", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.show();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fr_main, new LoginFragment());
        ft.commit();

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataManager.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_CAMERA || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }
}
