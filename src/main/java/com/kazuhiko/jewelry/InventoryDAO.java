package com.kazuhiko.jewelry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public void insert(Jewelry jewelry) {
        String sql = "INSERT INTO jewelry (name, type, material, price, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jewelry.getName());
            pstmt.setString(2, jewelry.getType());
            pstmt.setString(3, jewelry.getMaterial());
            pstmt.setDouble(4, jewelry.getPrice());
            pstmt.setInt(5, jewelry.getQuantity());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting jewelry: " + e.getMessage());
        }
    }

    public void update(Jewelry jewelry) {
        String sql = "UPDATE jewelry SET name = ?, type = ?, material = ?, price = ?, quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jewelry.getName());
            pstmt.setString(2, jewelry.getType());
            pstmt.setString(3, jewelry.getMaterial());
            pstmt.setDouble(4, jewelry.getPrice());
            pstmt.setInt(5, jewelry.getQuantity());
            pstmt.setInt(6, jewelry.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating jewelry: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM jewelry WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting jewelry: " + e.getMessage());
        }
    }

    public Jewelry findById(int id) {
        String sql = "SELECT * FROM jewelry WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToJewelry(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding jewelry: " + e.getMessage());
        }
        return null;
    }

    public List<Jewelry> findAll() {
        List<Jewelry> items = new ArrayList<>();
        String sql = "SELECT * FROM jewelry ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(mapResultSetToJewelry(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory: " + e.getMessage());
        }
        return items;
    }

    public List<Jewelry> search(String keyword) {
        List<Jewelry> items = new ArrayList<>();
        String sql = "SELECT * FROM jewelry WHERE name LIKE ? OR type LIKE ? OR material LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(mapResultSetToJewelry(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching jewelry: " + e.getMessage());
        }
        return items;
    }

    private Jewelry mapResultSetToJewelry(ResultSet rs) throws SQLException {
        return new Jewelry(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getString("material"),
                rs.getDouble("price"),
                rs.getInt("quantity")
        );
    }
}
