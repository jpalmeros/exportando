package com.empresa.oscar.exportando;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class LeerQR_login extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private ProgressDialog progressDialog;
    private Activity context;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        super.onPause();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Autenificando, espere por favor...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        // Do something with the result here
        //separamos cadena obtenida
        try {
            String tmp[]=rawResult.getContents().split(":");
            String usr=tmp[0];
            String pass=tmp[1];
            Log.d("QR", "QR Scan :" + usr+" - "+ pass);
            //logueando
            new Login(this,usr,pass).execute();
            progressDialog.hide();
            //  Log.v("QR", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
        }catch (ArrayIndexOutOfBoundsException e){
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
