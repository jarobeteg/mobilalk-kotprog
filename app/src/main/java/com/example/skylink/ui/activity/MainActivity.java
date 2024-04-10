package com.example.skylink.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
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
                .requestIdToken(getString(R.string.default_web_client_id))
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
            loginWithGoogle();

        });
        logout.setOnClickListener(view -> {
            logoutWithGoogle();
            recreate();
        });
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
                        } else {

                        }
                    }
                });
    }

    public void loginWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void logoutWithGoogle(){
        mAuth.signOut();
        mGoogleSignInClient.signOut();
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