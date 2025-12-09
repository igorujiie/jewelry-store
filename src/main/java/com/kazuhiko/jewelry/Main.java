package com.kazuhiko.jewelry;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initialize();
        Menu menu = new Menu();
        menu.start();
    }
}
