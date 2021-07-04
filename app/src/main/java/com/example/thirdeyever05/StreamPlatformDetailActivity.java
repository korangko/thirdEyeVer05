package com.example.thirdeyever05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.example.thirdeyever05.MainActivity.broadcasting;
import static com.example.thirdeyever05.MainActivity.selectedPlatform;

public class StreamPlatformDetailActivity extends AppCompatActivity implements Button.OnClickListener {

    ConstraintLayout streaming_start_btn;
    ImageView streaming_platform_icon;
    EditText usr_stream_key_input;
    TextView stream_url;
    String keyValue;

    ImageView btn_before_page;
    BroadcastRTMP broadcastRTMP = new BroadcastRTMP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platdetail);

        usr_stream_key_input = findViewById(R.id.usr_stream_key_input);
        stream_url = findViewById(R.id.stream_url);
        streaming_start_btn = findViewById(R.id.streaming_start_btn);
        streaming_start_btn.setOnClickListener(this);
        btn_before_page = findViewById(R.id.before_page_btn);
        btn_before_page.setOnClickListener(this);
        streaming_platform_icon = findViewById(R.id.streaming_platform_icon);

        SharedPreferences sharedPreferences = getSharedPreferences("stream_key", MODE_PRIVATE);

        if (selectedPlatform == "FACEBOOK") {
            stream_url.setText("rtmps://live-api-s.facebook.com:443/rtmp/");
            if(sharedPreferences.getString("key","null") == "null"){
                usr_stream_key_input.setText("189284259305046?s_bl=1&s_ps=1&s_psm=1&s_sw=0&s_vt=api-s&a=AbyWzLUTmcKMuFvs");
            }else{
                keyValue = sharedPreferences.getString("key", "");
                usr_stream_key_input.setText(keyValue);
            }
            streaming_platform_icon.setImageResource(R.drawable.ico_facebook);
        } else if (selectedPlatform == "YOUTUBE") {
            stream_url.setText("rtmp://a.rtmp.youtube.com/live2");
            if(sharedPreferences.getString("key2","null") == "null"){
                usr_stream_key_input.setText("b4gc-s07t-pwjz-0137-a7pr");
            }else{
                keyValue = sharedPreferences.getString("key2", "");
                usr_stream_key_input.setText(keyValue);
            }
            streaming_platform_icon.setImageResource(R.drawable.ico_youtube);
        } else if (selectedPlatform == "INSTAGRAM") {
            stream_url.setText("rtmps://live-upload.instagram.com:443/rtmp/");
//            if(sharedPreferences.getString("key3","null") == "null"){
//                usr_stream_key_input.setText("17889712768859923?s_sw=0&s_vt=ig&a=Abw15Cw4R2cziCPo");
//            }else{
//                keyValue = sharedPreferences.getString("key3", "");
//                usr_stream_key_input.setText(keyValue);
//            }
            usr_stream_key_input.setText("17917428976528761?s_sw=0&s_vt=ig&a=Abw_DFaYBtUQmStj");
            streaming_platform_icon.setImageResource(R.drawable.ico_instagram);
        } else if (selectedPlatform == "TWITCH") {
            stream_url.setText("rtmp://live.twitch.tv/app");
            if(sharedPreferences.getString("key4","null") == "null"){
                usr_stream_key_input.setText("live_586002078_LBVa6QJiQjfXqic4FAQyFMb5eoBRtR");
            }else{
                keyValue = sharedPreferences.getString("key4", "");
                usr_stream_key_input.setText(keyValue);
            }
            streaming_platform_icon.setImageResource(R.drawable.ico_twitch);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("stream_key", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = usr_stream_key_input.getText().toString();
        if (selectedPlatform == "FACEBOOK") {
            editor.putString("key", value);
        } else if (selectedPlatform == "YOUTUBE") {
            editor.putString("key2", value);
        } else if (selectedPlatform == "INSTAGRAM") {
            editor.putString("key3", value);
        } else if (selectedPlatform == "TWITCH") {
            editor.putString("key4", value);
        }
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.before_page_btn:
                Intent intentSp = new Intent(StreamPlatformDetailActivity.this, MainActivity.class);
                startActivity(intentSp);
                finish();
                break;

            case R.id.streaming_start_btn:
                if (selectedPlatform == "FACEBOOK") {
                    String connectingLink = getString(R.string.facebook_url) + usr_stream_key_input.getText().toString();
                    System.out.println("josh url = " + connectingLink);
                    broadcastRTMP.broadcastStart(this, connectingLink);
                } else if (selectedPlatform == "YOUTUBE") {
                    String connectingLink = getString(R.string.youtube_url) + usr_stream_key_input.getText().toString();
                    System.out.println("josh url = " + connectingLink);
                    broadcastRTMP.broadcastStart(this, connectingLink);
                } else if (selectedPlatform == "INSTAGRAM") {
                    String connectingLink = getString(R.string.instagram_url) + usr_stream_key_input.getText().toString();
                    System.out.println("josh url = " + connectingLink);
                    broadcastRTMP.broadcastStart(this, connectingLink);
                } else if (selectedPlatform == "TWITCH") {
                    String connectingLink = getString(R.string.twitch_url) + usr_stream_key_input.getText().toString();
                    System.out.println("josh url = " + connectingLink);
                    broadcastRTMP.broadcastStart(this, connectingLink);
                }
                broadcasting= true;
                Intent intentAB = new Intent(StreamPlatformDetailActivity.this, MainActivity.class);
                startActivity(intentAB);
                finish();
                break;
        }
    }

}
