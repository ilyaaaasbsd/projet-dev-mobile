package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderReviewActivity extends AppCompatActivity {

    private double finalTotal = 0;
    private List<Caftan> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);

        Bundle extras = getIntent().getExtras();
        TextView txtAddress = findViewById(R.id.txtAddressSummary);
        TextView txtPayment = findViewById(R.id.txtPaymentSummary);
        TextView txtTotal = findViewById(R.id.txtOrderTotal);
        Button btnPlace = findViewById(R.id.btnPlaceOrder);

        // Display Info
        if (extras != null) {
            String fullAddress = extras.getString("NAME") + "\n" +
                    extras.getString("ADDRESS") + "\n" +
                    extras.getString("CITY") + ", " + extras.getString("ZIP");
            txtAddress.setText(fullAddress);
            txtPayment.setText("Card ending in " + extras.getString("CARD_NUM").substring(Math.max(0, extras.getString("CARD_NUM").length() - 4)));
        }

        // Fetch Cart Items to Calculate Total & Prepare for Order
        RetrofitClient.getService().getCartItems().enqueue(new Callback<List<Caftan>>() {
            @Override
            public void onResponse(Call<List<Caftan>> call, Response<List<Caftan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems = response.body();
                    for (Caftan c : cartItems) finalTotal += c.getPrice();
                    txtTotal.setText(finalTotal + " DH");
                }
            }
            @Override
            public void onFailure(Call<List<Caftan>> call, Throwable t) {}
        });

        // Place Order
        btnPlace.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) return;

            OrderRequest order = new OrderRequest(
                    extras.getString("NAME"),
                    extras.getString("ADDRESS"),
                    extras.getString("CITY"),
                    extras.getString("ZIP"),
                    extras.getString("PHONE"),
                    extras.getString("CARD_NUM"),
                    finalTotal,
                    cartItems
            );

            RetrofitClient.getService().createOrder(order).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(OrderReviewActivity.this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();
                        // Go back to Home
                        Intent intent = new Intent(OrderReviewActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(OrderReviewActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}