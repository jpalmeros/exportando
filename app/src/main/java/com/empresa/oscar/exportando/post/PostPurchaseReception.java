package com.empresa.oscar.exportando.post;

/**
 * Created by UsuarioRasa on 28/10/2014.
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


public class PostPurchaseReception extends AsyncTask<Void, Void, String> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    private String success,status;
    private int purchase_id;
    String authorization;


    public PostPurchaseReception(Activity context, int purchase_id) {
        this.context = context;
        this.purchase_id=purchase_id;

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
        int user_id= prefs.getInt("Id",0);

        //post para obtener los dominios
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/purchaseReception.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("nick", usr));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            nameValuePairs.add(new BasicNameValuePair("purchase_employee_id", Integer.toString(user_id)));
            nameValuePairs.add(new BasicNameValuePair("purchase_reception_date", get_fecha()));
            nameValuePairs.add(new BasicNameValuePair("purchase_id", Integer.toString(purchase_id)));
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
            authorization="error";
            Log.e("error",ex.toString());
            return authorization;
        }
        authorization="exito";
        return authorization;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.hide();
        if (!error) {
            Log.d("Registro de compra","Datos correctos");
        } else {
            Log.d("Registro de compra","Datos incorrectos");
        }
        progressDialog.dismiss();
        if(success.equals("granted")){
            Toast.makeText(context, " Compra Registrada ", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(context,"Error al registrar la compra",Toast.LENGTH_SHORT).show();
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