package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ProductReception extends Activity {
    private ArrayList<Locacion> ListaLocaciones;
    private int user_id,id_compra_value,caja_value,id_code_value;
    private TextView compra,caja;
    private locacionAdapter locAdapter;
    private Spinner locSpinner;
    private Button boton_recepcion;
    private EditText texto_amount;
    private String code_value_serial,usr,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delivery);
        compra=(TextView)findViewById(R.id.id_compra_value);
        caja=(TextView)findViewById(R.id.caja_value);
        boton_recepcion=(Button)findViewById(R.id.recepcion);
        texto_amount=(EditText)findViewById(R.id.amount);

        final Bundle bundle = getIntent().getExtras();
        id_code_value= bundle.getInt("code_id");
        id_compra_value= bundle.getInt("purchase_id");
        caja_value = bundle.getInt("purchase_box");
        code_value_serial = bundle.getString("code_value_serial");

        SharedPreferences prefs = getSharedPreferences("Exporta", Activity.MODE_PRIVATE);
        usr=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        user_id=prefs.getInt("Id",0);

        caja.setText(caja_value);
        compra.setText(id_compra_value);

        try {
            ListaLocaciones=new GetLocations(this,usr,pass,user_id).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        locAdapter = new locacionAdapter(this, ListaLocaciones);
        locSpinner = (Spinner) findViewById(R.id.spinner1);
        locSpinner.setAdapter(locAdapter);

        boton_recepcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cadena_amount = texto_amount.getText().toString();

                if(TextUtils.isEmpty(cadena_amount)){
                    texto_amount.setError("Debes ingresar un valor adecuado");
                    return;
                }
                else{
                    Locacion loc=(Locacion)locSpinner.getSelectedItem();
                    int int_amount=Integer.parseInt(cadena_amount);
                    new PostStorageDelivery(ProductReception.this,id_compra_value,id_code_value,code_value_serial,user_id,int_amount,loc.getIndiceLocacion()).execute();
                }
            }
        });
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
