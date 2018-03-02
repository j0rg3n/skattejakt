package com.Android2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.LOCATION;

public class QRScannerActivity extends AppCompatActivity {

    public static final String QR_EXTRA_MESSAGE_KEY = "com.Android2.MESSAGE";

    /* ZXing scan - doesn't work, just a white screen :( */
    private ZXingScannerView mScannerView;

    private static List<BarcodeFormat> desiredFormats = Arrays.asList(
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION,
            BarcodeFormat.QR_CODE
    );

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(desiredFormats);
        // Set the scanner view as the content view
        setContentView(mScannerView);
        //mScannerView.setAutoFocus(true);
    }

    // From https://stackoverflow.com/questions/26261769/w-camerabase-an-error-occurred-while-connecting-to-camera-0-on-camera-open-c
    private static final int REQUEST_GET_ACCOUNT = 112;
    private static final int REQUEST_GET_ACCOUNT2 = 113;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE2 = 201;

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        //ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE2);
        //ActivityCompat.requestPermissions(this, new String[]{LOCATION}, PERMISSION_REQUEST_CODE);
    }

    boolean locationAccepted = false;
    boolean cameraAccepted = false;

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
            case PERMISSION_REQUEST_CODE2:
                if (grantResults.length > 0) {

                    locationAccepted = locationAccepted || (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    cameraAccepted = cameraAccepted || (requestCode == PERMISSION_REQUEST_CODE2 && grantResults[0] == PackageManager.PERMISSION_GRANTED);

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data and camera", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }

                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QRScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    
    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }

        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                handleQRScanResult(result);
            }
        });
        // Start camera on resume
        try {
            //mScannerView.setAutoFocus(false);
        } catch (RuntimeException r) {
            //...
        }
        mScannerView.startCamera(0);
        //mScannerView.startCamera(); //< Auto-select front-facing camera, fallback to any camera.
    }

    @Override
    public void onPause() {
        // Stop camera on pause
        mScannerView.stopCamera();
        super.onPause();
    }

    private void handleQRScanResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results
        //Logger.verbose("result", rawResult.getText());
        // Prints the scan format (qrcode, pdf417 etc.)
        //Logger.verbose("result", rawResult.getBarcodeFormat().toString());
        //If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
        Intent intent = new Intent();
        intent.putExtra(QR_EXTRA_MESSAGE_KEY, rawResult.getText());
        setResult(RESULT_OK, intent);
        finish();
    }
}
