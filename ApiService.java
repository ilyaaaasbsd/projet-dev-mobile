package com.example.caftanrental;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT; // Import PUT
import retrofit2.http.Path;

public interface ApiService {
    @GET("/api/caftans")
    Call<List<Caftan>> getCaftans();

    @POST("/api/caftans")
    Call<Caftan> addCaftan(@Body Caftan caftan);

    @DELETE("/api/caftans/{id}")
    Call<Void> deleteCaftan(@Path("id") String id);

    // New Update Method
    @PUT("/api/caftans/{id}")
    Call<Caftan> updateCaftan(@Path("id") String id, @Body Caftan caftan);

    // --- CART ENDPOINTS ---

    @GET("/api/cart")
    Call<List<Caftan>> getCartItems();
    // Note: We are reusing the 'Caftan' class because the fields (name, price, image) are the same.

    @POST("/api/cart")
    Call<Void> addToCart(@Body Caftan caftan);
    // We send a Caftan object to be saved

    @DELETE("/api/cart/{id}")
    Call<Void> removeFromCart(@Path("id") String id);

    @POST("/api/orders")
    Call<Void> createOrder(@Body OrderRequest orderRequest);

    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/signup")
    Call<LoginResponse> signup(@Body LoginRequest request);
}