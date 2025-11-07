package com.example.ecotracku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private TextView leaderboardTextView, backButtonLeaderboard;
    private DatabaseReference leaderboardRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardTextView = findViewById(R.id.leaderboardTextView);
        backButtonLeaderboard = findViewById(R.id.backButtonLeaderboard);


        leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard");


        leaderboardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    leaderboardTextView.setText("⚠️ No leaderboard data found!");
                    return;
                }

                List<UserScore> userScores = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    Long points = userSnapshot.child("points").getValue(Long.class);

                    if (name != null && points != null) {
                        userScores.add(new UserScore(name, points));
                        android.util.Log.d("FIREBASE_DATA", "User: " + name + ", Points: " + points);
                    }
                }

                if (userScores.isEmpty()) {
                    leaderboardTextView.setText("⚠️ No player data found!");
                    return;
                }


                Collections.sort(userScores, (a, b) -> Long.compare(b.points, a.points));


                StringBuilder data = new StringBuilder();
                for (int i = 0; i < userScores.size(); i++) {
                    UserScore user = userScores.get(i);
                    data.append(getMedal(i + 1))
                            .append(" ")
                            .append(i + 1)
                            .append(". ")
                            .append(user.name)
                            .append(" — ")
                            .append(user.points)
                            .append(" points\n\n");
                }

                leaderboardTextView.setText(data.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                leaderboardTextView.setText("❌ Error: " + error.getMessage());
                android.util.Log.e("FIREBASE_ERROR", error.getMessage());
            }
        });


        backButtonLeaderboard.setOnClickListener(v -> {
            startActivity(new Intent(LeaderboardActivity.this, MainActivity.class));
            finish();
        });
    }


    private String getMedal(int rank) {
        switch (rank) {
            case 1:
                return "🥇";
            case 2:
                return "🥈";
            case 3:
                return "🥉";
            default:
                return "⭐";
        }
    }


    static class UserScore {
        String name;
        long points;

        UserScore(String name, long points) {
            this.name = name;
            this.points = points;
        }
    }
}
