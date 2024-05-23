package com.samy.groceryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.samy.groceryapp.MainActivity;
import com.samy.groceryapp.R;

public class HomeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setup();
    }

    private void setup() {
        ImageView imageView = findViewById(R.id.img_main);
        Glide.with(this).load(R.mipmap.grocery).into(imageView);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            Toast.makeText(this, "please wait you are already logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }
        View login_ll = findViewById(R.id.login_ss);
        View register_ll = findViewById(R.id.register_ss);
        login_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        register_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
                finish();
            }
        });
    }

}