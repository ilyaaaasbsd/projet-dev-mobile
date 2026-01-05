package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText searchInput;
    private CaftanAdapter adapter;
    private List<Caftan> allCaftans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navFav = findViewById(R.id.navFav);

        fetchCaftans();

        navHome.setOnClickListener(v -> finish());


        navFav.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, FavoritesActivity.class));
            finish();
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchCaftans() {
        RetrofitClient.getService().getCaftans().enqueue(new Callback<List<Caftan>>() {
            @Override
            public void onResponse(Call<List<Caftan>> call, Response<List<Caftan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allCaftans = response.body();
                    // Initial load
                    adapter = new CaftanAdapter(SearchActivity.this, allCaftans, false);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override public void onFailure(Call<List<Caftan>> call, Throwable t) {}
        });
    }

    private void filter(String text) {
        List<Caftan> filteredList = new ArrayList<>();
        for (Caftan item : allCaftans) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter = new CaftanAdapter(SearchActivity.this, filteredList, false);
        recyclerView.setAdapter(adapter);
    }
}