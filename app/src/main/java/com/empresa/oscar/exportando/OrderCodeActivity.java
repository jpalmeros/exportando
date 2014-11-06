package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrderCodeActivity extends Activity {
    int amount;
    String orderString;
    JSONArray ordenArray;
    ArrayList<OrderCode> listOrderCode;
    orderCodeAdapter adapertOrderCode;
    ListView listViewOrderCode;
    Button deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_code);

        final Bundle bundle = getIntent().getExtras();
        amount= bundle.getInt("amount");
        SharedPreferences prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        orderString=prefs.getString("orderString","");
        Log.e("order code", orderString);
        int pañal=0;
        int shell=0;
        int etiquetillas=0;

        deleteButton=(Button)findViewById(R.id.borrar);
        listViewOrderCode=(ListView)findViewById(R.id.ordercodes);


        try {
            ordenArray=new JSONArray(orderString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<ordenArray.length();i++){
            try {
                JSONObject code= ordenArray.getJSONObject(i);
                OrderCode actualorderCode=new OrderCode(code.getInt("codeId"),code.getString("codeSerial"),
                        code.getInt("productId"),code.getInt("productType"),code.getString("productName"),
                        code.getInt("codeAmount"));
                listOrderCode.add(actualorderCode);
                int type=code.getInt("tipo");
                switch (type){
                    case 1:
                        shell++;
                        break;
                    case 2:
                        etiquetillas++;
                        break;
                    case 3:
                        pañal++;
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("Orden Actual","Shell ="+Integer.toString(shell)+", Pañal="+Integer.toString(pañal)+", Etiquetillas"+Integer.toString(etiquetillas));

        adapertOrderCode=new orderCodeAdapter(OrderCodeActivity.this,listOrderCode);
        listViewOrderCode.setAdapter(adapertOrderCode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_code, menu);
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
