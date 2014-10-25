package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class DealerActivity extends Activity {
    private Button entrega_button,recepcion_button;
    private String nick,pass,type;
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

        entrega_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QR_producto = new Intent(DealerActivity.this, LeerQR_prodcuto.class);
                QR_producto.putExtra("nick",nick);
                QR_producto.putExtra("pass",pass);
                QR_producto.putExtra("type",type);
                QR_producto.putExtra("process","entrega");
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(QR_producto);

            }
        });

        recepcion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QR_producto = new Intent(DealerActivity.this, LeerQR_prodcuto.class);
                QR_producto.putExtra("nick",nick);
                QR_producto.putExtra("pass",pass);
                QR_producto.putExtra("type",type);
                QR_producto.putExtra("process","recepcion");
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(QR_producto);
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
}
