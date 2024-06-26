package com.example.skylink.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.skylink.R;
import com.example.skylink.adapter.MainViewPagerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AbsThemeActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar mainToolbar;
    private ViewPager2 viewPager2;
    private MainViewPagerAdapter adapter;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        ImageButton login = findViewById(R.id.login_button);
        ImageButton logout = findViewById(R.id.logout_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //dumbo android studio thinks this is a bug
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.main_view_pager);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        adapter = new MainViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                updateToolbarTitle(position);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_fragment_home){
                viewPager2.setCurrentItem(0, true);
            } else if (menuItem.getItemId() == R.id.nav_fragment_bookings){
                viewPager2.setCurrentItem(1, true);
            } else if (menuItem.getItemId() == R.id.nav_fragment_profile){
                viewPager2.setCurrentItem(2, true);
            } else if (menuItem.getItemId() == R.id.nav_fragment_more){
                viewPager2.setCurrentItem(3, true);
            }
            return true;
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (viewPager2.getCurrentItem() != 0) {
                    viewPager2.setCurrentItem(0, true);
                } else {
                    finish();
                }
            }
        });

        login.setOnClickListener(v -> {
            loginDialog();

        });

        logout.setOnClickListener(view -> {
            logoutWithGoogle();
        });

        loginAsGuest();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mGoogleSignInClient.signOut();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            recreate();
                            showToast(getString(R.string.login_success));
                        }
                    }
                });
    }

    private void loginDialog(){
        View dialogView = LayoutInflater.from(this).inflate(R.layout.login_dialog, null);

        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        Button buttonLogin = dialogView.findViewById(R.id.buttonLogin);
        Button buttonGoogleSignIn = dialogView.findViewById(R.id.buttonGoogleSignIn);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        recreate();
                        showToast(getString(R.string.login_success));
                    }
                }
            });
            dialog.dismiss();
        });

        buttonGoogleSignIn.setOnClickListener(v -> {
            loginWithGoogle();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void loginWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginAsGuest() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signInAnonymously();
        }
    }

    public void logoutWithGoogle(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user.isAnonymous()){
            showToast(getString(R.string.cant_logout_anonymous));
            return;
        }
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    recreate();
                    showToast(getString(R.string.logout_success));
                }
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                System.out.println(e.getStatus());
            }
        }
    }

    private void updateToolbarTitle(int position){
        String newTitle;
        switch (position) {
            case 1:
                newTitle = getString(R.string.bottom_nav_bookings);
                break;
            case 2:
                newTitle = getString(R.string.bottom_nav_profile);
                break;
            case 3:
                newTitle = getString(R.string.bottom_nav_more);
                break;
            default:
                newTitle = getString(R.string.app_name);
                break;
        }
        TextView textView = findViewById(R.id.main_toolbar_title);
        if (textView != null){
            textView.setText(newTitle);
        }
    }
}