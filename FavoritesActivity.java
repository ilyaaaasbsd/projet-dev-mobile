package com.example.caftanrental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navSearch = findViewById(R.id.navSearch);

        navHome.setOnClickListener(v -> finish());


        navSearch.setOnClickListener(v -> {
            startActivity(new Intent(FavoritesActivity.this, SearchActivity.class));
            finish();
        });

        RetrofitClient.getService().getCaftans().enqueue(new Callback<List<Caftan>>() {
            @Override
            public void onResponse(Call<List<Caftan>> call, Response<List<Caftan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Caftan> favs = new ArrayList<>();
                    for (Caftan c : response.body()) {
                        if (c.isFavorite()) favs.add(c);
                    }
                    recyclerView.setAdapter(new CaftanAdapter(FavoritesActivity.this, favs, false));
                }
            }
            @Override public void onFailure(Call<List<Caftan>> call, Throwable t) {}
        });
    }
}