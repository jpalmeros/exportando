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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainFullscreenActivity extends Activity {

    private static final String TAG = "QR Scanner";
    private Activity mActivity;
    private Button mGetQRButton;
    private TextView mQRCodeTextView;

    private Activity context;



    private Button login_button,exit_button,otro_button;
    private EditText login_nick,login_password,temp_login_button;
    private Boolean displayed_login;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        //para QR
        mActivity = this;
        mGetQRButton = (Button) findViewById(R.id.button_get_qr_code);
        mQRCodeTextView = (TextView) findViewById(R.id.text_view_qr_content);

        otro_button = (Button) findViewById(R.id.button_QRconLib);
        otro_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leerqr = new Intent(MainFullscreenActivity.this, LeerQR.class);
                MainFullscreenActivity.this.startActivity(leerqr);
            }});


        setupButton();

        displayed_login=false;

        login_button=(Button)findViewById(R.id.login_button);
        exit_button=(Button)findViewById(R.id.exit_button);
        login_nick=(EditText)findViewById(R.id.login_nick);
        login_password=(EditText)findViewById(R.id.login_password);


        final ObjectAnimator loginOpen= new ObjectAnimator().ofFloat(login_button,"translationY",login_button.getY(),login_button.getY()-115);
        loginOpen.setDuration(1000);
        loginOpen.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)login_button.getLayoutParams();
                params.setMargins(0,0,0,50);
                login_button.setLayoutParams(params);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                login_nick.setVisibility(View.VISIBLE);
                login_password.setVisibility(View.VISIBLE);
                displayed_login=true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });


        final ObjectAnimator loginClosed= new ObjectAnimator().ofFloat(login_button,"translationY",login_button.getY(),login_button.getY()+115);
        loginClosed.setDuration(1000);

        loginClosed.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                login_nick.setVisibility(View.INVISIBLE);
                login_password.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)login_button.getLayoutParams();
                params.setMargins(0,0,0,165);
                login_button.setLayoutParams(params);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                displayed_login = false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!displayed_login){
                    loginOpen.start();
                    Log.i("login clicked","Opening");
                }else{
                    Log.i("login clicked","Posting");
                    new Login(MainFullscreenActivity.this,login_nick.getText().toString(),login_password.getText().toString()).execute();
                }

            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!displayed_login){
                   finish();
                }
                else {
                    loginClosed.start();

                }
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





//lee QR cuando termina
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String scanContent = scanResult.getContents();

            //separamos cadena obtenida
            String tmp[]=scanContent.split(":");
            String usr=tmp[0];
            String pass=tmp[1];
            Log.d(TAG, "QR Scan :" + usr+" - "+ pass);
            //logueando
            new Login(MainFullscreenActivity.this,usr,pass).execute();
            mQRCodeTextView.setText("Hola: "+usr);
        }
    }
    //abre QR reader
    private void setupButton() {
        mGetQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(mActivity);
                integrator.initiateScan();
            }
        });
    }
}
