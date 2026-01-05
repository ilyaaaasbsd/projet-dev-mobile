package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        EditText name = findViewById(R.id.inputName);
        EditText address = findViewById(R.id.inputAddress);
        EditText city = findViewById(R.id.inputCity);
        EditText zip = findViewById(R.id.inputZip);
        EditText phone = findViewById(R.id.inputPhone);
        Button btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            if (name.getText().toString().isEmpty() || address.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
            // Pass the data to the next step
            intent.putExtra("NAME", name.getText().toString());
            intent.putExtra("ADDRESS", address.getText().toString());
            intent.putExtra("CITY", city.getText().toString());
            intent.putExtra("ZIP", zip.getText().toString());
            intent.putExtra("PHONE", phone.getText().toString());
            startActivity(intent);
        });
    }
}