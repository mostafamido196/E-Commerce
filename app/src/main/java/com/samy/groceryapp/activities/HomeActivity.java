package com.samy.groceryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.samy.groceryapp.R;
import com.samy.groceryapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

//    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setup();
    }

    private void setup() {
        Glide.with(this).load(R.mipmap.grocery).into(binding.imgMain);
        binding.progressbar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            binding.progressbar.setVisibility(View.VISIBLE);
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            Toast.makeText(this, "please wait you are already logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }
        binding.loginSs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressbar.setVisibility(View.GONE);
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        binding.registerSs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressbar.setVisibility(View.GONE);
                startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
                finish();
            }
        });
    }

}