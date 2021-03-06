package com.empresa.oscar.exportando.get;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.empresa.oscar.exportando.object.Locacion;

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
 * Created by lord on 02/10/2014.
 */


public class GetLocations extends AsyncTask<Void, Void, ArrayList> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private ArrayList<Locacion> listaLocaciones;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    String tipo;
    int id;
    String nick,pass,type,authorization;


    public GetLocations(Activity context, String nick, String pass, int id) {
        this.context = context;
        this.nick=nick;
        this.pass=pass;
        this.id=id;
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
    protected ArrayList doInBackground(Void... arg0) {
        String response;
        String aux;
        listaLocaciones=new ArrayList<Locacion>();
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/getLocations.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("employee_nickname", nick));
            nameValuePairs.add(new BasicNameValuePair("employee_password", pass));
            nameValuePairs.add(new BasicNameValuePair("employee_id", Integer.toString(id)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux= response.toString();

            if (aux != null) {
                Log.i("Locaciones", "Pidiendo Locaciones \n ");
            } else {
                Log.e("Locaciones","fallaron las locaciones");
            }

            JSONArray locaciones = new JSONArray(aux);

            Log.e("Response",locaciones.toString());
            Log.e("Tamaño locaciones", String.valueOf(locaciones.length()));
            for (int i = 0; i < locaciones.length(); i++) {
                JSONObject evobject = locaciones.getJSONObject(i);
                Log.e("Tamaño locaciones", evobject.getString("location_id")+evobject.getString("location_name"));
                String templocation=evobject.getString("location_id");
                listaLocaciones.add(new Locacion( Integer.parseInt(templocation),evobject.getString("location_name")));
            }

        } catch (Exception ex) {
            error = true;
            Log.e("error",ex.toString());
            return null;
        }
        return listaLocaciones;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    protected void onPostExecute(ArrayList result) {
        super.onPostExecute(result);
        progressDialog.hide();
        if (!error) {
            Log.d("Login","Datos correctos");
        } else {
            Log.d("Login","Datos incorrectos");
        }
        progressDialog.dismiss();
    }
}