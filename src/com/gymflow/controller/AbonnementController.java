package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.gymflow.model.Abonnement;
import com.gymflow.dao.AbonnementDAO;

public class AbonnementController {

    @FXML private TextField nameField;
    @FXML private TextField priceField;

    @FXML private TableView<Abonnement> table;
    @FXML private TableColumn<Abonnement, String> nameCol;
    @FXML private TableColumn<Abonnement, Double> priceCol;
    @FXML private TableColumn<Abonnement, Void> actionCol;

    private final AbonnementDAO dao = new AbonnementDAO();
    private final ObservableList<Abonnement> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // ✅ Correct bindings with model (nom / prix)
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // ✅ Fix table resize warning (JavaFX 26)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        loadData();
        addDeleteButton();
    }

    // ================= LOAD =================
    private void loadData() {
        data.clear();
        data.addAll(dao.getAllAbonnements());
        table.setItems(data);
    }

    // ================= ADD =================
    @FXML
    private void handleAdd() {

        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            showAlert("Erreur", "Remplis tous les champs");
            return;
        }

        double price;

        try {
            price = Double.parseDouble(priceText);
        } catch (Exception e) {
            showAlert("Erreur", "Prix invalide");
            return;
        }

        Abonnement ab = new Abonnement(name, price);

        dao.addAbonnement(ab);

        loadData();
        clearFields();
    }

    // ================= DELETE BUTTON =================
    private void addDeleteButton() {

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button deleteBtn = new Button("❌ Supprimer");

            {
                deleteBtn.getStyleClass().add("action-btn");

                deleteBtn.setOnAction(e -> {
                    Abonnement ab = getTableView().getItems().get(getIndex());

                    dao.deleteAbonnement(ab.getId());

                    loadData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    // ================= UTILS =================
    private void clearFields() {
        nameField.clear();
        priceField.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}