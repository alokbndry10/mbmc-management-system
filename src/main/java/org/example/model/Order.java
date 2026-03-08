package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private String customerName;
    private String status;
    private double totalPrice;
    private String createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(int id, String customerName, String status, double totalPrice, String createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void addItem(OrderItem item) { this.items.add(item); }
}

