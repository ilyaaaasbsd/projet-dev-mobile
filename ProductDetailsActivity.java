package com.example.caftanrental;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private String selectedSize = "M";
    private int quantity = 1;
    private TextView txtQty;

    private Button btnXS, btnS, btnM, btnL, btnXL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Get the passed object
        Caftan caftan = (Caftan) getIntent().getSerializableExtra("caftan_item");

        if (caftan == null) return;

        // Initialize Views
        ImageView image = findViewById(R.id.detailImage);
        TextView price = findViewById(R.id.detailPrice);
        TextView oldPrice = findViewById(R.id.detailOldPrice);
        TextView desc = findViewById(R.id.detailDescription);
        Button btnAdd = findViewById(R.id.btnAddToCart);
        txtQty = findViewById(R.id.txtQty);
        TextView btnPlus = findViewById(R.id.btnQtyPlus);
        TextView btnMinus = findViewById(R.id.btnQtyMinus);

        // Set Basic Data
        price.setText(caftan.getPrice() + " DH");
        desc.setText(caftan.getDescription());
        btnAdd.setText("Add to Cart - " + caftan.getPrice() + " DH");

        // Image Loading
        String imageName = caftan.getImageUrl();
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (resId != 0) {
            image.setImageResource(resId);
        } else {
            image.setImageResource(R.mipmap.ic_launcher);
        }

        // Strikethrough Old Price
        if (oldPrice != null) {
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // --- SIZE SELECTION ---
        btnXS = findViewById(R.id.btnSizeXS);
        btnS = findViewById(R.id.btnSizeS);
        btnM = findViewById(R.id.btnSizeM);
        btnL = findViewById(R.id.btnSizeL);
        btnXL = findViewById(R.id.btnSizeXL);

        btnXS.setOnClickListener(v -> selectSize("XS", btnXS));
        btnS.setOnClickListener(v -> selectSize("S", btnS));
        btnM.setOnClickListener(v -> selectSize("M", btnM));
        btnL.setOnClickListener(v -> selectSize("L", btnL));
        btnXL.setOnClickListener(v -> selectSize("XL", btnXL));

        selectSize("M", btnM);

        // --- QUANTITY ---
        btnPlus.setOnClickListener(v -> {
            quantity++;
            txtQty.setText(String.valueOf(quantity));
            btnAdd.setText("Add to Cart - " + (caftan.getPrice() * quantity) + " DH");
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQty.setText(String.valueOf(quantity));
                btnAdd.setText("Add to Cart - " + (caftan.getPrice() * quantity) + " DH");
            }
        });

        // --- ADD TO CART ---
        btnAdd.setOnClickListener(v -> {
            // FIX IS HERE: Added 'caftan.getCategory()' as the 5th argument
            Caftan cartItem = new Caftan(
                    caftan.getName(),
                    caftan.getDescription(),
                    caftan.getPrice(),
                    caftan.getImageUrl(),
                    caftan.getCategory() // <--- ADDED THIS
            );

            cartItem.setSize(selectedSize);
            cartItem.setQuantity(quantity);

            RetrofitClient.getService().addToCart(cartItem).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "Error adding to cart", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ProductDetailsActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void selectSize(String size, Button selectedBtn) {
        selectedSize = size;
        resetButton(btnXS);
        resetButton(btnS);
        resetButton(btnM);
        resetButton(btnL);
        resetButton(btnXL);

        selectedBtn.setBackgroundColor(Color.BLACK);
        selectedBtn.setTextColor(Color.WHITE);
    }

    private void resetButton(Button btn) {
        btn.setBackgroundResource(R.drawable.bg_size_item);
        btn.setTextColor(Color.BLACK);
    }
}