package com.empresa.oscar.exportando;

import com.empresa.oscar.exportando.util.*;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = false;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;


    private static final int HIDER_FLAGS = com.empresa.oscar.exportando.util.SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private com.empresa.oscar.exportando.util.SystemUiHider mSystemUiHider;

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

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

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


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
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
