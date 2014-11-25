package com.empresa.oscar.exportando;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class StorageBossActivity extends Activity {
    private Button entrance_button,ready_button,order_button,loss_button;
    private String nick,pass,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance_and_ready);

        entrance_button=(Button)findViewById(R.id.primera_entrada);
        ready_button=(Button)findViewById(R.id.producto_listo);
        order_button=(Button)findViewById(R.id.crear_orden);
        loss_button=(Button)findViewById(R.id.perdidas);

        SharedPreferences prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        nick=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        type=prefs.getString("Type",null);

        loss_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Mermas = new Intent(StorageBossActivity.this, LossActivity.class);
                Mermas.putExtra("nick",nick);
                Mermas.putExtra("pass",pass);
                Mermas.putExtra("type",type);
                Mermas.putExtra("process","entrance");
                Mermas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Mermas.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Mermas);
            }
        });

        entrance_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QR_producto = new Intent(StorageBossActivity.this, LeerQR_prodcuto.class);
                QR_producto.putExtra("nick",nick);
                QR_producto.putExtra("pass",pass);
                QR_producto.putExtra("type",type);
                QR_producto.putExtra("process","entrance");
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(QR_producto);

            }
        });

        ready_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QR_producto = new Intent(StorageBossActivity.this, LeerQR_prodcuto.class);
                QR_producto.putExtra("nick",nick);
                QR_producto.putExtra("pass",pass);
                QR_producto.putExtra("type",type);
                QR_producto.putExtra("process","ready");
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                QR_producto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(QR_producto);
            }
        });

        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(StorageBossActivity.this);

                LayoutInflater inflater = StorageBossActivity.this.getLayoutInflater();
                final View dialogView =inflater.inflate(R.layout.order_dialog, null);
                builder.setTitle(R.string.CrearOrden);
                builder.setView(dialogView)

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText inputAmount = (EditText) dialogView.findViewById(R.id.order_amount);
                                int amount=Integer.parseInt(inputAmount.getText().toString());
                                Log.e("cantidad de Orden",Integer.toString(amount));
                                Intent OrderCode = new Intent(StorageBossActivity.this, OrderCodeActivity.class);
                                OrderCode.putExtra("amount",amount);
                                OrderCode.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                OrderCode.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                StorageBossActivity.this.startActivity(OrderCode);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.e("cantidad de Orden","Cancelada");
                            }
                        });
                builder.create();
                builder.show();
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
}
