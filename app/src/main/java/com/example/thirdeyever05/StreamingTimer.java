package com.example.thirdeyever05;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import static com.example.thirdeyever05.MainActivity.broadcasting;
import static com.example.thirdeyever05.MainActivity.mContext;
import static com.example.thirdeyever05.MainActivity.selectedPlatform;

public class StreamingTimer extends Thread {
    BroadcastRTMP broadcastRTMP = new BroadcastRTMP();
    Animation rotate = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
    final Handler handler = new Handler();

    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();

        handler.post(new Runnable() {
            @Override
            public void run() {  // 화면에 그려줄 작업
                ((MainActivity) MainActivity.mContext).stream_btn_text.setText(null);
                ((MainActivity) MainActivity.mContext).streaming_live_time.setText("00:00");
                ((MainActivity) MainActivity.mContext).stream_btn_background.setImageResource(R.drawable.ic_loading);
                ((MainActivity) MainActivity.mContext).stream_btn_background.startAnimation(rotate);
                ((MainActivity) MainActivity.mContext).streaming_live_layout.setVisibility(View.VISIBLE);
                if(selectedPlatform == "FACEBOOK"){
                    ((MainActivity) MainActivity.mContext).streaming_live_layout_image.setImageResource(R.drawable.ico_facebook);
                }else if(selectedPlatform == "TWITCH"){
                    ((MainActivity) MainActivity.mContext).streaming_live_layout_image.setImageResource(R.drawable.ico_twitch);
                }else if(selectedPlatform == "YOUTUBE"){
                    ((MainActivity) MainActivity.mContext).streaming_live_layout_image.setImageResource(R.drawable.ico_youtube);
                }else if(selectedPlatform == "INSTAGRAM"){
                    ((MainActivity) MainActivity.mContext).streaming_live_layout_image.setImageResource(R.drawable.ico_instagram);
                }

            }
        });

        while (broadcasting) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {  // 화면에 그려줄 작업
                    ((MainActivity) MainActivity.mContext).streaming_live_time.setText(broadcastRTMP.broadcastTimeCalculator(startTime));
                }
            });
        }
        handler.post(new Runnable() {
            @Override
            public void run() {  // 화면에 그려줄 작업
                ((MainActivity) MainActivity.mContext).streaming_live_time.setText("00:00");
            }
        });
    }
}
