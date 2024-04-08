package com.example.skylink.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skylink.util.ThemeUtil;

abstract public class AbsThemeActivity extends AppCompatActivity {
    private int themeStyle = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int newThemeStyle = new ThemeUtil(this).setThemeType();

        if (themeStyle != newThemeStyle) {
            themeStyle = newThemeStyle;
            setTheme(themeStyle);
            recreate();
        }
    }

    protected void updateTheme(){
        themeStyle = new ThemeUtil(this).setThemeType();
        setTheme(themeStyle);
    }
}
