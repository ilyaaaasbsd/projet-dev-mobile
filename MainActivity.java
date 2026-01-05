package com.example.caftanrental;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private boolean isAdmin = false;

    // Filter Buttons
    private Button btnAll, btnNew, btnDresses, btnEvening;

    // Data List
    private List<Caftan> allCaftans = new ArrayList<>(); // Master List

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        isAdmin = getIntent().getBooleanExtra("IS_ADMIN", false);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAdd);

        // Find Buttons
        btnAll = findViewById(R.id.btnCatAll);
        btnNew = findViewById(R.id.btnCatNew);
        btnDresses = findViewById(R.id.btnCatDresses);
        btnEvening = findViewById(R.id.btnCatEvening);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setNestedScrollingEnabled(false);

        if (isAdmin) fabAdd.setVisibility(View.VISIBLE);
        else fabAdd.setVisibility(View.GONE);

        fabAdd.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddCaftanActivity.class)));

        // CLICK LISTENERS FOR CATEGORIES
        btnAll.setOnClickListener(v -> filterList("All", btnAll));
        btnNew.setOnClickListener(v -> filterList("New In", btnNew));
        btnDresses.setOnClickListener(v -> filterList("Dresses", btnDresses));
        btnEvening.setOnClickListener(v -> filterList("Evening", btnEvening));

        setupNavigation();
        fetchCaftans();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCaftans();
    }

    private void fetchCaftans() {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getService().getCaftans().enqueue(new Callback<List<Caftan>>() {
            @Override
            public void onResponse(Call<List<Caftan>> call, Response<List<Caftan>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    allCaftans = response.body(); // Save ALL items
                    filterList("All", btnAll);    // Show ALL initially
                }
            }
            @Override
            public void onFailure(Call<List<Caftan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList(String category, Button selectedBtn) {
        // 1. Reset Visuals
        resetButtons();
        selectedBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        selectedBtn.setTextColor(Color.WHITE);

        // 2. Filter Data
        List<Caftan> filtered = new ArrayList<>();
        if (category.equals("All")) {
            filtered.addAll(allCaftans);
        } else {
            for (Caftan c : allCaftans) {
                if (c.getCategory() != null && c.getCategory().equalsIgnoreCase(category)) {
                    filtered.add(c);
                }
            }
        }

        // 3. Update Adapter
        CaftanAdapter adapter = new CaftanAdapter(MainActivity.this, filtered, isAdmin);
        recyclerView.setAdapter(adapter);
    }

    private void resetButtons() {
        int grey = Color.parseColor("#F5F5F5");
        int black = Color.BLACK;

        setBtnStyle(btnAll, grey, black);
        setBtnStyle(btnNew, grey, black);
        setBtnStyle(btnDresses, grey, black);
        setBtnStyle(btnEvening, grey, black);
    }

    private void setBtnStyle(Button btn, int bgColor, int txtColor) {
        btn.setBackgroundTintList(ColorStateList.valueOf(bgColor));
        btn.setTextColor(txtColor);
    }

    private void setupNavigation() {
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navSearch = findViewById(R.id.navSearch);
        ImageView navFav = findViewById(R.id.navFav);
        ImageView btnShoppingBag = findViewById(R.id.btnShoppingBag);

        navHome.setOnClickListener(v -> filterList("All", btnAll));
        navSearch.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));
        navFav.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavoritesActivity.class)));
        if (btnShoppingBag != null) {
            btnShoppingBag.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        }
    }
}