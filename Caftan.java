package com.example.caftanrental;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Caftan implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private boolean isFavorite;
    private String size;
    private int quantity;
    private String category; // NEW FIELD

    public Caftan() {}

    // Constructor used when adding new items
    public Caftan(String name, String description, double price, String imageUrl, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isFavorite = false;
        this.size = "M";
        this.quantity = 1;
        this.category = category;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public boolean isFavorite() { return isFavorite; }
    public String getSize() { return size; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; } // NEW

    // Setters
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setSize(String size) { this.size = size; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCategory(String category) { this.category = category; } // NEW
}