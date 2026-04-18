package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.gymflow.model.Coach;
import com.gymflow.dao.CoachDAO;

import java.util.List;

public class CoachController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telField;
    @FXML private TextField expField;

    @FXML private ComboBox<String> specialiteCombo;

    @FXML private TableView<Coach> table;
    @FXML private TableColumn<Coach, String> nomCol;
    @FXML private TableColumn<Coach, String> prenomCol;
    @FXML private TableColumn<Coach, String> telCol;
    @FXML private TableColumn<Coach, Integer> expCol;
    @FXML private TableColumn<Coach, Double> salaireCol;
    @FXML private TableColumn<Coach, String> specCol;
    @FXML private TableColumn<Coach, Void> actionCol;

    private final CoachDAO dao = new CoachDAO();

    @FXML
    public void initialize() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // columns
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        expCol.setCellValueFactory(new PropertyValueFactory<>("anneeExperience"));
        salaireCol.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        specCol.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        // specialite list
        specialiteCombo.setItems(FXCollections.observableArrayList(
                "Boxe",
                "Karate",
                "Taekwondo",
                "Musculation",
                "Gymnastique"
        ));

        loadData();
        addDeleteButton();
    }

    private void loadData() {
        List<Coach> list = dao.getAllCoachs();
        table.getItems().setAll(list);
    }

    @FXML
    private void handleAddCoach() {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String tel = telField.getText();
        String spec = specialiteCombo.getValue();

        int exp;

        try {
            exp = Integer.parseInt(expField.getText());
        } catch (Exception e) {
            System.out.println("Experience invalide");
            return;
        }

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || spec == null) {
            System.out.println("Remplis tous les champs");
            return;
        }

        Coach c = new Coach(nom, prenom, tel, exp, spec);

        dao.addCoach(c);

        clearFields();
        loadData();
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        telField.clear();
        expField.clear();
        specialiteCombo.setValue(null);
    }

    private void addDeleteButton() {

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button deleteBtn = new Button("❌");

            {
                deleteBtn.getStyleClass().add("action-btn");

                deleteBtn.setOnAction(e -> {
                    Coach c = getTableView().getItems().get(getIndex());
                    deleteCoach(c.getId());
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

    private void deleteCoach(int id) {

        try {
            var conn = com.gymflow.util.DBConnection.getConnection();
            var ps = conn.prepareStatement("DELETE FROM coach WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}