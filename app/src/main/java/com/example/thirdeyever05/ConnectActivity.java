package com.example.thirdeyever05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectActivity extends AppCompatActivity implements Button.OnClickListener {

    ImageView btn_before_page, btn_connect;
    EditText usr_device_ip_input;
    String deviceIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        SharedPreferences sharedPreferences = getSharedPreferences("device_ip", MODE_PRIVATE);

        usr_device_ip_input = findViewById(R.id.usr_device_ip);
        btn_before_page = findViewById(R.id.before_page_btn);
        btn_before_page.setOnClickListener(this);
        btn_connect = findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(this);

        if (sharedPreferences.getString("ip", "null") == "null") {
            usr_device_ip_input.setText("rtsp://192.168.0.222:8554/ch0");
        } else {
            deviceIp = sharedPreferences.getString("ip", "");
            usr_device_ip_input.setText(deviceIp);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                SharedPreferences sharedPreferences = getSharedPreferences("device_ip", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String value = usr_device_ip_input.getText().toString();
                editor.putString("ip", value);
                editor.commit();
            case R.id.before_page_btn:
                Intent intentSp = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(intentSp);
                finish();
                break;
        }
    }
}
