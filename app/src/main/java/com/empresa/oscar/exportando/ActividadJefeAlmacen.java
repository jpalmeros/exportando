package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ActividadJefeAlmacen extends Activity {
    private static final String TAG = "QR Scanner";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_jefe_almacen);
        readQR();

    }

    private void readQR() {
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String scanContent = scanResult.getContents();

            //separamos cadena obtenida
            String tmp[]=scanContent.split(" ");
            String code_id=tmp[0];
            String purchase_id=tmp[1];
            String purchase_box=tmp[2];
            String purchase_date=tmp[3];
            Log.d(TAG, "QR Scan :" + purchase_id + " - " + purchase_box);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actividad_jefe_almacen, menu);
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
}
