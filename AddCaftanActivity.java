package com.example.caftanrental;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCaftanActivity extends AppCompatActivity {

    private EditText inputName, inputDesc, inputPrice, inputImage;
    private Spinner spinnerCategory;
    private Button btnSave;

    // New UI references for Image Upload
    private FrameLayout btnUploadImage;
    private ImageView ivImagePreview;
    private LinearLayout layoutImagePlaceholder;

    // Launcher for the Gallery Intent
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ERROR FIX: Changed from layout_add_product to activity_add_caftan
        setContentView(R.layout.activity_add_caftan);

        // 1. Initialize Views
        inputName = findViewById(R.id.inputName);
        inputDesc = findViewById(R.id.inputDesc);
        inputPrice = findViewById(R.id.inputPrice);
        inputImage = findViewById(R.id.inputImage); // The hidden field
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Initialize Image Upload Views
        btnUploadImage = findViewById(R.id.btnUploadImage);
        ivImagePreview = findViewById(R.id.ivImagePreview);
        layoutImagePlaceholder = findViewById(R.id.layoutImagePlaceholder);

        // 2. Setup Back Button
        btnBack.setOnClickListener(v -> finish());

        // 3. Setup Categories Spinner
        String[] categories = {"New In", "Dresses", "Evening", "Wedding"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // 4. Configure Image Picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Get the Uri of the selected image
                        Uri selectedImageUri = result.getData().getData();

                        if (selectedImageUri != null) {
                            // Display the image in the card
                            ivImagePreview.setImageURI(selectedImageUri);

                            // Hide the "Tap to add image" placeholder
                            layoutImagePlaceholder.setVisibility(View.GONE);

                            // Save the URI string to the hidden EditText so it can be sent to DB
                            inputImage.setText(selectedImageUri.toString());
                        }
                    }
                }
        );

        // 5. Setup Upload Button Click
        btnUploadImage.setOnClickListener(v -> {
            // Open Gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // 6. Save Logic
        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String desc = inputDesc.getText().toString();
            String priceStr = inputPrice.getText().toString();
            String imageUriString = inputImage.getText().toString();
            String selectedCategory = spinnerCategory.getSelectedItem().toString();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill in Name and Price", Toast.LENGTH_SHORT).show();
                return;
            }

            // Optional: Check if image is selected
            if (imageUriString.isEmpty()) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create Object with Category
            Caftan newCaftan = new Caftan(name, desc, Double.parseDouble(priceStr), imageUriString, selectedCategory);

            RetrofitClient.getService().addCaftan(newCaftan).enqueue(new Callback<Caftan>() {
                @Override
                public void onResponse(Call<Caftan> call, Response<Caftan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddCaftanActivity.this, "Caftan Added Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddCaftanActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Caftan> call, Throwable t) {
                    Toast.makeText(AddCaftanActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}