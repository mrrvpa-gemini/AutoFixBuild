package com.rvpa.autofix;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout llTheme;
    private TextView tvProfile, tvCreator;
    private Button btnThemeDark, btnThemeLight, btnThemeBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        setupListeners();
        loadTheme();
    }

    private void initViews() {
        llTheme = findViewById(R.id.llTheme);
        tvProfile = findViewById(R.id.tvProfile);
        tvCreator = findViewById(R.id.tvCreator);
        btnThemeDark = findViewById(R.id.btnThemeDark);
        btnThemeLight = findViewById(R.id.btnThemeLight);
        btnThemeBlue = findViewById(R.id.btnThemeBlue);

        tvCreator.setText("Creator: Rvpa.official");
        tvProfile.setText("👤 @Rvpa_official");
    }

    private void setupListeners() {
        btnThemeDark.setOnClickListener(v -> setTheme(0xFF121212, 0xFFFFFFFF));
        btnThemeLight.setOnClickListener(v -> setTheme(0xFFFFFFFF, 0xFF121212));
        btnThemeBlue.setOnClickListener(v -> setTheme(0xFF0A1628, 0xFF00CCFF));
    }

    private void setTheme(int bgColor, int textColor) {
        llTheme.setBackgroundColor(bgColor);

        SharedPreferences prefs = getSharedPreferences("theme", MODE_PRIVATE);
        prefs.edit()
            .putInt("bgColor", bgColor)
            .putInt("textColor", textColor)
            .apply();

        // Apply to all views
        for (int i = 0; i < llTheme.getChildCount(); i++) {
            View v = llTheme.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(textColor);
            }
            if (v instanceof Button) {
                ((Button) v).setTextColor(textColor);
            }
        }
    }

    private void loadTheme() {
        SharedPreferences prefs = getSharedPreferences("theme", MODE_PRIVATE);
        int bgColor = prefs.getInt("bgColor", 0xFF0A1628);
        int textColor = prefs.getInt("textColor", 0xFF00CCFF);
        llTheme.setBackgroundColor(bgColor);

        for (int i = 0; i < llTheme.getChildCount(); i++) {
            View v = llTheme.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(textColor);
            }
            if (v instanceof Button) {
                ((Button) v).setTextColor(textColor);
            }
        }
    }
}
