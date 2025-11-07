package com.example.ecotracku;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EcoTasksActivity extends AppCompatActivity {

    private int ecoPoints = 0;
    private TextView ecoPointsText;

    private String[] tasks = {
            "🚶 Walk or cycle instead of driving today",
            "🌳 Plant a small tree or care for a plant",
            "♻️ Separate dry and wet waste",
            "💧 Save water while brushing teeth",
            "⚡ Turn off unnecessary lights and fans",
            "🛍️ Avoid plastic bags — use cloth bag"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_tasks);
// 🔙 Back Button Logic
        TextView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); });

        ecoPointsText = findViewById(R.id.ecoPointsText);
        LinearLayout taskList = findViewById(R.id.taskList);

        // Dynamically add tasks
        for (String task : tasks) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(task);
            checkBox.setTextColor(getColor(android.R.color.white));
            checkBox.setTextSize(16f);
            checkBox.setPadding(10, 20, 10, 20);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    ecoPoints += 10;
                } else {
                    ecoPoints -= 10;
                }
                ecoPointsText.setText("Eco Points: " + ecoPoints);
            });

            taskList.addView(checkBox);
        }
    }
}
