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


public class StorageActivity extends Activity implements ZBarScannerView.ResultHandler {
    private Button entrega_button,recepcion_button,compra_button;
    private String nick,pass,type;
    private ZBarScannerView mScannerView;
    private boolean escaneando,delivery,reception;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        delivery=false;
        reception=false;
        entrega_button=(Button)findViewById(R.id.entrega);
        recepcion_button=(Button)findViewById(R.id.recepcion);
        compra_button=(Button)findViewById(R.id.compra);
        mScannerView = new ZBarScannerView(this);

        SharedPreferences prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        nick=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        type=prefs.getString("Type",null);

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

        compra_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent compra_producto = new Intent(StorageActivity.this, PurchaseActivity.class);
                compra_producto.putExtra("nick",nick);
                compra_producto.putExtra("pass",pass);
                compra_producto.putExtra("type",type);
                compra_producto.putExtra("process","compra");
                compra_producto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                compra_producto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(compra_producto);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.storage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initialize_view(){
        setContentView(R.layout.activity_storage);
        entrega_button=(Button)findViewById(R.id.entrega);
        recepcion_button=(Button)findViewById(R.id.recepcion);
        compra_button=(Button)findViewById(R.id.compra);

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

        compra_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent compra_producto = new Intent(StorageActivity.this, PurchaseActivity.class);
                compra_producto.putExtra("nick",nick);
                compra_producto.putExtra("pass",pass);
                compra_producto.putExtra("type",type);
                compra_producto.putExtra("process","compra");
                compra_producto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                compra_producto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(compra_producto);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(StorageActivity.this); // Register ourselves as a handler for scan results.
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
