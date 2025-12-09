package com.kazuhiko.jewelry;

public class Jewelry {
    private int id;
    private String name;
    private String type;
    private String material;
    private double price;
    private int quantity;

    public Jewelry() {}

    public Jewelry(int id, String name, String type, String material, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.material = material;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Type: %s | Material: %s | Price: $%.2f | Stock: %d",
                id, name, type, material, price, quantity);
    }
}
