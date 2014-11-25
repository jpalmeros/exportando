package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.empresa.oscar.exportando.adapter.locacionAdapter;
import com.empresa.oscar.exportando.get.GetLocations;
import com.empresa.oscar.exportando.object.Locacion;
import com.empresa.oscar.exportando.post.PostStorageDelivery;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ProductDelivery extends Activity {
    private ArrayList<Locacion> ListaLocaciones;
    private int user_id,id_compra_value,id_code_value;
    private TextView token_value;
    private String code_value_serial,pass,usr,responsePost;
    private locacionAdapter locAdapter;
    private Spinner locSpinner;
    private Button boton_recepcion;
    private EditText texto_amount;
    private CheckBox full;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delivery);
        token_value=(TextView)findViewById(R.id.order_token_value);
        boton_recepcion=(Button)findViewById(R.id.btn_recepcion_delivery);
        texto_amount=(EditText)findViewById(R.id.amount_delivery);
        full = (CheckBox) findViewById(R.id.lleno_delivery);
        final Bundle bundle = getIntent().getExtras();
        code_value_serial = bundle.getString("code_value_serial");
        Log.e("Token de orden",code_value_serial);
        SharedPreferences prefs = getSharedPreferences("Exporta", Activity.MODE_PRIVATE);
        usr=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        user_id= prefs.getInt("Id",0);

        token_value.setText(code_value_serial);

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

                if (TextUtils.isEmpty(cadena_amount)) {
                    texto_amount.setError("Debes ingresar un valor adecuado");
                    return;
                } else {
                    int lleno,vacio;
                    Log.e("checkbox ischecked", Boolean.toString(full.isChecked()));
                    if (full.isChecked()) {
                     lleno=1;
                     vacio=0;
                    }
                    else{
                        lleno=0;
                        vacio=1;
                    }
                    Locacion loc = (Locacion) locSpinner.getSelectedItem();
                    int int_amount = Integer.parseInt(cadena_amount);
                    try {
                        responsePost=new PostStorageDelivery(ProductDelivery.this,code_value_serial, user_id, int_amount, loc.getIndiceLocacion(),lleno,vacio).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(responsePost.equals("exito")){
                        finish();
                    }
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
