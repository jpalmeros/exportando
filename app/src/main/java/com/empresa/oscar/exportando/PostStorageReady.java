package com.empresa.oscar.exportando;

/**
 * Created by lord on 13/10/2014.
 */

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

public class PostStorageReady extends AsyncTask<Void, Void, String> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    private String serial,success,status;
    private int purchase_id,code_id,amount,user_id,full,empty;
    int id;
    String nick,pass,type,authorization;

    PostStorageReady(Activity context,int purchase_id,int code_id, String serial ,int user_id, int amount,int full,int empty) {
        this.context = context;
        this.purchase_id=purchase_id;
        this.code_id=code_id;
        this.user_id=user_id;
        this.serial=serial;
        this.amount=amount;
        this.full=full;
        this.empty=empty;
    }

    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registrando entrada, espere por favor...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... arg0) {
        String response;
        String aux;
        JSONObject jsonObject;

        SharedPreferences prefs = context.getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
        String usr=prefs.getString("Empleado",null);
        String pass=prefs.getString("Password",null);

        //post para obtener los dominios
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/storageReady.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("nick", usr));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            nameValuePairs.add(new BasicNameValuePair("storage_employee_id", Integer.toString(user_id)));
            nameValuePairs.add(new BasicNameValuePair("storage_ready_code_id", Integer.toString(code_id)));
            nameValuePairs.add(new BasicNameValuePair("storage_ready_date", get_fecha()));
            nameValuePairs.add(new BasicNameValuePair("storage_ready_amount", Integer.toString(amount)));
            nameValuePairs.add(new BasicNameValuePair("code_purchase_id", Integer.toString(purchase_id)));
            nameValuePairs.add(new BasicNameValuePair("code_value_serial", serial));
            nameValuePairs.add(new BasicNameValuePair("storage_full", Integer.toString(full)));
            nameValuePairs.add(new BasicNameValuePair("storage_empty", Integer.toString(empty)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux= response.toString();

            if (aux != null) {
                Log.i("Registro de almacen", "Registro posteado\n ");
            } else {
                Log.e("Registro de almacen","fallo el Registro");
            }
            Log.e("Toda la respuesta",aux);
            jsonObject = new JSONObject(aux);
            Log.e("Response",jsonObject.toString());
            JSONObject login_response = jsonObject.getJSONObject("response");
            Log.e("Response Object", login_response.toString());

            success=  login_response.getString("success");
            status=  login_response.getString("status");

        } catch (Exception ex) {
            error = true;
            Log.e("error",ex.toString());
            return null;
        }
        return authorization;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.hide();
        if (!error) {
            Log.d("Login","Datos correctos");
        } else {
            Log.d("Login","Datos incorrectos");
        }
        progressDialog.dismiss();
        if(success.equals("granted")){
            Toast.makeText(context, " Compra Registrada ", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Error al registrar la entrada",Toast.LENGTH_SHORT).show();
        }
    }
    private String get_fecha(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        Log.d("fecha",strDate);
        return strDate;
    }
}