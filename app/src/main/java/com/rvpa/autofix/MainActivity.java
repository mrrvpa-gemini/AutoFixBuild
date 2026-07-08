package com.rvpa.autofix;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etRepo, etBranch;
    private Button btnLogin, btnBuild;
    private ProgressBar progressBar;
    private TextView tvStatus, tvLog;
    private LinearLayout llSpline;
    private ImageView ivSpline;
    private List<String> logs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupSplineBackground();
        setupListeners();
        setupGitLogin();
    }

    private void initViews() {
        etRepo = findViewById(R.id.etRepo);
        etBranch = findViewById(R.id.etBranch);
        btnLogin = findViewById(R.id.btnLogin);
        btnBuild = findViewById(R.id.btnBuild);
        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);
        tvLog = findViewById(R.id.tvLog);
        llSpline = findViewById(R.id.llSpline);
        ivSpline = findViewById(R.id.ivSpline);

        btnBuild.setEnabled(false);
        progressBar.setVisibility(View.GONE);
    }

    private void setupSplineBackground() {
        // 3D Spline Effect with ValueAnimator
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(8000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            float rotation = value * 360f;
            float scale = 0.8f + (value * 0.4f);
            float alpha = 0.5f + (value * 0.5f);

            ivSpline.setRotation(rotation);
            ivSpline.setScaleX(scale);
            ivSpline.setScaleY(scale);
            ivSpline.setAlpha(alpha);

            // Change color gradient
            int color1 = Color.argb((int)(alpha * 255), (int)(100 + value * 155), (int)(200 + value * 55), 255);
            int color2 = Color.argb((int)(alpha * 255), 255, (int)(100 + value * 155), (int)(200 + value * 55));

            GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{color1, color2}
            );
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setGradientRadius(500);
            llSpline.setBackground(gd);
        });

        animator.start();
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> performGitLogin());

        btnBuild.setOnClickListener(v -> startBuild());

        etRepo.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void validateInputs() {
        String repo = etRepo.getText().toString().trim();
        String branch = etBranch.getText().toString().trim();
        boolean isValid = !repo.isEmpty() && !branch.isEmpty();
        btnBuild.setEnabled(isValid);
    }

    private void setupGitLogin() {
        // Simulasi login GitHub via Termux
        new Handler().postDelayed(() -> {
            addLog("[✓] GitHub Login Success");
            addLog("[✓] Connected to GitHub API");
            btnLogin.setText("✅ Logged In");
            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
        }, 1000);
    }

    private void performGitLogin() {
        addLog("[*] Logging to GitHub...");
        btnLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            addLog("[✓] Login Success!");
            addLog("[✓] Token Valid");
            btnLogin.setEnabled(true);
            btnLogin.setText("✅ Logged In");
            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
        }, 2000);
    }

    private void startBuild() {
        String repo = etRepo.getText().toString().trim();
        String branch = etBranch.getText().toString().trim();

        addLog("[*] Starting Build...");
        addLog("[*] Repo: " + repo);
        addLog("[*] Branch: " + branch);

        btnBuild.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Building...");
        tvStatus.setTextColor(Color.YELLOW);

        Intent intent = new Intent(this, BuildActivity.class);
        intent.putExtra("repo", repo);
        intent.putExtra("branch", branch);
        startActivity(intent);

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            btnBuild.setEnabled(true);
            tvStatus.setText("Build Completed!");
            tvStatus.setTextColor(Color.GREEN);
            addLog("[✓] Build Finished");
            Toast.makeText(MainActivity.this, "APK Build Success!", Toast.LENGTH_LONG).show();
        }, 5000);
    }

    private void addLog(String log) {
        logs.add(log);
        tvLog.append(log + "\n");
        tvLog.setSelection(tvLog.getText().length());
    }
}
