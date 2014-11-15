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
import android.widget.ListView;
import android.widget.TextView;

import com.empresa.oscar.exportando.adapter.orderCodeAdapter;
import com.empresa.oscar.exportando.object.OrderCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class OrderCodeActivity extends Activity implements ZBarScannerView.ResultHandler{
    int amount;
    String orderString;
    JSONArray ordenArray;
    ArrayList<OrderCode> listOrderCode;
    orderCodeAdapter adapertOrderCode;
    ListView listViewOrderCode;
    Button cancelOrder,addOrderCode;
    SharedPreferences prefs;
    int shell,pañal,etiquetillas;
    private ZBarScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_code);

        final Bundle bundle = getIntent().getExtras();
        amount= bundle.getInt("amount");
        prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        orderString=prefs.getString("orderString","");
        Log.e("order code", orderString);
        pañal=0;
        shell=0;
        etiquetillas=0;

        listOrderCode=new ArrayList<OrderCode>();

        cancelOrder=(Button)findViewById(R.id.cancelar_orden);
        addOrderCode=(Button)findViewById(R.id.agregar_producto);
        listViewOrderCode=(ListView)findViewById(R.id.ordercodes);
        mScannerView = new ZBarScannerView(OrderCodeActivity.this);

        /*try {
            ordenArray=new JSONArray(orderString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<ordenArray.length();i++){
            try {
                JSONObject code= ordenArray.getJSONObject(i);
                OrderCode actualorderCode=new OrderCode(code.getInt("codeOrderId"),code.getInt("codeId"),code.getString("codeSerial"),
                        code.getInt("productId"),code.getInt("productType"),code.getString("productName"),
                        code.getInt("codeAmount"));
                listOrderCode.add(actualorderCode);
                int lastCodeOrderId=code.getInt("codeOrderId")+1;
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
        */
        Log.e("Orden Actual","Shell ="+Integer.toString(shell)+", Pañal="+Integer.toString(pañal)+", Etiquetillas"+Integer.toString(etiquetillas));

        //adapertOrderCode=new orderCodeAdapter(OrderCodeActivity.this,listOrderCode);
        //listViewOrderCode.setAdapter(adapertOrderCode);

        addOrderCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
            }
        });
    }
    @Override
    public void handleResult(Result result) {
        OrderCode newOrderCode=null;
        String scanContent = result.getContents();
        Log.e("Codigo",scanContent);
        try {
            JSONArray productCode = new JSONArray(scanContent);
            for(int i=0;i<productCode.length();i++){
                try {
                    JSONObject code= productCode.getJSONObject(i);
                    newOrderCode=new OrderCode(0,code.getInt("code_id"),
                            code.getString("code_serial_value"),code.getInt("product_id"),
                            code.getInt("product_type_id"),code.getString("product_name"),
                            code.getInt("product_amount_remains"));
                    listOrderCode.add(newOrderCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(OrderCodeActivity.this);

        LayoutInflater inflater = OrderCodeActivity.this.getLayoutInflater();
        final View dialogView =inflater.inflate(R.layout.order_dialog, null);
        TextView serial = (TextView) dialogView.findViewById(R.id.SerialValue);
        TextView producto = (TextView) dialogView.findViewById(R.id.ProductoValue);
        TextView disponibles = (TextView) dialogView.findViewById(R.id.DisponiblesValue);
        serial.setText(newOrderCode.codeSerial);
        producto.setText(newOrderCode.productName);
        disponibles.setText(newOrderCode.amount);
        builder.setTitle(R.string.CrearOrden);
        builder.setView(dialogView)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText inputAmount = (EditText) dialogView.findViewById(R.id.order_amount);
                        int amount=Integer.parseInt(inputAmount.getText().toString());
                        Log.e("Cantidad de Producto",Integer.toString(amount));

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.e("Cantidad de Producto","Cancelada");
                    }
                });
        builder.create();
        builder.show();

        orderString=prefs.getString("Product_"+newOrderCode.codeSerial,"");


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

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(OrderCodeActivity.this); // Register ourselves as a handler for scan results.
        if(mScannerView.isActivated()){
            mScannerView.stopCamera();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.setResultHandler(OrderCodeActivity.this); // Register ourselves as a handler for scan results.
        if(mScannerView.isActivated()){
            mScannerView.stopCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.setResultHandler(OrderCodeActivity.this); // Register ourselves as a handler for scan results.
        if(mScannerView.isActivated()){
            mScannerView.stopCamera();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.setResultHandler(OrderCodeActivity.this); // Register ourselves as a handler for scan results.
        if(mScannerView.isActivated()){
            mScannerView.stopCamera();
        }
    }
}
