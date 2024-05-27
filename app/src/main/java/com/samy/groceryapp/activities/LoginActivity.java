package com.samy.groceryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.samy.groceryapp.R;
import com.samy.groceryapp.databinding.ActivityLoginBinding;
import com.samy.groceryapp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setup();

    }


    private void setup() {
        auth = FirebaseAuth.getInstance();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        downloadImg();
        observe();
        onClick();
    }

    private void downloadImg() {
        Glide.with(this).load(R.mipmap.loginbg).into(binding.regImg);
        binding.progressbar.setVisibility(View.GONE);

    }

    private void onClick() {
        binding.loginBtnLogin.setOnClickListener(view -> {
            binding.progressbar.setVisibility(View.VISIBLE);
            login();
        });

        binding.signUpLogin.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            finish();
        });
    }

    private void observe() {
        loginViewModel.getLoginLivedata().observe(this, response -> {
            switch (response.status) {
                case LOADING:
                    // Show loading indicator
                    break;
                case SUCCESS:
                    // Update UI with response.data
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    break;
                case ERROR:
                    // Show error message
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    break;
                case IDEL:
                    // Handle idle state if needed
                    break;
            }
        });
    }

    private void login() {
        String userEmail = binding.emailLogin.getText().toString();
        String userPassword = binding.passwordLogin.getText().toString();
        verifyEmailPassword(userEmail,userPassword);
        loginViewModel.login(binding.emailLogin.getText().toString(), binding.passwordLogin.getText().toString());

    }

    private void verifyEmailPassword(String userEmail, String userPassword) {
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Password is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password Length must be greater then 6 letter", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("emailState", binding.emailLogin.getText().toString());
        outState.putString("passState", binding.passwordLogin.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String email = savedInstanceState.getString("emailState");
        String pass = savedInstanceState.getString("passState");
        binding.emailLogin.setText(email);
        binding.passwordLogin.setText(pass);
    }

}