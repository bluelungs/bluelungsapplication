package com.example.bluelungs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    ImageView img;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img= findViewById (R.id.img);
        img.animate().alpha(4000).setDuration(0);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent dsp = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(dsp);
                finish();
            }
        }, 4000);
    }
}