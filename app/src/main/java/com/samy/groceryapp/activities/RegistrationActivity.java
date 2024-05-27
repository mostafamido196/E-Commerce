package com.samy.groceryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.AuthResult;
import com.samy.groceryapp.R;
import com.samy.groceryapp.databinding.ActivityRegistrationBinding;
import com.samy.groceryapp.viewmodel.RegisterViewModel;

public class RegistrationActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;

    private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding.setViewModel(registerViewModel);
        binding.setLifecycleOwner(this);

        setup();
    }


    private void setup() {
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Using Glide:
        Glide.with(this).load(R.mipmap.regbg).into(binding.regImg);

        observe();

    }



    private void observe() {
        registerViewModel.getRegisterLivedata().observe(this, response -> {
            switch (response.status) {
                case LOADING:
                    // Show loading indicator
                    Log.d("mos samy", "LOADING ");
                    break;
                case SUCCESS:
                    // Update UI with response.data
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    Log.d("mos samy", "SUCCESS: " + ((AuthResult) response.data));
                    break;
                case ERROR:
                    // Show error message
                    Toast.makeText(RegistrationActivity.this, "error: " + response.error, Toast.LENGTH_LONG).show();
                    Log.d("mos samy", "ERROR: " + response.error);
                    break;
                case IDEL:
                    // Handle idle state if needed
                    Log.d("mos samy", "IDEL");
                    break;
            }
        });

        registerViewModel.get_signUpResult().observe(this, result -> {
            Toast.makeText(this, result + "", Toast.LENGTH_SHORT).show();
        });


        registerViewModel.get_signInClick().observe(this, result -> {
            if ((Boolean) result) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });


    }



}