package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class LeerQR_prodcuto extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private String nick,pass,type,process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getIntent().getExtras();
        nick= bundle.getString("nick");
        pass= bundle.getString("pass");
        type = bundle.getString("type");
        process = bundle.getString("process");

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

            if(process.equals("entrance")){
            Intent StorageEntrance = new Intent(this,StorageEntrance.class);
            StorageEntrance.putExtra("code_id",Integer.parseInt(code_id));
            StorageEntrance.putExtra("purchase_id",Integer.parseInt(purchase_id));
            StorageEntrance.putExtra("purchase_box",Integer.parseInt(purchase_box));
            StorageEntrance.putExtra("code_value_serial",scanContent);
            startActivity(StorageEntrance);
            }
            if(process.equals("ready")){
                Intent StorageEntrance = new Intent(this,StorageEntrance.class);
                StorageEntrance.putExtra("code_id",Integer.parseInt(code_id));
                StorageEntrance.putExtra("purchase_id",Integer.parseInt(purchase_id));
                StorageEntrance.putExtra("purchase_box",Integer.parseInt(purchase_box));
                StorageEntrance.putExtra("code_value_serial",scanContent);
                startActivity(StorageEntrance);
            }
            if(process.equals("entrega")){
                Intent  Delivery= new Intent(this,ProductDelivery.class);
                Delivery.putExtra("code_id",Integer.parseInt(code_id));
                Delivery.putExtra("purchase_id",Integer.parseInt(purchase_id));
                Delivery.putExtra("purchase_box",Integer.parseInt(purchase_box));
                Delivery.putExtra("code_value_serial",scanContent);
                startActivity(Delivery);
            }
            if(process.equals("recepcion")){
                Intent  Reception= new Intent(this,ProductReception.class);
                Reception.putExtra("code_id",Integer.parseInt(code_id));
                Reception.putExtra("purchase_id",Integer.parseInt(purchase_id));
                Reception.putExtra("purchase_box",Integer.parseInt(purchase_box));
                Reception.putExtra("code_value_serial",scanContent);
                startActivity(Reception);
            }
            //new Login(this,usr,pass).execute();

    }
}
