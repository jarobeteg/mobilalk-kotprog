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

import com.example.skylink.R;
import com.example.skylink.ui.activity.RegisterActivity;
import com.example.skylink.ui.viewmodel.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

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
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null){
            System.out.println("Authenticated user");
            System.out.println(user.getDisplayName());
            System.out.println(user.getEmail());
        } else {
            System.out.println("Unauthenticated user");
        }

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
                        System.out.println("register was successful");
                    } else if (result.getResultCode() == RegisterActivity.RESULT_CANCELED) {
                        System.out.println("registration was cancelled by the user");
                    }
                });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}