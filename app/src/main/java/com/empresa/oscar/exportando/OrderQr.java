package com.empresa.oscar.exportando;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.empresa.oscar.exportando.get.GetProduct;
import com.empresa.oscar.exportando.object.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by lord on 05/11/2014.
 */
public class OrderQr extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private String nick,pass,type,process,orderString;
    private int amount,id;
    JSONArray ordenArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        nick=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        type=prefs.getString("Type",null);
        id=prefs.getInt("Id",0);
        orderString=prefs.getString("orderString","");
        Log.e("order code",orderString);
        int pañal=0;
        int shell=0;
        int etiquetillas=0;

        try {
            ordenArray=new JSONArray(orderString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<ordenArray.length();i++){
            try {
                JSONObject code= ordenArray.getJSONObject(i);
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

        final Bundle bundle = getIntent().getExtras();
        amount= bundle.getInt("amount");
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editpref=prefs.edit();
        editpref.putString("orderString","");
        editpref.commit();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        //separamos cadena obtenida

        String scanContent = rawResult.getContents();
        //separamos cadena obtenida
        String tmp[]=scanContent.split(" ");
        final String code_id=tmp[0];
        String purchase_id=tmp[1];
        String purchase_box=tmp[2];
        String purchase_date=tmp[3];
        Log.d("QR", purchase_id + " - " + purchase_box);

        try {
            final Product producto =new GetProduct(OrderQr.this,nick,pass,Integer.parseInt(type),Integer.parseInt(code_id)).execute().get();
            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderQr.this);
            LayoutInflater inflater = OrderQr.this.getLayoutInflater();
            final View dialogView =inflater.inflate(R.layout.order_dialog, null);
            builder.setTitle(producto.getProductName());
            builder.setView(dialogView)

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int position) {
                            EditText inputAmount = (EditText) dialogView.findViewById(R.id.order_amount);
                            inputAmount.setFilters(new InputFilter[]{new InputFilterMinMax(1,producto.getProductAmount())});
                            int amount = Integer.parseInt(inputAmount.getText().toString());
                            JSONObject orderCode=new JSONObject();
                            try {
                                orderCode.put("code", code_id);
                                orderCode.put("amount",amount);
                                orderCode.put("tipo",producto.getProductTypeId());
                                orderCode.put("user",id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ordenArray.put(orderCode);
                            SharedPreferences prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editpref=prefs.edit();
                            editpref.putString("orderString",ordenArray.toString());
                            editpref.commit();

                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.e("cantidad de Orden", "Cancelada");
                        }
                    });
            builder.create();
            builder.show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


}