package com.empresa.oscar.exportando;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by UsuarioRasa on 06/10/2014.
 */
public class PostStorageDelivery extends AsyncTask<Void, Void, String> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    private boolean error,full,empty;
    private String serial, success;
    private int purchase_id, code_id, amount, user_id;
    int id,location_id;
    String nick, pass, type, authorization;


    PostStorageDelivery(Activity context, int purchase_id, int code_id, String serial, int user_id, int amount,int location_id,boolean full,boolean empty) {
        this.context = context;
        this.purchase_id = purchase_id;
        this.code_id = code_id;
        this.user_id = user_id;
        this.serial = serial;
        this.amount = amount;
        this.location_id=location_id;
        this.full=full;
        this.empty=empty;

    }

    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registrando Entrega, espere por favor...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... arg0) {
        String response;
        String aux;
        JSONObject jsonObject;

        SharedPreferences prefs = context.getSharedPreferences("Exporta", Activity.MODE_PRIVATE);
        String usr = prefs.getString("Empleado", null);
        String pass = prefs.getString("Password", null);


        //post para obtener los dominios
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/postDelivery.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("employee_nickname", usr));
            nameValuePairs.add(new BasicNameValuePair("employee_password", pass));
            nameValuePairs.add(new BasicNameValuePair("delivery_employee_id", Integer.toString(user_id)));
            nameValuePairs.add(new BasicNameValuePair("delivery_code_id", Integer.toString(code_id)));
            nameValuePairs.add(new BasicNameValuePair("delivery_date", get_fecha()));
            nameValuePairs.add(new BasicNameValuePair("delivery_amount", Integer.toString(amount)));
            nameValuePairs.add(new BasicNameValuePair("delivery_location_id", Integer.toString(location_id)));
            nameValuePairs.add(new BasicNameValuePair("delivery_purchase_id", Integer.toString(purchase_id)));
            nameValuePairs.add(new BasicNameValuePair("delivery_full", Boolean.toString(full)));
            nameValuePairs.add(new BasicNameValuePair("delivery_empty", Boolean.toString(empty)));
            nameValuePairs.add(new BasicNameValuePair("delivery_value_serial", serial));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux = response.toString();

            if (aux != null) {
                Log.i("Registro de almacen", "Registro posteado\n ");
            } else {
                Log.e("Registro de almacen", "fallo el Registro");
            }
            Log.e("Toda la respuesta", aux);
            jsonObject = new JSONObject(aux);
            Log.e("Response", jsonObject.toString());
            JSONObject login_response = jsonObject.getJSONObject("response");
            Log.e("Response Object", login_response.toString());


            success = login_response.getString("success");
            
        } catch (Exception ex) {
            error = true;
            Log.e("error", ex.toString());
            return null;
        }
        return authorization;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.hide();
        if (!error) {
            Log.d("Entrega", "Datos correctos");
        } else {
            Log.d("Entrega", "Datos incorrectos");
        }
        progressDialog.dismiss();
        if (success.equals("exito")) {
            Toast.makeText(context, " Entrega Registrada ", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(context, "Error al registrar la Entrega", Toast.LENGTH_SHORT).show();
        }
    }

    private String get_fecha() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        Log.d("fecha", strDate);
        return strDate;
    }
}