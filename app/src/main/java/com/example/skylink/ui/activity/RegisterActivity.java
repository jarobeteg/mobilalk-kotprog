package com.example.skylink.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AbsThemeActivity {
    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordConfirmationEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.register_username);
        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        passwordConfirmationEditText = findViewById(R.id.register_password_confirm);

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
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirmation = passwordConfirmationEditText.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
            System.out.println("username or email or password or password confirmation is empty");
            return;
        }

        if (!password.equals(passwordConfirmation)) {
            System.out.println("the passwords don't match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() || task.isComplete()){
                    System.out.println("The exception: " + task.getException());
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    System.out.println("undefined error: " + task.getException());
                }
            }
        });
    }
}