package com.example.Pharmacy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BarcodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BarcodeFragment extends DialogFragment {
    public static final int REQUEST_CAMERA = 1;
    private SurfaceView mCameraPreview;
    private CameraSource mCameraSource;
    private OnBarCodeDetect onBarCodeDetect;

    public BarcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fr_dialog, container, false);
        mCameraPreview = view.findViewById(R.id.camera_view);

        final BarcodeDetector mBarcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mCameraSource = new CameraSource.Builder(Objects.requireNonNull(getContext()), mBarcodeDetector).setFacing(
                    CameraSource.CAMERA_FACING_BACK)
                    .setRequestedFps(35.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            mCameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mCameraSource.start(mCameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    mCameraSource.stop();
                }
            });

            mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes != null && barcodes.size() > 0) {
                        BarcodeFragment.this.dismiss();
                        onBarCodeDetect.onBarCodeDetect(barcodes.valueAt(0).displayValue);
                        Log.i("BARCODE", barcodes.valueAt(0).displayValue);
                    }
                }
            });
            MainActivity.hideProgressBar(getActivity());
        }


        return view;
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
        void onFragmentInteraction(String code);
    }

    public void setOnBarCodeDetect(OnBarCodeDetect onBarCodeDetect) {
        this.onBarCodeDetect = onBarCodeDetect;
    }

    public interface OnBarCodeDetect{
        void onBarCodeDetect(String code);
    }
}
