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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PostOrder extends AsyncTask<Void, Void, String> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
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
        String aux="";
        JSONObject jsonObject;
        InputStream inputStream;
        String json="";

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
            JSONObject jsonPost = new JSONObject();
            jsonPost.accumulate("user", usuario);
            jsonPost.accumulate("order", postOrder);
            json = jsonPost.toString();
            StringEntity se = new StringEntity(json);
            Log.e("Json a Enviar",json);
            httpPost.setEntity(se);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept-Encoding", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream!=null) {
                aux =convertInputStreamToString(inputStream);
                Log.i("Registro de orden", "Registro posteado\n ");
            } else {
                Log.e("Registro de orden", "fallo el Registro");
            }
            Log.e("Toda la respuesta", inputStream.toString());
            JSONObject jsonar= new JSONObject(aux);
            Log.e("Response", jsonar.toString());
            JSONObject login_response = jsonar.getJSONObject("response");
            Log.e("Response Object", login_response.toString());
            success = login_response.getString("success");
            error=false;
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
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }
}