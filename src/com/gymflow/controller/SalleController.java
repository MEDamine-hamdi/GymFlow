package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

import com.gymflow.model.Salle;
import com.gymflow.dao.SalleDAO;

import java.util.List;

public class SalleController {

    @FXML private TextField numField;
    @FXML private TextField capaciteField;

    @FXML private TableView<Salle> table;
    @FXML private TableColumn<Salle, Integer> numCol;
    @FXML private TableColumn<Salle, Integer> capaciteCol;
    @FXML private TableColumn<Salle, Void> actionCol;

    private final SalleDAO dao = new SalleDAO();

    @FXML
    public void initialize() {

        numCol.setCellValueFactory(new PropertyValueFactory<>("numSalle"));
        capaciteCol.setCellValueFactory(new PropertyValueFactory<>("capacite"));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadData();
        addDeleteButton();
    }

    private void loadData() {
        List<Salle> list = dao.getAllSalles();
        table.getItems().setAll(list);
    }

    @FXML
    private void handleAddSalle() {

        try {
            int num = Integer.parseInt(numField.getText());
            int cap = Integer.parseInt(capaciteField.getText());

            Salle s = new Salle(num, cap);
            dao.addSalle(s);

            loadData();
            clearFields();

        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    private void clearFields() {
        numField.clear();
        capaciteField.clear();
    }

    private void addDeleteButton() {

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button deleteBtn = new Button("❌");

            {
                deleteBtn.getStyleClass().add("action-btn");

                deleteBtn.setOnAction(e -> {
                    Salle s = getTableView().getItems().get(getIndex());
                    dao.deleteSalle(s.getId());
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
}