package com.empresa.oscar.exportando.get;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.empresa.oscar.exportando.object.Product;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lord on 05/11/2014.
 */
public class GetProduct extends AsyncTask<Void, Void, Product> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    String tipo;
    int id,code_id;
    String nick,pass,type,authorization;
    public GetProduct(Activity context, String nick, String pass, int id, int code_id) {
        this.context = context;
        this.nick=nick;
        this.pass=pass;
        this.id=id;
        this.code_id=code_id;
    }

    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Autenificando, espere por favor...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }

    @Override
    protected Product doInBackground(Void... arg0) {

        String response;
        String aux;
        Product producto=null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/getProduct.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("employee_nickname", nick));
            nameValuePairs.add(new BasicNameValuePair("employee_password", pass));
            nameValuePairs.add(new BasicNameValuePair("employee_id", Integer.toString(id)));
            nameValuePairs.add(new BasicNameValuePair("code_id", Integer.toString(code_id)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux= response.toString();

            if (aux != null) {
                Log.i("Producto", "Pidiendo Producto \n ");
            } else {
                Log.e("Producto","fallo producto");
            }

            JSONArray products = new JSONArray(aux);

            Log.e("Response", products.toString());
            Log.e("Tamaño producto", String.valueOf(products.length()));
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);

                Log.e("Tamaño Poducto", Integer.toString(product.getInt("product_amount_remains")) + product.getString("product_name"));
                producto=new Product(product.getInt("code_id"),product.getInt("product_id"),product.getInt("product_type_id"),product.getInt("product_amount_remains"),product.getString("product_name"),product.getString("code_value_serial"));
            }
        } catch (Exception ex) {
            error = true;
            Log.e("error",ex.toString());
            return null;
        }
        return producto;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    protected void onPostExecute(Product producto) {
        super.onPostExecute(producto);

        progressDialog.hide();

        if (!error) {
            Log.d("Login","Datos correctos");
        } else {
            Log.d("Login","Datos incorrectos");
        }
        progressDialog.dismiss();
    }
}