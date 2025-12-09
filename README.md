# Jewelry Store Management System

A console-based jewelry store inventory and sales management application built with Java and SQLite.

## Features

- **Inventory Management**: Add, update, delete, and view jewelry items
- **Sales Recording**: Record sales transactions with customer information
- **Search**: Search jewelry by name, type, or material
- **Persistent Storage**: SQLite database for data persistence

## Project Structure

```
jewelry-store/
├── src/main/java/com/example/jewelry/
│   ├── Main.java              # Application entry point
│   ├── Menu.java              # Console menu interface
│   ├── DatabaseConnection.java # Database connection and initialization
│   ├── Jewelry.java           # Jewelry entity model
│   ├── Sale.java              # Sale entity model
│   ├── InventoryDAO.java      # Data access for jewelry inventory
│   └── SaleDAO.java           # Data access for sales
├── db/
│   └── jewelry.db             # SQLite database (auto-created)
└── README.md
```

## Requirements

- Java 17 or higher
- Maven
- SQLite JDBC driver (included in pom.xml)

## Running the Application

```bash
# Build the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.example.jewelry.Main"
```

## Database Schema

### Jewelry Table
| Column   | Type    | Description           |
|----------|---------|----------------------|
| id       | INTEGER | Primary key (auto)   |
| name     | TEXT    | Jewelry name         |
| type     | TEXT    | Type (Ring, Necklace, etc.) |
| material | TEXT    | Material (Gold, Silver, etc.) |
| price    | REAL    | Unit price           |
| quantity | INTEGER | Stock quantity       |

### Sales Table
| Column        | Type      | Description              |
|---------------|-----------|--------------------------|
| id            | INTEGER   | Primary key (auto)       |
| jewelry_id    | INTEGER   | Foreign key to jewelry   |
| quantity      | INTEGER   | Quantity sold            |
| total_price   | REAL      | Total sale amount        |
| customer_name | TEXT      | Customer name            |
| sale_date     | TIMESTAMP | Date/time of sale        |

## Menu Options

1. **View Inventory** - Display all jewelry items
2. **Add Jewelry** - Add new jewelry to inventory
3. **Update Jewelry** - Modify existing jewelry details
4. **Delete Jewelry** - Remove jewelry from inventory
5. **Record Sale** - Record a new sale transaction
6. **View Sales** - Display sales history
7. **Search Jewelry** - Search by keyword
0. **Exit** - Close the application
# jewelry-store
