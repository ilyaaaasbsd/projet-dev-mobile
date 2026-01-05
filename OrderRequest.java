package com.example.caftanrental;

import java.io.Serializable;
import java.util.List;

public class OrderRequest implements Serializable {
    private String customerName;
    private String address;
    private String city;
    private String zipCode;
    private String phoneNumber;
    private String cardNumber;
    private double totalAmount;
    private List<Caftan> items;

    public OrderRequest(String customerName, String address, String city, String zipCode, String phoneNumber, String cardNumber, double totalAmount, List<Caftan> items) {
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.cardNumber = cardNumber;
        this.totalAmount = totalAmount;
        this.items = items;
    }
}