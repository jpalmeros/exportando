package com.empresa.oscar.exportando.get;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.empresa.oscar.exportando.object.Loss;

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
public class GetLosses extends AsyncTask<Void, Void, ArrayList> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private ArrayList<Loss> listaLosses;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    String tipo;
    int id;
    String nick,pass,type,authorization;

    public GetLosses(Activity context, String nick, String pass, int id) {
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
        listaLosses=new ArrayList<Loss>();
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/getLosses.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("employee_nickname", nick));
            nameValuePairs.add(new BasicNameValuePair("employee_password", pass));
            nameValuePairs.add(new BasicNameValuePair("employee_id", Integer.toString(id)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux= response;

            if (aux != null) {
                Log.i("Mermas", "Pidiendo Mermas \n ");
            } else {
                Log.e("Mermas","fallaron las mermas");
            }

            JSONArray losses = new JSONArray(aux);

            Log.e("Response", losses.toString());
            Log.e("Tamaño mermas", String.valueOf(losses.length()));
            for (int i = 0; i < losses.length(); i++) {

                JSONObject loss = losses.getJSONObject(i);
                int loss_id=loss.getInt("loss_id");
                int loss_order_id=loss.getInt("loss_order_id");
                int loss_amount=loss.getInt("loss_amount");
                int loss_location_id=loss.getInt("loss_location_id");
                int loss_delivery_id=loss.getInt("loss_delivery_id");
                String loss_location_name=loss.getString("loss_location_name");
                int loss_user_id=loss.getInt("loss_user_id");
                String loss_user_nickname=loss.getString("loss_user_nickname");
                int loss_type_id=loss.getInt("loss_type_id");
                String loss_order_token=loss.getString("loss_order_token");
                String loss_type_name=loss.getString("loss_type_name");
                boolean loss_status=Boolean.parseBoolean(loss.getString("loss_status"));
                String loss_date=loss.getString("loss_date");
                listaLosses.add(new Loss(loss_order_id,loss_order_token,loss_location_id,loss_location_name,loss_type_id
                ,loss_type_name,loss_user_id,loss_user_nickname,loss_amount,loss_id,loss_status,loss_date,loss_delivery_id));
            }

        } catch (Exception ex) {
            error = true;
            Log.e("error",ex.toString());
            return null;
        }
        return listaLosses;
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