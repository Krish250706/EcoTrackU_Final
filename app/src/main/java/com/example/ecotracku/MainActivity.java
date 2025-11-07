package com.example.ecotracku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        TextView appTitle = findViewById(R.id.appTitle);
        TextView appSubtitle = findViewById(R.id.appSubtitle);
        GridLayout dashboardGrid = findViewById(R.id.dashboardGrid);


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);


        appTitle.startAnimation(fadeIn);
        appSubtitle.startAnimation(fadeIn);


        int delay = 200;
        for (int i = 0; i < dashboardGrid.getChildCount(); i++) {
            View card = dashboardGrid.getChildAt(i);
            card.setAlpha(0f);
            card.animate()
                    .alpha(1f)
                    .translationYBy(-30)
                    .setStartDelay(delay)
                    .setDuration(600)
                    .start();
            delay += 150;


            int index = i;
            card.setOnClickListener(v -> handleCardClick(index));
        }
    }

    // 🧭 Handle dashboard card clicks
    private void handleCardClick(int index) {
        switch (index) {
            case 0:

                startActivity(new Intent(MainActivity.this, EcoTasksActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case 1:

                startActivity(new Intent(MainActivity.this, CycleActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case 2:

                startActivity(new Intent(MainActivity.this, WalkActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case 3:

                startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case 4:

                Toast.makeText(this, getString(R.string.coming_soon_graphs), Toast.LENGTH_SHORT).show();
                break;

            case 5:

                Toast.makeText(this, getString(R.string.coming_soon_shop), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
