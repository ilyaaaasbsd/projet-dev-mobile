package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText user = findViewById(R.id.inputUser);
        EditText pass = findViewById(R.id.inputPass);
        Button btnSignup = findViewById(R.id.btnSignup);
        TextView txtLogin = findViewById(R.id.txtLogin);

        btnSignup.setOnClickListener(v -> {
            String u = user.getText().toString().trim();
            String p = pass.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create request WITHOUT role (Default is user)
            LoginRequest req = new LoginRequest(u, p);

            RetrofitClient.getService().signup(req).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            Toast.makeText(SignupActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(SignupActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }
}