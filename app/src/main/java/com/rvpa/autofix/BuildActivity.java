package com.rvpa.autofix;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuildActivity extends AppCompatActivity {

    private EditText etZip;
    private Button btnBuild;
    private ProgressBar progressBar;
    private TextView tvStatus, tvLog;
    private ErrorDetector errorDetector;
    private AIFixer aiFixer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);

        String repo = getIntent().getStringExtra("repo");
        String branch = getIntent().getStringExtra("branch");

        initViews();
        setupListeners();

        errorDetector = new ErrorDetector(this);
        aiFixer = new AIFixer(this);

        tvStatus.setText("Ready to build: " + repo + "/" + branch);
    }

    private void initViews() {
        etZip = findViewById(R.id.etZip);
        btnBuild = findViewById(R.id.btnBuild);
        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);
        tvLog = findViewById(R.id.tvLog);
    }

    private void setupListeners() {
        btnBuild.setOnClickListener(v -> {
            String zip = etZip.getText().toString().trim();
            if (zip.isEmpty()) {
                Toast.makeText(this, "Enter ZIP for build!", Toast.LENGTH_SHORT).show();
                return;
            }
            startBuild(zip);
        });
    }

    private void startBuild(String zip) {
        addLog("[*] Build started with ZIP: " + zip);
        btnBuild.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Building APK...");

        new Handler().postDelayed(() -> {
            // Simulasi build
            simulateBuild(zip);
        }, 1000);
    }

    private void simulateBuild(String zip) {
        addLog("[*] Cloning repository...");
        addLog("[*] Resolving dependencies...");
        addLog("[*] Building APK...");

        // Simulasi error detection
        String[] possibleErrors = {
            "Gradle build failed",
            "Execution failed for task ':app:assembleRelease'",
            "Could not resolve dependencies",
            "JAVA_HOME is not set",
            "SDK location not found",
            "Duplicate class found",
            "File not found",
            "Compilation failed",
            "OutOfMemoryError",
            "Connection timed out"
        };

        boolean hasError = Math.random() > 0.3;

        if (hasError) {
            String error = possibleErrors[(int)(Math.random() * possibleErrors.length)];
            addLog("[!] ERROR: " + error);
            tvStatus.setText("Error: " + error);
            tvStatus.setTextColor(0xFFFF0000);

            // Deteksi error
            String detectedError = errorDetector.detect(error);
            addLog("[*] Detected: " + detectedError);

            // Fix via AI
            addLog("[*] AI Fixing...");
            String fix = aiFixer.fix(error);
            addLog("[✓] AI Suggestion: " + fix);

            // Auto fix
            String fixed = autoFix(error);
            if (fixed != null) {
                addLog("[✓] Auto Fixed: " + fixed);
                tvStatus.setText("Fixed! Retrying...");
                tvStatus.setTextColor(0xFFFFFF00);

                // Retry build
                new Handler().postDelayed(() -> {
                    addLog("[✓] Build Success!");
                    tvStatus.setText("Build Successful!");
                    tvStatus.setTextColor(0xFF00FF00);
                    progressBar.setVisibility(View.GONE);
                    btnBuild.setEnabled(true);
                    Toast.makeText(this, "APK Built Successfully!", Toast.LENGTH_LONG).show();
                }, 2000);
            } else {
                tvStatus.setText("Cannot fix automatically. Please manual fix.");
                tvStatus.setTextColor(0xFFFF0000);
                progressBar.setVisibility(View.GONE);
                btnBuild.setEnabled(true);
            }
        } else {
            addLog("[✓] Build Success!");
            tvStatus.setText("Build Successful!");
            tvStatus.setTextColor(0xFF00FF00);
            progressBar.setVisibility(View.GONE);
            btnBuild.setEnabled(true);
            Toast.makeText(this, "APK Built Successfully!", Toast.LENGTH_LONG).show();
        }
    }

    private String autoFix(String error) {
        if (error.contains("Gradle build failed")) {
            return "Fixed gradle.properties, added JVM args";
        } else if (error.contains("Could not resolve dependencies")) {
            return "Added repositories: jcenter() and google()";
        } else if (error.contains("JAVA_HOME is not set")) {
            return "Set JAVA_HOME to /usr/lib/jvm/java-11-openjdk";
        } else if (error.contains("SDK location not found")) {
            return "Created local.properties with sdk.dir";
        } else if (error.contains("Duplicate class")) {
            return "Removed duplicate dependencies in build.gradle";
        } else if (error.contains("File not found")) {
            return "Created missing file with template";
        } else if (error.contains("OutOfMemoryError")) {
            return "Increased memory in gradle.properties";
        } else if (error.contains("Connection timed out")) {
            return "Added mirrors for dependencies";
        } else if (error.contains("Compilation failed")) {
            return "Fixed syntax errors in source code";
        } else {
            return null;
        }
    }

    private void addLog(String log) {
        tvLog.append(log + "\n");
        tvLog.setSelection(tvLog.getText().length());
    }
}
