package com.example.caftanrental;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCaftanActivity extends AppCompatActivity {

    private EditText editName, editDesc, editPrice, editImage;
    private Spinner spinnerCategory;
    private Button btnUpdate;
    private Caftan currentCaftan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_caftan);

        // 1. Get Passed Object
        currentCaftan = (Caftan) getIntent().getSerializableExtra("caftan_data");

        // 2. Init Views
        editName = findViewById(R.id.editName);
        editDesc = findViewById(R.id.editDesc);
        editPrice = findViewById(R.id.editPrice);
        editImage = findViewById(R.id.editImage);
        spinnerCategory = findViewById(R.id.spinnerEditCategory);
        btnUpdate = findViewById(R.id.btnUpdate);

        // 3. Setup Category Spinner
        String[] categories = {"New In", "Dresses", "Evening", "Wedding"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // 4. Pre-fill Data
        if (currentCaftan != null) {
            editName.setText(currentCaftan.getName());
            editDesc.setText(currentCaftan.getDescription());
            editPrice.setText(String.valueOf(currentCaftan.getPrice()));
            editImage.setText(currentCaftan.getImageUrl());

            // Set Spinner Selection
            if (currentCaftan.getCategory() != null) {
                int spinnerPosition = adapter.getPosition(currentCaftan.getCategory());
                spinnerCategory.setSelection(spinnerPosition);
            }
        }

        // 5. Update Logic
        btnUpdate.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String desc = editDesc.getText().toString();
            double price = Double.parseDouble(editPrice.getText().toString());
            String image = editImage.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            // Update Object
            Caftan updatedCaftan = new Caftan(name, desc, price, image, category);

            // Call API
            RetrofitClient.getService().updateCaftan(currentCaftan.getId(), updatedCaftan).enqueue(new Callback<Caftan>() {
                @Override
                public void onResponse(Call<Caftan> call, Response<Caftan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditCaftanActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Close screen
                    } else {
                        Toast.makeText(EditCaftanActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Caftan> call, Throwable t) {
                    Toast.makeText(EditCaftanActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}