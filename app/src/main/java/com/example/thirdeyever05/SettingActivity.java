package com.example.thirdeyever05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity implements Button.OnClickListener{

    ImageView btn_before_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sharedPreferences = getSharedPreferences("device_ip", MODE_PRIVATE);

        btn_before_page = findViewById(R.id.before_page_btn);
        btn_before_page.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.before_page_btn:
                Intent intentSp = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intentSp);
                finish();
                break;
        }
    }
}
