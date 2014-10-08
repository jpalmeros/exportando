package com.empresa.oscar.exportando;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MainFullscreenActivity extends Activity {

    private static final String TAG = "QR Scanner";
    private Activity mActivity;
    private Button mGetQRButton;
    private TextView mQRCodeTextView;

    private Activity context;



    private Button login_button,exit_button,otro_button;
    private EditText login_nick,login_password,temp_login_button;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        //para QR
        mActivity = this;

        otro_button = (Button) findViewById(R.id.btn_login_QR);
        otro_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leerqr = new Intent(MainFullscreenActivity.this, LeerQR_login.class);
                MainFullscreenActivity.this.startActivity(leerqr);
            }});





        login_button=(Button)findViewById(R.id.btn_login_Manual);
        exit_button=(Button)findViewById(R.id.exit_button);
        login_nick=(EditText)findViewById(R.id.login_nick);
        login_password=(EditText)findViewById(R.id.login_password);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("login clicked","Posting");
                new Login(MainFullscreenActivity.this,login_nick.getText().toString(),login_password.getText().toString()).execute();
                }


        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) { super.onPostCreate(savedInstanceState);}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}
