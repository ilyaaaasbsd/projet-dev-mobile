package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        EditText user = findViewById(R.id.inputUser);
        EditText pass = findViewById(R.id.inputPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtSignup = findViewById(R.id.txtSignup);

        btnLogin.setOnClickListener(v -> {
            String u = user.getText().toString().trim();
            String p = pass.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest req = new LoginRequest(u, p);

            RetrofitClient.getService().login(req).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String role = response.body().getRole();
                        boolean isAdmin = "admin".equalsIgnoreCase(role);

                        Toast.makeText(LoginActivity.this, "Welcome " + u, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("IS_ADMIN", isAdmin);
                        startActivity(intent);
                        finish();
                    } else {
                        // FIX: Read actual error
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String errorMessage = jsonObject.getString("error");
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    Log.e("LoginError", t.getMessage());
                }
            });
        });

        txtSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }
}