package com.example.skylink.ui.fragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skylink.R;
import com.example.skylink.ui.activity.RegisterActivity;
import com.example.skylink.ui.viewmodel.ProfileViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    TextView usernameTextView;
    TextView emailTextView;
    private ProfileViewModel mViewModel;
    private ActivityResultLauncher<Intent> registerLauncher;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.username);
        emailTextView = view.findViewById(R.id.email);

        Button register = view.findViewById(R.id.register_button);

        register.setOnClickListener(v -> {
            register.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        register.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start();
                    })
                    .start();

            registerLauncher.launch(new Intent(requireActivity(), RegisterActivity.class));
        });

        registerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RegisterActivity.RESULT_OK){
                        assert result.getData() != null;
                        addUser(result.getData().getStringExtra("username"), result.getData().getStringExtra("email"));
                    }
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }

    private void updateUI(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null){
            usernameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
        }
    }

    private void updateUI(String username, String email){
        usernameTextView.setText(username);
        emailTextView.setText(email);
    }

    private void showToast(String message){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void addUser(String username, String email){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("email", email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateUI(username, email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error: " + e.getMessage());
                    }
                });
    }

    private void loadUser(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null){
            return;
        }

        if (user.isAnonymous()){
            updateUI(getString(R.string.anonymous), getString(R.string.anonymous));
            return;
        }

        String userId = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");

                    updateUI(username, email);
                } else {
                    updateUI();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Error: " + e.getMessage());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}