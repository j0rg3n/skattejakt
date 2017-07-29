package com.Android2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                handleQRScanResult(result);
            }
        });
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
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
