package com.example.ecotracku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView tvEcoPoints;
    private CheckBox task1, task2, task3;
    private int ecoPoints = 100;
    private Button btnWalk, btnCycle, btnLeaderboard, btnGraphs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvEcoPoints = findViewById(R.id.tvEcoPoints);
        task1 = findViewById(R.id.task1);
        task2 = findViewById(R.id.task2);
        task3 = findViewById(R.id.task3);

        btnWalk = findViewById(R.id.btnWalk);
        btnCycle = findViewById(R.id.btnCycle);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnGraphs = findViewById(R.id.btnGraphs);

        updatePointsDisplay();


        task1.setOnCheckedChangeListener((buttonView, isChecked) -> updatePoints(isChecked, 10));
        task2.setOnCheckedChangeListener((buttonView, isChecked) -> updatePoints(isChecked, 10));
        task3.setOnCheckedChangeListener((buttonView, isChecked) -> updatePoints(isChecked, 10));

        btnWalk.setOnClickListener(v -> showMessage("Walking tracker coming soon!"));
        btnCycle.setOnClickListener(v -> showMessage("Cycling tracker coming soon!"));
        btnLeaderboard.setOnClickListener(v -> showMessage("Leaderboard coming soon!"));
        btnGraphs.setOnClickListener(v -> showMessage("Graphs & Analytics coming soon!"));
    }

    private void updatePoints(boolean isChecked, int points) {
        if (isChecked) {
            ecoPoints+=points;
        } else {
            ecoPoints-=points;
        }
        updatePointsDisplay();
    }

    private void updatePointsDisplay() {
        tvEcoPoints.setText("🌱 Eco Points: " + ecoPoints);
    }

    private void showMessage(String msg) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show();
    }
}
