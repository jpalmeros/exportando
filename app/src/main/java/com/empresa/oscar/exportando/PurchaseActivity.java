package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.empresa.oscar.exportando.adapter.purchaseAdapter;
import com.empresa.oscar.exportando.get.GetPurchases;
import com.empresa.oscar.exportando.object.Purchase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class PurchaseActivity extends Activity {
    private ArrayList<Purchase> listPurchases;
    private ListView listaCompras;
    private String usr,pass;
    private int user_id;
    private purchaseAdapter adaptadorCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        listaCompras=(ListView)findViewById(R.id.compras);

        SharedPreferences prefs = getSharedPreferences("Exporta", Activity.MODE_PRIVATE);
        usr=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        user_id= prefs.getInt("Id",0);

        try {
            listPurchases=new GetPurchases(this,usr,pass,user_id).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adaptadorCompras=new purchaseAdapter(PurchaseActivity.this,listPurchases);
        listaCompras.setAdapter(adaptadorCompras);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.purchase, menu);
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
