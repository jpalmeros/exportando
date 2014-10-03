package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ProductReception extends Activity {
    private ArrayList<Locacion> ListaLocaciones;
    private String id_compra_value,caja_value,id_code_value;
    private TextView compra,caja;
    private locacionAdapter locAdapter;
    private Spinner locSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delivery);
        compra=(TextView)findViewById(R.id.id_compra_value);
        caja=(TextView)findViewById(R.id.caja_value);

        final Bundle bundle = getIntent().getExtras();
        id_code_value= Integer.toString(bundle.getInt("code_id"));
        id_compra_value= Integer.toString(bundle.getInt("purchase_id"));
        caja_value = Integer.toString(bundle.getInt("purchase_box"));


        SharedPreferences prefs = getSharedPreferences("Exporta", Activity.MODE_PRIVATE);
        String usr=prefs.getString("Empleado",null);
        String pass=prefs.getString("Password",null);

        caja.setText(caja_value);
        compra.setText(id_compra_value);

        try {
            ListaLocaciones=new GetLocations(this,usr,pass).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        locAdapter = new locacionAdapter(this, ListaLocaciones);
        locSpinner = (Spinner) findViewById(R.id.spinner1);
        locSpinner.setAdapter(locAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_delivery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
