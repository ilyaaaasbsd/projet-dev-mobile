package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private TextView txtTotal; // Class variable so we can access it later
    TextView btnPlus = findViewById(R.id.btnQtyPlus);
    TextView btnMinus = findViewById(R.id.btnQtyMinus);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        

        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        txtTotal = findViewById(R.id.txtTotal);
        Button btnCheckout = findViewById(R.id.btnCheckout);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // FETCH FROM SERVER
        fetchCartItems();
        btnCheckout.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
        });



    }

    private void fetchCartItems() {
        RetrofitClient.getService().getCartItems().enqueue(new retrofit2.Callback<List<Caftan>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Caftan>> call, retrofit2.Response<List<Caftan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Caftan> cartList = response.body();

                    // Setup Adapter with Server Data
                    CartAdapter adapter = new CartAdapter(CartActivity.this, cartList);
                    RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
                    recyclerView.setAdapter(adapter);

                    // Calculate Total
                    double total = 0;
                    for (Caftan c : cartList) {
                        total += c.getPrice();
                    }
                    txtTotal.setText(total + " DH");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Caftan>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Could not load cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Update the method used by Adapter to refresh the total/list
    public void updateTotalPrice() {
        // Re-fetch everything to be safe and accurate
        fetchCartItems();
    }

    // This method is called by the Adapter when an item is deleted


}