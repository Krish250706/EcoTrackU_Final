package com.example.ecotracku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        TextView appName = findViewById(R.id.appName);


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        appName.startAnimation(fadeIn);


        ObjectAnimator scaleX = ObjectAnimator.ofFloat(appName, "scaleX", 1f, 1.15f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(appName, "scaleY", 1f, 1.15f, 1f);
        scaleX.setDuration(1800);
        scaleY.setDuration(1800);

        AnimatorSet pulse = new AnimatorSet();
        pulse.playTogether(scaleX, scaleY);
        pulse.start();


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 2500);

    }

}
