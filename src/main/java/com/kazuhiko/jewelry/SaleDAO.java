package com.kazuhiko.jewelry;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    public void insert(Sale sale) {
        String sql = "INSERT INTO sales (jewelry_id, quantity, total_price, customer_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sale.getJewelryId());
            pstmt.setInt(2, sale.getQuantity());
            pstmt.setDouble(3, sale.getTotalPrice());
            pstmt.setString(4, sale.getCustomerName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting sale: " + e.getMessage());
        }
    }

    public List<Sale> findAll() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(mapResultSetToSale(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching sales: " + e.getMessage());
        }
        return sales;
    }

    public List<Sale> findByJewelryId(int jewelryId) {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE jewelry_id = ? ORDER BY sale_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jewelryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sales.add(mapResultSetToSale(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching sales by jewelry: " + e.getMessage());
        }
        return sales;
    }

    private Sale mapResultSetToSale(ResultSet rs) throws SQLException {
        LocalDateTime saleDate = null;
        Timestamp timestamp = rs.getTimestamp("sale_date");
        if (timestamp != null) {
            saleDate = timestamp.toLocalDateTime();
        }
        return new Sale(
                rs.getInt("id"),
                rs.getInt("jewelry_id"),
                rs.getInt("quantity"),
                rs.getDouble("total_price"),
                rs.getString("customer_name"),
                saleDate
        );
    }
}
