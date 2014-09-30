package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class LeerQR_prodcuto extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Do something with the result here
        //separamos cadena obtenida

            String scanContent = rawResult.getContents();

            //separamos cadena obtenida
            String tmp[]=scanContent.split(" ");
            String code_id=tmp[0];
            String purchase_id=tmp[1];
            String purchase_box=tmp[2];
            String purchase_date=tmp[3];
            Log.d("QR", purchase_id + " - " + purchase_box);
            //logueando
            Intent StorageEntrance = new Intent(this,StorageEntrance.class);
            StorageEntrance.putExtra("code_id",Integer.parseInt(code_id));
            StorageEntrance.putExtra("purchase_id",Integer.parseInt(purchase_id));
            StorageEntrance.putExtra("purchase_box",Integer.parseInt(purchase_box));
            StorageEntrance.putExtra("code_value_serial",scanContent);

            startActivity(StorageEntrance);

            //new Login(this,usr,pass).execute();

    }
}
