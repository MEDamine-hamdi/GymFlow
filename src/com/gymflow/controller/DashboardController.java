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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;




public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void loadAbonnements() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/abonnement.fxml")
            );
            contentArea.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        Utilisateur user = Session.getUtilisateur();

        if (user != null) {
            welcomeLabel.setText("Bienvenue " + user.getNom());
        }
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // clear session
            Session.setUtilisateur(null);

            // load login screen
            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/login.fxml")
            );

            Scene scene = new Scene(root);

            // apply CSS
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

    @FXML
    private void loadClients() {
        System.out.println("CLICK WORKS");

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/clients.fxml")
            );

            Node view = loader.load();
            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}