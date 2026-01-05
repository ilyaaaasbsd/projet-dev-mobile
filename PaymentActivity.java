package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get Data from Step 1
        Bundle extras = getIntent().getExtras();

        EditText cardNum = findViewById(R.id.inputCardNum);
        Button btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, OrderReviewActivity.class);
            if (extras != null) intent.putExtras(extras); // Pass previous data

            // Add Payment Data
            intent.putExtra("CARD_NUM", cardNum.getText().toString());
            startActivity(intent);
        });
    }
}