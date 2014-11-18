package com.empresa.oscar.exportando;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.empresa.oscar.exportando.get.GetProduct;
import com.empresa.oscar.exportando.object.OrderCode;
import com.empresa.oscar.exportando.object.Product;
import com.empresa.oscar.exportando.post.PostOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;



public class OrderCodeActivity extends Activity implements ZBarScannerView.ResultHandler{
    public static int orderAmount;
    String orderString,usr,pass;
    JSONArray ordenArray;
    ArrayList<OrderCode> listOrderCode;
    orderCodeAdapter adapertOrderCode;
    ListView listViewOrderCode;
    public static Button cancelOrder,addOrderCode,confirmOrder;
    SharedPreferences prefs;
    Product product;
    public static int shell,pañal,etiquetillas;
    int user_id;
    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_code);

        final Bundle bundle = getIntent().getExtras();
        orderAmount= bundle.getInt("amount");
        prefs = getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        orderString=prefs.getString("orderString","");
        usr=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        user_id= prefs.getInt("Id",0);
        Log.e("order code", orderString);
        pañal=0;
        shell=0;
        etiquetillas=0;

        listOrderCode=new ArrayList<OrderCode>();

        cancelOrder=(Button)findViewById(R.id.cancelar_orden);
        confirmOrder=(Button)findViewById(R.id.confirmar_orden);
        addOrderCode=(Button)findViewById(R.id.agregar_producto);
        listViewOrderCode=(ListView)findViewById(R.id.ordercodes);
        confirmOrder.setEnabled(false);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view

        Log.e("Orden Actual","Shell ="+Integer.toString(shell)+", Pañal="+Integer.toString(pañal)+", Etiquetillas"+Integer.toString(etiquetillas));

        addOrderCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
            }
        });
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray postOrder= new JSONArray();
                JSONObject postOrderAmount=new JSONObject();
                try {
                    postOrderAmount.put("order_amount",Integer.toString(orderAmount));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray orderCodes = new JSONArray();
                for (OrderCode ordercode : listOrderCode){
                    JSONObject ob=new JSONObject();
                    try {
                        ob.put("code_id",ordercode.codeId);
                        ob.put("product_id",ordercode.productId);
                        ob.put("product_type_id",ordercode.productType);
                        ob.put("product_amount",ordercode.amount);
                        ob.put("code_value_serial",ordercode.codeSerial);
                        ob.put("product_name",ordercode.productName);
                        orderCodes.put(ob);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                postOrder.put(postOrderAmount);
                postOrder.put(orderCodes);
                Log.e("order codes a registrar",postOrder.toString());
                new PostOrder(OrderCodeActivity.this,postOrder).execute();
            }
        });
    }


    @Override
    public void handleResult(Result result) {
        mScannerView.stopCamera();
        //
        String scanContent = result.getContents();
        String []tmp=scanContent.split(" ");
        Log.e("Codigo",scanContent);
        Log.e("Codigo",tmp[0]);
        try {
            product=new GetProduct(OrderCodeActivity.this,usr,pass,user_id,Integer.parseInt(tmp[0])).execute().get();

            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderCodeActivity.this);
            LayoutInflater inflater = OrderCodeActivity.this.getLayoutInflater();
            final View dialogView =inflater.inflate(R.layout.product_dialog, null);
            TextView serial = (TextView) dialogView.findViewById(R.id.SerialValue);
            final TextView producto = (TextView) dialogView.findViewById(R.id.ProductoValue);
            TextView disponibles = (TextView) dialogView.findViewById(R.id.DisponiblesValue);
            producto.setText(product.getProductName());
            disponibles.setText(Integer.toString(product.getProductAmount()));
            serial.setText(product.getCodeValueSerial());
            builder.setTitle(R.string.CrearOrden);
            builder.setView(dialogView)

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText inputAmount = (EditText) dialogView.findViewById(R.id.order_amount);
                            int amount=Integer.parseInt(inputAmount.getText().toString());
                            Log.e("Cantidad de Producto",Integer.toString(amount));
                            OrderCode actualorderCode=new OrderCode(0,product.getCodeId(),product.getCodeValueSerial(),
                                    product.getProductId(),product.getProductTypeId(),product.getProductName(),
                                    amount);
                            listOrderCode.add(actualorderCode);
                            int type=actualorderCode.productType;
                            switch (type){
                                case 1:
                                    shell+=amount;
                                    break;
                                case 2:
                                    etiquetillas+=amount;
                                    break;
                                case 3:
                                    pañal+=amount;
                                    break;
                            }
                            initialize_view();
                            if((shell&etiquetillas&pañal)==orderAmount){

                                confirmOrder.setEnabled(true);
                            }
                            else {

                                confirmOrder.setEnabled(false);
                            }
                            Log.e("Orden Actual","Shell ="+Integer.toString(shell)+", Pañal="+Integer.toString(pañal)+", Etiquetillas"+Integer.toString(etiquetillas));
                            adapertOrderCode=new orderCodeAdapter(OrderCodeActivity.this,listOrderCode);
                            listViewOrderCode.setAdapter(adapertOrderCode);

                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.e("Cantidad de Producto","Cancelada");
                        }
                    });
            builder.create();
            builder.show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //orderString=prefs.getString("Product_"+newOrderCode.codeSerial,"");
    }

    private void initialize_view() {
        mScannerView.startCamera();
        setContentView(R.layout.activity_order_code);
        cancelOrder=(Button)findViewById(R.id.cancelar_orden);
        addOrderCode=(Button)findViewById(R.id.agregar_producto);
        listViewOrderCode=(ListView)findViewById(R.id.ordercodes);
        confirmOrder=(Button)findViewById(R.id.confirmar_orden);

        addOrderCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(mScannerView);
            }
        });
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray postOrder= new JSONArray();
                JSONObject postOrderAmount=new JSONObject();
                try {
                    postOrderAmount.put("order_amount",Integer.toString(orderAmount));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray orderCodes = new JSONArray();
                for (OrderCode ordercode : listOrderCode){
                    JSONObject ob=new JSONObject();
                    try {
                        ob.put("code_id",ordercode.codeId);
                        ob.put("product_id",ordercode.productId);
                        ob.put("product_type_id",ordercode.productType);
                        ob.put("product_amount",ordercode.amount);
                        ob.put("code_value_serial",ordercode.codeSerial);
                        ob.put("product_name",ordercode.productName);
                        orderCodes.put(ob);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                postOrder.put(postOrderAmount);
                postOrder.put(orderCodes);
                Log.e("order codes a registrar", postOrder.toString());
                new PostOrder(OrderCodeActivity.this,postOrder).execute();
            }
        });
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
        mScannerView.startCamera();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.stopCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.stopCamera();

    }
}
