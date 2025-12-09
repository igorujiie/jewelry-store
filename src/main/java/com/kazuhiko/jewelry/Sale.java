package com.kazuhiko.jewelry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale {
    private int id;
    private int jewelryId;
    private int quantity;
    private double totalPrice;
    private String customerName;
    private LocalDateTime saleDate;

    public Sale() {}

    public Sale(int id, int jewelryId, int quantity, double totalPrice, String customerName, LocalDateTime saleDate) {
        this.id = id;
        this.jewelryId = jewelryId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
        this.saleDate = saleDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJewelryId() {
        return jewelryId;
    }

    public void setJewelryId(int jewelryId) {
        this.jewelryId = jewelryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateStr = saleDate != null ? saleDate.format(formatter) : "N/A";
        return String.format("Sale #%d | Qty: %d | Total: $%.2f | Customer: %s | Date: %s",
                id, quantity, totalPrice, customerName != null ? customerName : "N/A", dateStr);
    }
}
