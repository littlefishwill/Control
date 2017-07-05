package com.lfish.control.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;

/**
 * Created by SuZhiwei on 2016/7/4.
 */
public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 拨打方username
        String from = intent.getStringExtra("from");
        // call type
        String type = intent.getStringExtra("type");
        if ("video".equals(type)) { // 视频通话
            // context.startActivity(new Intent(context,
            // VideoCallActivity.class).
            // putExtra("username", from).putExtra("isComingCall", true).
            // addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            VideoCallAgree(context);
        } else { // 音频通话

            AudioCallAgree();
            //关闭扬声器
//            CloseSpeaker(context);
        }
    }

    private void AudioCallAgree() {
        /**
         * 接听通话
         * @throws EMNoActiveCallException
         * @throws EMNetworkUnconnectedException
         */
        try {
            EMClient.getInstance().callManager().answerCall();
//            EMClient.getInstance().callManager().pauseVoiceTransfer();
        } catch (EMNoActiveCallException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        EMClient.getInstance().callManager().


    }

    public void CloseSpeaker(Context context) {
        try {
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            if(audioManager != null) {
                if(audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(false);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,0,
                            AudioManager.STREAM_VOICE_CALL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
