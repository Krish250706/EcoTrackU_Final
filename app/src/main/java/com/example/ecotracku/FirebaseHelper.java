package com.example.ecotracku;

import android.content.Context;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static final String LEADERBOARD_NODE = "leaderboard";

    public static void writeUserScore(Context ctx, String uid, String name, int points) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(LEADERBOARD_NODE);
        UserScore user = new UserScore(name, points);
        ref.child(uid).setValue(user);
    }


    public static class UserScore {
        public String name;
        public int points;

        public UserScore() { }
        public UserScore(String name, int points) {
            this.name = name;
            this.points = points;
        }
    }
}
