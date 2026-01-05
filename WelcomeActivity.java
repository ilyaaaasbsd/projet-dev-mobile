package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        TextView btnSignIn = findViewById(R.id.btnSignIn);

        // BOTH buttons now go to Login, where user can choose to Sign Up
        btnGetStarted.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        });

        btnSignIn.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        });
    }
}