package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.empresa.oscar.exportando.adapter.lossAdapter;
import com.empresa.oscar.exportando.get.GetLosses;
import com.empresa.oscar.exportando.object.Loss;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class LossActivity extends Activity {
    private ArrayList<Loss> listLosses;
    private ListView listaMermas;
    private String usr,pass;
    private int user_id;
    private lossAdapter adaptadorMermas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);

        listaMermas=(ListView)findViewById(R.id.mermas);

        SharedPreferences prefs = getSharedPreferences("Exporta", Activity.MODE_PRIVATE);
        usr=prefs.getString("Empleado",null);
        pass=prefs.getString("Password",null);
        user_id= prefs.getInt("Id",0);

        try {
            listLosses=new GetLosses(this,usr,pass,user_id).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adaptadorMermas=new lossAdapter(LossActivity.this,listLosses);
        listaMermas.setAdapter(adaptadorMermas);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loss, menu);
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
