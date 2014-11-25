package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class DealerActivity extends Activity implements ZBarScannerView.ResultHandler {
    private Button entrega_button,recepcion_button;
    private String nick,pass,type;
    private ZBarScannerView mScannerView;
    private boolean escaneando,delivery,reception;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer);
        entrega_button=(Button)findViewById(R.id.entrega);
        recepcion_button=(Button)findViewById(R.id.recepcion);
        SharedPreferences prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        nick=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        type=prefs.getString("Type",null);
        mScannerView = new ZBarScannerView(this);
        delivery=false;
        reception=false;

        entrega_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
                escaneando = true;
                delivery=true;
                reception=false;
            }
        });

        recepcion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
                escaneando = true;
                delivery=false;
                reception=true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.storage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize_view(){
        setContentView(R.layout.activity_dealer);
        entrega_button=(Button)findViewById(R.id.entrega);
        recepcion_button=(Button)findViewById(R.id.recepcion);

        entrega_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
                escaneando = true;
                delivery=true;
                reception=false;
            }
        });

        recepcion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
                escaneando = true;
                delivery=false;
                reception=true;
            }
        });
    }

    @Override
    public void handleResult(Result result) {
        mScannerView.stopCamera();
        escaneando = false;
        String scanContent = result.getContents();
        String[] tmp = scanContent.split(" ");
        Log.e("Codigo", scanContent);
        Log.e("Codigo", tmp[0]);
        initialize_view();

        if(delivery){
            Intent  Delivery= new Intent(this,ProductDelivery.class);
            Delivery.putExtra("code_value_serial",scanContent);
            Delivery.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Delivery.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Delivery);
        }
        if (reception){
            Intent  Delivery= new Intent(this,ProductReception.class);
            Delivery.putExtra("code_value_serial",scanContent);
            Delivery.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Delivery.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Delivery);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(DealerActivity.this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.stopCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {

        if (escaneando) {
            delivery=false;
            reception=false;
            initialize_view();
        } else {
            super.onBackPressed();
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.stopCamera();

        }
    }
}
