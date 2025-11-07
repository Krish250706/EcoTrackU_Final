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
import com.google.android.gms.fitness.RecordingClient;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class CycleActivity extends AppCompatActivity {

    private static final int REQUEST_OAUTH_REQUEST_CODE = 1001;
    private TextView todayDistanceTv, statusTv;
    private double simulatedKm = 0.0;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    statusTv.setText("Permission granted");
                } else {
                    statusTv.setText("Permission denied");
                    Toast.makeText(this, "Activity permission needed for trackers", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);


        TextView backButton = findViewById(R.id.backButtonCycle);
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });


        todayDistanceTv = findViewById(R.id.todayDistance);
        statusTv = findViewById(R.id.statusText);

        Button btnConnect = findViewById(R.id.btnConnectFit);
        Button btnSubscribe = findViewById(R.id.btnSubscribe);
        Button btnRead = findViewById(R.id.btnReadToday);
        Button btnSimulate = findViewById(R.id.btnSimulate);

        requestActivityPermissionIfNeeded();

        btnConnect.setOnClickListener(v -> requestGoogleSignInWithFit());
        btnSubscribe.setOnClickListener(v -> {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account == null) {
                Toast.makeText(this, "Please connect Google Fit first", Toast.LENGTH_SHORT).show();
                return;
            }
            subscribeToDistance(account);
        });
        btnRead.setOnClickListener(v -> readTodayDistance());
        btnSimulate.setOnClickListener(v -> {
            simulatedKm += 0.5;
            updateDistanceUi(simulatedKm);
            statusTv.setText("Simulated +0.5 km (demo mode)");
        });

        // If already signed in, show connected status
        GoogleSignInAccount last = GoogleSignIn.getLastSignedInAccount(this);
        if (last != null) {
            statusTv.setText("Connected to Google Fit");
        }
    }

    private void updateDistanceUi(double km) {
        String s = String.format("Today's cycling: %.2f km", km);
        todayDistanceTv.setText(s);
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
        // Request Fitness activity read scope
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
                Toast.makeText(this, "Google Fit connected", Toast.LENGTH_SHORT).show();
            } else {
                statusTv.setText("Google Fit connection failed");
            }
        }
    }

    private void subscribeToDistance(GoogleSignInAccount account) {
        RecordingClient recordingClient = Fitness.getRecordingClient(this, account);
        recordingClient.subscribe(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(aVoid -> statusTv.setText("Subscribed to distance updates"))
                .addOnFailureListener(e -> statusTv.setText("Subscribe failed: " + e.getMessage()));
    }

    private void readTodayDistance() {
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
                        .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();

        Task<com.google.android.gms.fitness.result.DataReadResponse> response =
                historyClient.readData(readRequest);

        response.addOnSuccessListener(dataReadResponse -> {
            long totalMeters = 0;
            if (dataReadResponse.getBuckets().size() > 0) {
                for (Bucket bucket : dataReadResponse.getBuckets()) {
                    for (DataSet set : bucket.getDataSets()) {
                        for (DataPoint dp : set.getDataPoints()) {
                            for (com.google.android.gms.fitness.data.Field field : dp.getDataType().getFields()) {
                                try {
                                    float val = dp.getValue(field).asFloat();
                                    totalMeters += val;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } else {
                DataSet ds = dataReadResponse.getDataSet(DataType.AGGREGATE_DISTANCE_DELTA);
                if (ds != null) {
                    for (DataPoint dp : ds.getDataPoints()) {
                        for (com.google.android.gms.fitness.data.Field field : dp.getDataType().getFields()) {
                            try {
                                totalMeters += dp.getValue(field).asFloat();
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }

            double km = totalMeters / 1000.0;
            km += simulatedKm;
            updateDistanceUi(km);
            statusTv.setText("Data fetched successfully");
        }).addOnFailureListener(e -> {
            statusTv.setText("Read failed: " + e.getMessage());
        });
    }
}
