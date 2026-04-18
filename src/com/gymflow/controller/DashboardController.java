package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.gymflow.model.Utilisateur;
import com.gymflow.util.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Label welcomeLabel;

    // ================= INIT =================
    @FXML
    public void initialize() {
        Utilisateur user = Session.getUtilisateur();

        if (user != null) {
            welcomeLabel.setText("Bienvenue " + user.getNom());
        }
    }

    // ================= GENERIC LOADER =================
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + fxmlFile)
            );

            Parent view = loader.load();

            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= NAVIGATION =================
    @FXML
    private void loadClients() {
        loadView("clients.fxml");
    }

    @FXML
    private void loadCoaches() {
        loadView("coach.fxml");
    }

    @FXML
    private void loadAbonnements() {
        loadView("abonnement.fxml");
    }

    @FXML
    private void loadSalles() {
        loadView("salle.fxml");
    }
    @FXML
    private void loadHome() {
        loadView("home.fxml");
    }

    // ================= LOGOUT =================
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Session.setUtilisateur(null);

            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/login.fxml")
            );

            Scene scene = new Scene(root);

            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("GymFlow - Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}