package com.example.ecotracku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int ecoPoints = 0;
    private TextView pointsView;
    private CheckBox challenge1, challenge2, challenge3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointsView = findViewById(R.id.ecoPoints);
        challenge1 = findViewById(R.id.challenge1);
        challenge2 = findViewById(R.id.challenge2);
        challenge3 = findViewById(R.id.challenge3);

        Button walkBtn = findViewById(R.id.walkBtn);
        Button cycleBtn = findViewById(R.id.cycleBtn);
        Button leaderboardBtn = findViewById(R.id.leaderboardBtn);
        Button graphsBtn = findViewById(R.id.graphsBtn);

        // simple UI-only buttons (non-functional for now)
        walkBtn.setOnClickListener(v -> { /* future: open tracking */ });
        cycleBtn.setOnClickListener(v -> { /* future: open tracking */ });
        leaderboardBtn.setOnClickListener(v -> { /* future: open leaderboard */ });
        graphsBtn.setOnClickListener(v -> { /* future: open charts */ });

        challenge1.setOnCheckedChangeListener((buttonView, isChecked) -> updatePoints(isChecked));
        challenge2.setOnCheckedChangeListener((buttonView, isChecked) -> updatePoints(isChecked));
        challenge3.setOnCheckedChangeListener((buttonView, isChecked) -> updatePoints(isChecked));
    }

    private void updatePoints(boolean add) {
        ecoPoints += add ? 10 : -10;
        if (ecoPoints < 0) ecoPoints = 0;
        pointsView.setText("Eco Points: " + ecoPoints);
    }
}
