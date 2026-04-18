package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import com.gymflow.model.Client;

public class ClientController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telephoneField;
    @FXML private TextField searchField;

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> prenomColumn;
    @FXML private TableColumn<Client, String> telColumn;
    @FXML private TableColumn<Client, Void> actionColumn;

    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private FilteredList<Client> filteredList;

    @FXML
    public void initialize() {

        // Bind columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Table fills full width
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Setup filtered list
        filteredList = new FilteredList<>(clientList, b -> true);

        // Search logic
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                filteredList.setPredicate(client -> {

                    if (newVal == null || newVal.isEmpty()) return true;

                    String keyword = newVal.toLowerCase();

                    return client.getNom().toLowerCase().contains(keyword)
                            || client.getPrenom().toLowerCase().contains(keyword)
                            || client.getTelephone().toLowerCase().contains(keyword);
                });
            });
        }

        // Sorted list
        SortedList<Client> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(clientTable.comparatorProperty());

        clientTable.setItems(sortedList);

        addActionButtons();
    }

    @FXML
    private void handleAddClient() {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String tel = telephoneField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty()) {
            showAlert("Erreur", "Remplis tous les champs");
            return;
        }

        clientList.add(new Client(nom, prenom, tel));

        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
    }

    private void addActionButtons() {

        Callback<TableColumn<Client, Void>, TableCell<Client, Void>> cellFactory = param -> new TableCell<>() {

            private final Button renewBtn = new Button("🔁");
            private final Button changeBtn = new Button("✏");

            {
                renewBtn.setTooltip(new Tooltip("Renouveler abonnement"));
                changeBtn.setTooltip(new Tooltip("Changer abonnement"));

                renewBtn.getStyleClass().add("action-btn");
                changeBtn.getStyleClass().add("action-btn-outline");

                renewBtn.setOnAction(e -> {
                    Client client = getTableView().getItems().get(getIndex());
                    showAlert("Succès", "Abonnement renouvelé pour " + client.getNom());
                });

                changeBtn.setOnAction(e -> {
                    Client client = getTableView().getItems().get(getIndex());
                    showAlert("Info", "Changer abonnement pour " + client.getNom());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8, renewBtn, changeBtn);
                    setGraphic(box);
                }
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}