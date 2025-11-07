package com.example.ecotracku;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.HistoryClient;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class WalkActivity extends AppCompatActivity {

    private static final int REQUEST_OAUTH_REQUEST_CODE = 1002;
    private TextView todayStepsTv, todayDistanceTv, statusTv;
    private int simulatedSteps = 0;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (!granted) {
                    Toast.makeText(this, "Activity recognition permission needed", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        todayStepsTv = findViewById(R.id.todaySteps);
        todayDistanceTv = findViewById(R.id.todayDistance);
        statusTv = findViewById(R.id.statusText);

        Button btnConnect = findViewById(R.id.btnConnectFit);
        Button btnRead = findViewById(R.id.btnReadToday);
        Button btnSimulate = findViewById(R.id.btnSimulate);


        TextView backButton = findViewById(R.id.backButtonWalk);
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        requestActivityPermissionIfNeeded();

        btnConnect.setOnClickListener(v -> requestGoogleSignInWithFit());
        btnRead.setOnClickListener(v -> readTodaySteps());
        btnSimulate.setOnClickListener(v -> {
            simulatedSteps += 500;
            double km = (simulatedSteps * 0.0008); // approx conversion
            todayStepsTv.setText("Today's steps: " + simulatedSteps);
            todayDistanceTv.setText(String.format("Distance: %.2f km", km));
            statusTv.setText("Simulated +500 steps (demo)");
        });
    }

    private void requestActivityPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
            }
        }
    }

    private void requestGoogleSignInWithFit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/fitness.activity.read"))
                .build();

        Intent signInIntent = GoogleSignIn.getClient(this, gso).getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_OAUTH_REQUEST_CODE);
    }

    @Override
    @Deprecated
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                statusTv.setText("Connected to Google Fit");
            } else {
                statusTv.setText("Google Fit connection failed");
            }
        }
    }

    private void readTodaySteps() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Toast.makeText(this, "Please connect Google Fit first", Toast.LENGTH_SHORT).show();
            return;
        }

        HistoryClient historyClient = Fitness.getHistoryClient(this, account);

        Calendar cal = Calendar.getInstance();
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long startTime = cal.getTimeInMillis();

        com.google.android.gms.fitness.request.DataReadRequest readRequest =
                new com.google.android.gms.fitness.request.DataReadRequest.Builder()
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();

        Task<com.google.android.gms.fitness.result.DataReadResponse> response =
                historyClient.readData(readRequest);

        response.addOnSuccessListener(dataReadResponse -> {
            int totalSteps = 0;
            if (dataReadResponse.getBuckets().size() > 0) {
                for (Bucket bucket : dataReadResponse.getBuckets()) {
                    for (DataSet set : bucket.getDataSets()) {
                        for (DataPoint dp : set.getDataPoints()) {
                            totalSteps += dp.getValue(dp.getDataType().getFields().get(0)).asInt();
                        }
                    }
                }
            }
            totalSteps += simulatedSteps;
            double km = totalSteps * 0.0008; // approximate step-to-km conversion
            todayStepsTv.setText("Today's steps: " + totalSteps);
            todayDistanceTv.setText(String.format("Distance: %.2f km", km));
            statusTv.setText("Steps data fetched successfully");
        }).addOnFailureListener(e -> {
            statusTv.setText("Failed to read steps: " + e.getMessage());
        });
    }
}
