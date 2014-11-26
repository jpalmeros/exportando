package com.empresa.oscar.exportando.post;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.List;

public class PostLossVerification extends AsyncTask<Void,Void,String> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    private String success,nick,pass;
    private int loss_id,loss_type_id;
    String authorization;

    public PostLossVerification(Activity context, int loss_id, int loss_type_id, String nick, String pass) {
        this.context = context;
        this.loss_id=loss_id;
        this.loss_type_id=loss_type_id;
        this.nick=nick;
        this.pass=pass;
    }

    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registrando merma, espere por favor...");
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
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/postLossVerification.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("nick", nick));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            nameValuePairs.add(new BasicNameValuePair("loss_id", Integer.toString(loss_id)));
            nameValuePairs.add(new BasicNameValuePair("loss_type_id", Integer.toString(loss_type_id)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux= response;

            if (aux != null) {
                Log.i("Registro de Merma", "Registro posteado\n ");
            } else {
                Log.e("Registro de Merma","fallo el Registro");
            }
            Log.e("Toda la respuesta",aux);
            jsonObject = new JSONObject(aux);
            Log.e("Response",jsonObject.toString());
            JSONObject login_response = jsonObject.getJSONObject("response");
            Log.e("Response Object", login_response.toString());

            success=  login_response.getString("success");

        } catch (Exception ex) {
            error = true;
            Log.e("error",ex.toString());
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
}
