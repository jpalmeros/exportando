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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UsuarioRasa on 25/09/2014.
 */
public class Login extends AsyncTask<Void, Void, String> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    String tipo;
    String nick,pass,type,authorization;


    Login(Activity context,String nick,String pass) {
        this.context = context;
        this.nick=nick;
        this.pass=pass;
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
    protected String doInBackground(Void... arg0) {
        String response;
        String aux;
        JSONObject jsonObject;


        //post para obtener los dominios
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/login.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("nick", nick));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux= response.toString();

            if (aux != null) {
                Log.i("Login", "Login posteado \n ");
            } else {
                Log.e("Login","fallo el login");
            }

            jsonObject = new JSONObject(aux);
            Log.e("Response",jsonObject.toString());
            JSONObject login_response = jsonObject.getJSONObject("response");
            Log.e("Response Object", login_response.toString());


            type=  login_response.getString("type");
            nick=  login_response.getString("employee");
            pass=  login_response.getString("password");
            authorization=  login_response.getString("authorization");

            SharedPreferences prefs = context.getSharedPreferences("Exporta",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Empleado", nick);
            editor.putString("Password", pass);
            editor.putString("Type", type);
            editor.putString("Autorizacion", authorization);



            editor.commit();
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
        if(authorization.equals("granted")){


            Toast.makeText(context," Acceso authorizado a "+nick+"\n+"+type,Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Ho"+nick+" Acceso no authorizado",Toast.LENGTH_SHORT).show();
        }
    }
}