package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.time.LocalDate;

import com.gymflow.model.Client;
import com.gymflow.model.Abonnement;
import com.gymflow.dao.ClientDAO;
import com.gymflow.dao.AbonnementDAO;

public class ClientController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telephoneField;
    @FXML private TextField searchField;

    @FXML private ComboBox<Abonnement> abonnementComboBox;

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> prenomColumn;
    @FXML private TableColumn<Client, String> telColumn;
    @FXML private TableColumn<Client, String> abonnementColumn;
    @FXML private TableColumn<Client, Void> actionColumn;

    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final ClientDAO clientDAO = new ClientDAO();
    private final AbonnementDAO abonnementDAO = new AbonnementDAO();

    private FilteredList<Client> filteredList;

    @FXML
    public void initialize() {

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        if (abonnementColumn != null) {
            abonnementColumn.setCellValueFactory(new PropertyValueFactory<>("abonnementNom"));
        }

        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        clientList.addAll(clientDAO.getAllClients());

        loadAbonnements();

        // 🔍 SEARCH
        filteredList = new FilteredList<>(clientList, b -> true);

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

        SortedList<Client> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(clientTable.comparatorProperty());

        clientTable.setItems(sortedList);

        addActionButtons();
        System.out.println("Clients loaded: " + clientList.size());
    }

    // ================= LOAD ABONNEMENTS =================
    private void loadAbonnements() {

        ObservableList<Abonnement> list =
                FXCollections.observableArrayList(abonnementDAO.getAllAbonnements());

        abonnementComboBox.setItems(list);

        abonnementComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Abonnement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

        abonnementComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Abonnement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
    }

    // ================= ADD CLIENT =================
    @FXML
    private void handleAddClient() {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String tel = telephoneField.getText();

        Abonnement selected = abonnementComboBox.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || selected == null) {
            showAlert("Erreur", "Remplis tous les champs");
            return;
        }

        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setTelephone(tel);

        client.setAbonnementId(selected.getId());
        client.setAbonnementNom(selected.getNom());

        client.setDateAbonnement(LocalDate.now());
        client.setDateExpiration(LocalDate.now().plusDays(30));

        clientDAO.addClient(client);
        clientList.add(client);

        clearFields();
    }

    // ================= ACTION BUTTONS =================
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

                    client.setDateExpiration(LocalDate.now().plusDays(30));
                    clientDAO.updateClient(client);

                    clientTable.refresh();
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
                    Client client = getTableView().getItems().get(getIndex());

                    long days = 0;

                    if (client.getDateExpiration() != null) {
                        days = daysLeft(client);
                    }

                    if (days <= 5) {
                        renewBtn.setStyle("-fx-background-color: #ff2e2e; -fx-text-fill: white;");
                    } else {
                        renewBtn.setStyle("-fx-background-color: #00c853; -fx-text-fill: white;");
                    }

                    HBox box = new HBox(8, renewBtn, changeBtn);
                    setGraphic(box);
                }
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    // ================= UTIL =================
    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
        abonnementComboBox.setValue(null);
    }

    private long daysLeft(Client client) {
        if (client.getDateExpiration() == null) {
            return 0;
        }

        return java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(),
                client.getDateExpiration()
        );
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}