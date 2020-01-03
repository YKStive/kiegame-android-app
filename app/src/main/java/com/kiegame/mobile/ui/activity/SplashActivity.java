package com.kiegame.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kiegame.mobile.R;

/**
 * Created by: var_rain.
 * Created date: 2020/01/03.
 * Description: 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // TODO: 2020/1/3 启动页保留
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
