package com.empresa.oscar.exportando.post;

/**
 * Created by UsuarioRasa on 18/11/2014.
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PostOrder extends AsyncTask<Void, Void, String> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    private boolean error;
    private String  success;
    private JSONArray postOrder;
    String authorization;


    public PostOrder(Activity context, JSONArray postOrder) {
        this.context = context;
        this.postOrder=postOrder;
    }

    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registrando Orden, espere por favor...");
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
        JSONObject usuario=new JSONObject();
        try {
            usuario.put("employee_nickname",usr);
            usuario.put("employee_password",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/postOrder.php");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept-Encoding", "application/json");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("user",usuario.toString()));
            nameValuePairs.add(new BasicNameValuePair("order", postOrder.toString()));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux = response.toString();
            if (aux != null) {
                Log.i("Registro de orden", "Registro posteado\n ");
            } else {
                Log.e("Registro de orden", "fallo el Registro");
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
            authorization="error";
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
            Log.d("Orden", "Datos correctos");
        } else {
            Log.d("Orden", "Datos incorrectos");
        }
        progressDialog.dismiss();
        if (success.equals("exito")) {
            Toast.makeText(context, " Orden Registrada ", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(context, "Error al registrar la Orden", Toast.LENGTH_SHORT).show();
        }
    }
}