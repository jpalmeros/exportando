package com.empresa.oscar.exportando.get;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import com.empresa.oscar.exportando.object.LossType;
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

public class GetLossTypes extends AsyncTask<Void, Void, ArrayList<LossType>> {
    private ProgressDialog progressDialog;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private ArrayList<LossType> listaLossTypes;
    private List<NameValuePair> nameValuePairs;
    private ResponseHandler<String> responseHandler;
    private Activity context;
    boolean error;
    int id;
    String nick,pass;

    public GetLossTypes(Activity context, String nick, String pass, int id) {
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
    protected ArrayList<LossType> doInBackground(Void... arg0) {
        String response;
        String aux;
        listaLossTypes=new ArrayList<LossType>();
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://crisoldeideas.com/exporta/api_layer/getLossTypes.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("employee_nickname", nick));
            nameValuePairs.add(new BasicNameValuePair("employee_password", pass));
            nameValuePairs.add(new BasicNameValuePair("employee_id", Integer.toString(id)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
            aux= response;
            if (aux != null) {
                Log.i("Mermas", "Pidiendo Tipo de mermas \n ");
            } else {
                Log.e("Mermas","fallaron los tipos de mermas");
            }
            JSONArray losses = new JSONArray(aux);
            Log.e("Response", losses.toString());
            Log.e("Tamaño mermas", String.valueOf(losses.length()));
            for (int i = 0; i < losses.length(); i++) {
                JSONObject lossType = losses.getJSONObject(i);
                int loss_type_id=lossType.getInt("loss_type_id");
                String loss_type_name=lossType.getString("loss_type_name");
                listaLossTypes.add(new LossType(loss_type_id,loss_type_name));
            }
        } catch (Exception ex) {
            error = true;
            Log.e("error",ex.toString());
            return null;
        }
        return listaLossTypes;
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