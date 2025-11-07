package com.example.ecotracku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView earthImage = findViewById(R.id.earthImage);
        TextView appName = findViewById(R.id.appName);
        TextView tagline = findViewById(R.id.tagline);


        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_earth);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        earthImage.startAnimation(rotate);
        appName.startAnimation(fadeIn);
        tagline.startAnimation(fadeIn);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3500);
    }
}
