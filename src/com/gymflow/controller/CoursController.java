package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

import com.gymflow.model.Cours;
import com.gymflow.model.Coach;
import com.gymflow.model.Salle;

import com.gymflow.dao.CoursDAO;
import com.gymflow.dao.CoachDAO;
import com.gymflow.dao.SalleDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CoursController {

    // ===== FORM =====
    @FXML private ComboBox<String> typeCombo;
    @FXML private ComboBox<Coach> coachCombo;
    @FXML private ComboBox<Salle> salleCombo;

    @FXML private TextField nbField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField; // ✅ ONLY ONE FIELD

    // ===== TABLE =====
    @FXML private TableView<Cours> table;
    @FXML private TableColumn<Cours, String> typeCol;
    @FXML private TableColumn<Cours, String> coachCol;
    @FXML private TableColumn<Cours, String> salleCol;
    @FXML private TableColumn<Cours, String> dateCol;
    @FXML private TableColumn<Cours, String> statusCol;
    @FXML private TableColumn<Cours, Void> actionCol;

    private final CoursDAO coursDAO = new CoursDAO();
    private final CoachDAO coachDAO = new CoachDAO();
    private final SalleDAO salleDAO = new SalleDAO();

    @FXML
    public void initialize() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coachNom"));
        salleCol.setCellValueFactory(new PropertyValueFactory<>("salleLabel"));

        dateCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getDate().toString()
                )
        );

        statusCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStatus()
                )
        );

        typeCombo.setItems(FXCollections.observableArrayList(
                "Boxe", "Karate", "Taekwondo", "Gymnastique"
        ));

        coachCombo.setItems(FXCollections.observableArrayList(coachDAO.getAllCoachs()));
        coachCombo.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(Coach c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNom());
            }
        });
        coachCombo.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Coach c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNom());
            }
        });

        salleCombo.setItems(FXCollections.observableArrayList(salleDAO.getAllSalles()));
        salleCombo.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(Salle s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? null : "Salle " + s.getNumSalle());
            }
        });
        salleCombo.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Salle s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? null : "Salle " + s.getNumSalle());
            }
        });

        loadData();
        addDeleteButton();
    }

    // ===== ADD =====
    @FXML
    private void handleAddCours() {

        try {
            String type = typeCombo.getValue();
            Coach coach = coachCombo.getValue();
            Salle salle = salleCombo.getValue();

            int nb = Integer.parseInt(nbField.getText());
            LocalDate date = datePicker.getValue();

            // ✅ ONLY ONE TIME FIELD
            LocalTime debut = LocalTime.parse(heureField.getText());
            LocalTime fin = debut.plusHours(1); // auto duration

            if (type == null || coach == null || salle == null || date == null) {
                showAlert("Erreur", "Remplis tous les champs");
                return;
            }

            Cours c = new Cours();
            c.setSpecialite(type);
            c.setCoachId(coach.getId());
            c.setCoachNom(coach.getNom());
            c.setSalleId(salle.getId());
            c.setSalleLabel("Salle " + salle.getNumSalle());
            c.setNbParticipants(nb);
            c.setDate(date);
            c.setHeureDebut(debut);
            c.setHeureFin(fin);

            coursDAO.addCours(c);

            loadData();
            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", "Format heure HH:mm (ex: 18:00)");
        }
    }

    private void loadData() {
        table.getItems().setAll(coursDAO.getAllCours());
    }

    private void addDeleteButton() {

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("❌");

            {
                btn.setOnAction(e -> {
                    Cours c = getTableView().getItems().get(getIndex());
                    coursDAO.deleteCours(c.getId());
                    loadData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void clearFields() {
        typeCombo.setValue(null);
        coachCombo.setValue(null);
        salleCombo.setValue(null);
        nbField.clear();
        datePicker.setValue(null);
        heureField.clear();
    }

    private void showAlert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setContentText(m);
        a.showAndWait();
    }
}