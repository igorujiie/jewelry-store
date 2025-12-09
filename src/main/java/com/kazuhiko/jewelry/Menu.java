package com.kazuhiko.jewelry;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final InventoryDAO inventoryDAO;
    private final SaleDAO saleDAO;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.inventoryDAO = new InventoryDAO();
        this.saleDAO = new SaleDAO();
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> viewInventory();
                case 2 -> addJewelry();
                case 3 -> updateJewelry();
                case 4 -> deleteJewelry();
                case 5 -> recordSale();
                case 6 -> viewSales();
                case 7 -> searchJewelry();
                case 0 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n========== JEWELRY STORE ==========");
        System.out.println("1. View Inventory");
        System.out.println("2. Add Jewelry");
        System.out.println("3. Update Jewelry");
        System.out.println("4. Delete Jewelry");
        System.out.println("5. Record Sale");
        System.out.println("6. View Sales");
        System.out.println("7. Search Jewelry");
        System.out.println("0. Exit");
        System.out.println("====================================");
    }

    private void viewInventory() {
        List<Jewelry> items = inventoryDAO.findAll();
        if (items.isEmpty()) {
            System.out.println("No items in inventory.");
            return;
        }
        System.out.println("\n--- Inventory ---");
        for (Jewelry item : items) {
            System.out.println(item);
        }
    }

    private void addJewelry() {
        System.out.println("\n--- Add New Jewelry ---");
        String name = readString("Name: ");
        String type = readString("Type (Ring/Necklace/Bracelet/Earring/Other): ");
        String material = readString("Material (Gold/Silver/Platinum/Other): ");
        double price = readDouble("Price: ");
        int quantity = readInt("Quantity: ");

        Jewelry jewelry = new Jewelry(0, name, type, material, price, quantity);
        inventoryDAO.insert(jewelry);
        System.out.println("Jewelry added successfully!");
    }

    private void updateJewelry() {
        System.out.println("\n--- Update Jewelry ---");
        int id = readInt("Enter jewelry ID to update: ");
        Jewelry existing = inventoryDAO.findById(id);
        if (existing == null) {
            System.out.println("Jewelry not found.");
            return;
        }
        System.out.println("Current: " + existing);
        
        String name = readString("New Name (press Enter to keep current): ");
        String type = readString("New Type (press Enter to keep current): ");
        String material = readString("New Material (press Enter to keep current): ");
        String priceStr = readString("New Price (press Enter to keep current): ");
        String quantityStr = readString("New Quantity (press Enter to keep current): ");

        if (!name.isEmpty()) existing.setName(name);
        if (!type.isEmpty()) existing.setType(type);
        if (!material.isEmpty()) existing.setMaterial(material);
        if (!priceStr.isEmpty()) existing.setPrice(Double.parseDouble(priceStr));
        if (!quantityStr.isEmpty()) existing.setQuantity(Integer.parseInt(quantityStr));

        inventoryDAO.update(existing);
        System.out.println("Jewelry updated successfully!");
    }

    private void deleteJewelry() {
        System.out.println("\n--- Delete Jewelry ---");
        int id = readInt("Enter jewelry ID to delete: ");
        Jewelry existing = inventoryDAO.findById(id);
        if (existing == null) {
            System.out.println("Jewelry not found.");
            return;
        }
        System.out.println("Deleting: " + existing);
        inventoryDAO.delete(id);
        System.out.println("Jewelry deleted successfully!");
    }

    private void recordSale() {
        System.out.println("\n--- Record Sale ---");
        int jewelryId = readInt("Enter jewelry ID: ");
        Jewelry jewelry = inventoryDAO.findById(jewelryId);
        if (jewelry == null) {
            System.out.println("Jewelry not found.");
            return;
        }
        System.out.println("Selected: " + jewelry);
        
        int quantity = readInt("Quantity to sell: ");
        if (quantity > jewelry.getQuantity()) {
            System.out.println("Insufficient stock. Available: " + jewelry.getQuantity());
            return;
        }

        double totalPrice = jewelry.getPrice() * quantity;
        String customerName = readString("Customer name: ");

        Sale sale = new Sale(0, jewelryId, quantity, totalPrice, customerName, null);
        saleDAO.insert(sale);

        jewelry.setQuantity(jewelry.getQuantity() - quantity);
        inventoryDAO.update(jewelry);

        System.out.println("Sale recorded! Total: $" + String.format("%.2f", totalPrice));
    }

    private void viewSales() {
        List<Sale> sales = saleDAO.findAll();
        if (sales.isEmpty()) {
            System.out.println("No sales recorded.");
            return;
        }
        System.out.println("\n--- Sales History ---");
        for (Sale sale : sales) {
            Jewelry jewelry = inventoryDAO.findById(sale.getJewelryId());
            String jewelryName = jewelry != null ? jewelry.getName() : "Unknown";
            System.out.println(sale + " | Item: " + jewelryName);
        }
    }

    private void searchJewelry() {
        System.out.println("\n--- Search Jewelry ---");
        String keyword = readString("Enter search keyword: ");
        List<Jewelry> results = inventoryDAO.search(keyword);
        if (results.isEmpty()) {
            System.out.println("No items found.");
            return;
        }
        System.out.println("Found " + results.size() + " item(s):");
        for (Jewelry item : results) {
            System.out.println(item);
        }
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
