package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


public class StorageReady extends Activity {
    private TextView usuario,caja,compra,serial;
    private EditText cantidad;
    private Button registra;
    private SharedPreferences prefs;
    private int user_id;
    private String id_code_value,id_compra_value,caja_value,serial_value,user_name,responsePost;
    private CheckBox full;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_entrance);

        usuario=(TextView)findViewById(R.id.user);
        compra=(TextView)findViewById(R.id.id_compra_value);
        caja=(TextView)findViewById(R.id.caja_value);
        cantidad=(EditText)findViewById(R.id.amount);
        serial=(TextView)findViewById(R.id.serial_value);
        registra=(Button)findViewById(R.id.postEntrance);
        full = (CheckBox) findViewById(R.id.lleno_delivery);

        final Bundle bundle = getIntent().getExtras();
        id_code_value= Integer.toString(bundle.getInt("code_id"));
        id_compra_value= Integer.toString(bundle.getInt("purchase_id"));
        caja_value = Integer.toString(bundle.getInt("purchase_box"));
        serial_value = bundle.getString("code_value_serial");

        serial.setText(serial_value);
        caja.setText(caja_value);
        compra.setText(id_compra_value);

        prefs = getSharedPreferences("Exporta", Context.MODE_PRIVATE);

        user_id = prefs.getInt("Id", 0);
        user_name = prefs.getString("Empleado", null);
        usuario.setText(user_name);

        registra.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String cadena_amount = cantidad.getText().toString();

                if (TextUtils.isEmpty(cadena_amount)) {
                    cantidad.setError("Debes ingresar un valor adecuado");
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
                    Log.e("Hola", String.valueOf(user_id));
                    int num=Integer.parseInt(cantidad.getText().toString());
                    try {
                        responsePost=new PostStorageReady(StorageReady.this,Integer.parseInt(id_compra_value),Integer.parseInt(id_code_value),serial_value,user_id,num,lleno,vacio).execute().get();
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
        getMenuInflater().inflate(R.menu.storage_entrance, menu);
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
