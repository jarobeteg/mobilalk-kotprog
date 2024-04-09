package com.example.skylink.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;

public class RegisterActivity extends AbsThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton registerCheckmark = findViewById(R.id.register_profile_checkmark);
        Toolbar registerToolbar = findViewById(R.id.register_profile_toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        };

        registerToolbar.setNavigationOnClickListener(v -> onBackPressedCallback.handleOnBackPressed());

        registerCheckmark.setOnClickListener(v -> {
            register();
        });

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    private void register() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}