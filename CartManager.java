package com.example.caftanrental;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Caftan> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Caftan caftan) {
        cartItems.add(caftan);
    }

    public void removeFromCart(Caftan caftan) {
        cartItems.remove(caftan);
    }

    public List<Caftan> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Caftan c : cartItems) {
            total += c.getPrice();
        }
        return total;
    }
}