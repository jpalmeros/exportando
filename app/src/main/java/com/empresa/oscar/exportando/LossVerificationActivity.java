package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.empresa.oscar.exportando.adapter.lossTypeAdapter;
import com.empresa.oscar.exportando.get.GetLossTypes;
import com.empresa.oscar.exportando.object.LossType;
import com.empresa.oscar.exportando.post.PostLossVerification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class LossVerificationActivity extends Activity {
    private Button verified,canceled;
    private TextView orderTexto,unitiesTexto,locationTexto,dateTexto,employeeTexto;
    private Spinner lossTypeSpinner;
    private String nick,pass,lossString;
    private JSONObject lossObject;
    private ArrayList<LossType> lossTypeArrayList;
    private lossTypeAdapter lossAdapter;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_verification);

        final Bundle bundle = getIntent().getExtras();
        lossString = bundle.getString("loss");
        try {
            lossObject= new JSONObject(lossString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = LossVerificationActivity.this.getSharedPreferences("Exporta", Activity.MODE_PRIVATE);

        id=prefs.getInt("Id", 0);
        nick=prefs.getString("Empleado", null);
        pass=prefs.getString("Password", null);

        verified=(Button)findViewById(R.id.verified);
        canceled=(Button)findViewById(R.id.canceled);
        orderTexto=(TextView)findViewById(R.id.orderValue);
        unitiesTexto=(TextView)findViewById(R.id.unitiesValue);
        locationTexto=(TextView)findViewById(R.id.locationValue);
        dateTexto=(TextView)findViewById(R.id.dateValue);
        employeeTexto=(TextView)findViewById(R.id.employeeValue);
        lossTypeSpinner=(Spinner)findViewById(R.id.spinner1);

        try {
            lossTypeArrayList=new GetLossTypes(LossVerificationActivity.this,nick,pass,id).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        lossAdapter=new lossTypeAdapter(LossVerificationActivity.this,lossTypeArrayList);
        lossTypeSpinner.setAdapter(lossAdapter);

        try {
            orderTexto.setText(lossObject.getString("loss_order_token"));
            unitiesTexto.setText(Integer.toString(lossObject.getInt("loss_amount")));
            locationTexto.setText(lossObject.getString("loss_location_name"));
            dateTexto.setText(lossObject.getString("loss_date"));
            employeeTexto.setText(lossObject.getString("loss_user_nickname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new PostLossVerification(LossVerificationActivity.this,lossObject.getInt("loss_id"),lossObject.getInt("loss_type_id"),nick,pass).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loss_verification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
