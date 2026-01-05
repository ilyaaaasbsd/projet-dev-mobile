package com.example.caftanrental;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaftanAdapter extends RecyclerView.Adapter<CaftanAdapter.ViewHolder> {
    private Context context;
    private List<Caftan> caftanList;
    private boolean isAdmin;

    public CaftanAdapter(Context context, List<Caftan> caftanList, boolean isAdmin) {
        this.context = context;
        this.caftanList = caftanList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_caftan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Caftan caftan = caftanList.get(position);

        holder.name.setText(caftan.getName());
        holder.price.setText(caftan.getPrice() + " DH");

        // 1. Load Image
        String imageName = caftan.getImageUrl();
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (resId != 0) {
            holder.image.setImageResource(resId);
        } else {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }

        // --- 2. FAVORITE LOGIC (RESTORED) ---
        if (caftan.isFavorite()) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled);
            holder.favoriteIcon.setColorFilter(Color.RED);
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
            holder.favoriteIcon.setColorFilter(Color.BLACK);
        }

        holder.favoriteIcon.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            // Optimistic Update (Change visually immediately)
            boolean newState = !caftan.isFavorite();
            caftan.setFavorite(newState);
            notifyItemChanged(currentPos);

            // Send Update to Database
            RetrofitClient.getService().updateCaftan(caftan.getId(), caftan).enqueue(new Callback<Caftan>() {
                @Override
                public void onResponse(Call<Caftan> call, Response<Caftan> response) {
                    if (!response.isSuccessful()) {
                        // If server fails, revert back
                        revertFavorite(caftan, !newState, currentPos);
                    }
                }

                @Override
                public void onFailure(Call<Caftan> call, Throwable t) {
                    // Connection error, revert back
                    revertFavorite(caftan, !newState, currentPos);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // --- 3. NAVIGATION (Product Details) ---
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("caftan_item", caftan);
            context.startActivity(intent);
        });

        // --- 4. ADMIN PANEL (Edit & Delete) ---
        if (isAdmin) {
            holder.adminPanel.setVisibility(View.VISIBLE);

            // Delete Button
            holder.btnDelete.setOnClickListener(v -> {
                int currentPos = holder.getAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) return;

                RetrofitClient.getService().deleteCaftan(caftan.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            caftanList.remove(currentPos);
                            notifyItemRemoved(currentPos);
                            notifyItemRangeChanged(currentPos, caftanList.size());
                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            // Edit Button
            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditCaftanActivity.class);
                intent.putExtra("caftan_data", caftan);
                context.startActivity(intent);
            });

        } else {
            holder.adminPanel.setVisibility(View.GONE);
        }
    }

    // Helper to revert favorite status if API fails
    private void revertFavorite(Caftan caftan, boolean oldState, int position) {
        caftan.setFavorite(oldState);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() { return caftanList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image, favoriteIcon;
        LinearLayout adminPanel;
        ImageButton btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.itemPrice);
            image = itemView.findViewById(R.id.itemImage);
            favoriteIcon = itemView.findViewById(R.id.iconHeart);

            // Admin Views
            adminPanel = itemView.findViewById(R.id.adminPanel);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}