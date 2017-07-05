package com.lfish.control.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.lfish.control.BaseActivity;
import com.lfish.control.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AudioCallActivity extends BaseActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private TextView mContentView;
    private View mControlsView;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (TextView) findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endCall();
//                finish();
            }
        });
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        CheckBox checkBox = (CheckBox) findViewById(R.id.dummy_mkf);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mkfControl(b);
            }
        });


        String user = getIntent().getStringExtra("user");
        Log.e("AudioCallActivity", user);


        EMClient.getInstance().callManager().addCallStateChangeListener(new EMCallStateChangeListener() {
            @Override
            public void onCallStateChanged(CallState callState, CallError error) {
                switch (callState) {
                    case CONNECTING: // 正在连接对方
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mContentView.setText("正在连接对方");
                            }
                        });

                        break;
                    case CONNECTED: // 双方已经建立连接
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mContentView.setText("双方已建立连接");
                                mkfControl(false);
                            }
                        });

                        break;

                    case ACCEPTED: // 电话接通成功

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mContentView.setText("已经接通");
                            }
                        });
                        break;
                    case DISCONNNECTED: // 电话断了
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mkfControl(true);
                                mContentView.setText("连接断了");
                                finish();
                            }
                        });
                        break;
                    case NETWORK_UNSTABLE: //网络不稳定
                        if (error == CallError.ERROR_NO_DATA) {
                            //无通话数据
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mContentView.setText("没有数据返回");
                                }
                            });
                        } else {
                        }
                        break;
                    case NETWORK_NORMAL: //网络恢复正常

                        break;
                    default:
                        break;
                }

            }
        });

        /**
         * 拨打语音通话
         * @param to
         * @throws EMServiceNotReadyException
         */
        try {
            EMClient.getInstance().callManager().makeVoiceCall(user);
        } catch (EMServiceNotReadyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void mkfControl(boolean isOpen) {
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(isOpen);
        audioManager.setSpeakerphoneOn(isOpen);
        if(isOpen){
            EMClient.getInstance().callManager().pauseVoiceTransfer();
            audioManager.setMicrophoneMute(isOpen);
        }else{
            EMClient.getInstance().callManager().resumeVoiceTransfer();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
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

    public static void open(Context context,String userName){
        Intent intent =new Intent(context,AudioCallActivity.class);
        intent.putExtra("user", userName);
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        endCall();
    }

    public void endCall(){
        /**
         * 挂断通话
         */
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    EMClient.getInstance().callManager().endCall();
                } catch (EMNoActiveCallException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

}
