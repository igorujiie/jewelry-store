package com.kazuhiko.jewelry.controller;

import com.kazuhiko.jewelry.InventoryDAO;
import com.kazuhiko.jewelry.Jewelry;
import com.kazuhiko.jewelry.Sale;
import com.kazuhiko.jewelry.SaleDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private VBox inventoryView;
    @FXML private VBox salesView;
    @FXML private VBox addJewelryView;
    
    @FXML private TableView<Jewelry> inventoryTable;
    @FXML private TableColumn<Jewelry, Integer> colId;
    @FXML private TableColumn<Jewelry, String> colName;
    @FXML private TableColumn<Jewelry, String> colType;
    @FXML private TableColumn<Jewelry, String> colMaterial;
    @FXML private TableColumn<Jewelry, Double> colPrice;
    @FXML private TableColumn<Jewelry, Integer> colQuantity;
    
    @FXML private TableView<Sale> salesTable;
    @FXML private TableColumn<Sale, Integer> colSaleId;
    @FXML private TableColumn<Sale, String> colSaleItem;
    @FXML private TableColumn<Sale, Integer> colSaleQty;
    @FXML private TableColumn<Sale, Double> colSaleTotal;
    @FXML private TableColumn<Sale, String> colSaleCustomer;
    @FXML private TableColumn<Sale, String> colSaleDate;
    
    @FXML private TextField searchField;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private ComboBox<String> materialCombo;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private Label formTitle;
    @FXML private Button saveButton;
    
    @FXML private Label statusLabel;
    
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final SaleDAO saleDAO = new SaleDAO();
    private ObservableList<Jewelry> inventoryList = FXCollections.observableArrayList();
    private ObservableList<Sale> salesList = FXCollections.observableArrayList();
    
    private Jewelry editingJewelry = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupInventoryTable();
        setupSalesTable();
        setupComboBoxes();
        loadInventory();
        showInventory();
    }
    
    private void setupInventoryTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        colPrice.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", price));
                }
            }
        });
        
        inventoryTable.setItems(inventoryList);
    }
    
    private void setupSalesTable() {
        colSaleId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSaleQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSaleTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colSaleCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        colSaleItem.setCellValueFactory(cellData -> {
            Jewelry j = inventoryDAO.findById(cellData.getValue().getJewelryId());
            return new SimpleStringProperty(j != null ? j.getName() : "Desconhecido");
        });
        
        colSaleDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getSaleDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(cellData.getValue().getSaleDate().format(formatter));
            }
            return new SimpleStringProperty("N/A");
        });
        
        colSaleTotal.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", price));
                }
            }
        });
        
        salesTable.setItems(salesList);
    }
    
    private void setupComboBoxes() {
        typeCombo.setItems(FXCollections.observableArrayList(
            "Anel", "Colar", "Pulseira", "Brinco", "Outro"
        ));
        materialCombo.setItems(FXCollections.observableArrayList(
            "Ouro", "Prata", "Platina", "Outro"
        ));
    }
    
    private void loadInventory() {
        inventoryList.clear();
        inventoryList.addAll(inventoryDAO.findAll());
    }
    
    private void loadSales() {
        salesList.clear();
        salesList.addAll(saleDAO.findAll());
    }
    
    @FXML
    private void showInventory() {
        inventoryView.setVisible(true);
        inventoryView.setManaged(true);
        salesView.setVisible(false);
        salesView.setManaged(false);
        addJewelryView.setVisible(false);
        addJewelryView.setManaged(false);
        loadInventory();
        setStatus("Visualizando inventário - " + inventoryList.size() + " itens");
    }
    
    @FXML
    private void showSales() {
        inventoryView.setVisible(false);
        inventoryView.setManaged(false);
        salesView.setVisible(true);
        salesView.setManaged(true);
        addJewelryView.setVisible(false);
        addJewelryView.setManaged(false);
        loadSales();
        setStatus("Visualizando vendas - " + salesList.size() + " registros");
    }
    
    @FXML
    private void showAddJewelry() {
        editingJewelry = null;
        clearForm();
        formTitle.setText("Adicionar Nova Joia");
        saveButton.setText("Salvar");
        
        inventoryView.setVisible(false);
        inventoryView.setManaged(false);
        salesView.setVisible(false);
        salesView.setManaged(false);
        addJewelryView.setVisible(true);
        addJewelryView.setManaged(true);
        setStatus("Adicionando nova joia");
    }
    
    @FXML
    private void editSelectedJewelry() {
        Jewelry selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma joia para editar.");
            return;
        }
        
        editingJewelry = selected;
        formTitle.setText("Editar Joia");
        saveButton.setText("Atualizar");
        
        nameField.setText(selected.getName());
        typeCombo.setValue(selected.getType());
        materialCombo.setValue(selected.getMaterial());
        priceField.setText(String.valueOf(selected.getPrice()));
        quantityField.setText(String.valueOf(selected.getQuantity()));
        
        inventoryView.setVisible(false);
        inventoryView.setManaged(false);
        salesView.setVisible(false);
        salesView.setManaged(false);
        addJewelryView.setVisible(true);
        addJewelryView.setManaged(true);
        setStatus("Editando: " + selected.getName());
    }
    
    @FXML
    private void deleteSelectedJewelry() {
        Jewelry selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma joia para excluir.");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Exclusão");
        confirm.setHeaderText("Excluir " + selected.getName() + "?");
        confirm.setContentText("Esta ação não pode ser desfeita.");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            inventoryDAO.delete(selected.getId());
            loadInventory();
            setStatus("Joia excluída: " + selected.getName());
        }
    }
    
    @FXML
    private void saveJewelry() {
        if (!validateForm()) {
            return;
        }
        
        try {
            String name = nameField.getText().trim();
            String type = typeCombo.getValue();
            String material = materialCombo.getValue();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            
            if (editingJewelry == null) {
                Jewelry jewelry = new Jewelry(0, name, type, material, price, quantity);
                inventoryDAO.insert(jewelry);
                setStatus("Joia adicionada: " + name);
            } else {
                editingJewelry.setName(name);
                editingJewelry.setType(type);
                editingJewelry.setMaterial(material);
                editingJewelry.setPrice(price);
                editingJewelry.setQuantity(quantity);
                inventoryDAO.update(editingJewelry);
                setStatus("Joia atualizada: " + name);
            }
            
            showInventory();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Preço ou quantidade inválidos.");
        }
    }
    
    @FXML
    private void cancelForm() {
        showInventory();
    }
    
    @FXML
    private void recordSale() {
        Jewelry selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma joia para vender.");
            return;
        }
        
        if (selected.getQuantity() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Esta joia está sem estoque.");
            return;
        }
        
        Dialog<Sale> dialog = new Dialog<>();
        dialog.setTitle("Registrar Venda");
        dialog.setHeaderText("Vender: " + selected.getName());
        
        ButtonType sellButton = new ButtonType("Vender", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sellButton, ButtonType.CANCEL);
        
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20;");
        
        Label stockLabel = new Label("Estoque disponível: " + selected.getQuantity());
        Label priceLabel = new Label("Preço unitário: R$ " + String.format("%.2f", selected.getPrice()));
        
        TextField qtyField = new TextField("1");
        qtyField.setPromptText("Quantidade");
        
        TextField customerField = new TextField();
        customerField.setPromptText("Nome do cliente");
        
        content.getChildren().addAll(
            stockLabel, priceLabel,
            new Label("Quantidade:"), qtyField,
            new Label("Cliente:"), customerField
        );
        
        dialog.getDialogPane().setContent(content);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sellButton) {
                try {
                    int qty = Integer.parseInt(qtyField.getText().trim());
                    if (qty <= 0 || qty > selected.getQuantity()) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Quantidade inválida.");
                        return null;
                    }
                    double total = selected.getPrice() * qty;
                    return new Sale(0, selected.getId(), qty, total, customerField.getText().trim(), null);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Quantidade deve ser um número.");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Sale> result = dialog.showAndWait();
        result.ifPresent(sale -> {
            saleDAO.insert(sale);
            selected.setQuantity(selected.getQuantity() - sale.getQuantity());
            inventoryDAO.update(selected);
            loadInventory();
            setStatus(String.format("Venda registrada! Total: R$ %.2f", sale.getTotalPrice()));
        });
    }
    
    @FXML
    private void searchJewelry() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadInventory();
        } else {
            inventoryList.clear();
            inventoryList.addAll(inventoryDAO.search(keyword));
        }
        setStatus("Busca: " + inventoryList.size() + " resultado(s)");
    }
    
    @FXML
    private void clearSearch() {
        searchField.clear();
        loadInventory();
        setStatus("Busca limpa");
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Nome é obrigatório.");
            return false;
        }
        if (typeCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um tipo.");
            return false;
        }
        if (materialCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um material.");
            return false;
        }
        if (priceField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Preço é obrigatório.");
            return false;
        }
        if (quantityField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Quantidade é obrigatória.");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        nameField.clear();
        typeCombo.setValue(null);
        materialCombo.setValue(null);
        priceField.clear();
        quantityField.clear();
    }
    
    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
