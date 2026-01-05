package com.example.caftanrental;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<Caftan> cartList;

    public CartAdapter(Context context, List<Caftan> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Links to your professional card layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Caftan caftan = cartList.get(position);

        // 1. Set Text Data
        holder.name.setText(caftan.getName());
        holder.price.setText(caftan.getPrice() + " DH");

        // 2. Load Image (Local Drawable)
        String imageName = caftan.getImageUrl();
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (resId != 0) {
            holder.image.setImageResource(resId);
        } else {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }

        // 3. Delete Logic (Connected to Database)
        holder.btnRemove.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            // Call API to remove from MongoDB
            RetrofitClient.getService().removeFromCart(caftan.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Success: Remove from screen
                        cartList.remove(currentPos);
                        notifyItemRemoved(currentPos);
                        notifyItemRangeChanged(currentPos, cartList.size());

                        // Update Total Price in the Activity
                        if (context instanceof CartActivity) {
                            ((CartActivity) context).updateTotalPrice();
                        }

                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // Connects to the IDs in item_cart.xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;
        ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cartName);
            price = itemView.findViewById(R.id.cartPrice);
            image = itemView.findViewById(R.id.cartImage);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}