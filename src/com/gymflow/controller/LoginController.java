package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import com.gymflow.service.AuthService;
import com.gymflow.model.Utilisateur;
import com.gymflow.util.Session;

public class LoginController {

    @FXML
    private TextField nomUtilisateurField;

    @FXML
    private PasswordField motDePasseField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String nom = nomUtilisateurField.getText();
        String mot = motDePasseField.getText();

        AuthService authService = new AuthService();
        Utilisateur user = authService.auth(nom, mot);

        if (user != null) {
            Session.setUtilisateur(user);
            System.out.println("Connexion réussie : " + user.getNom());

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml/dashboard.fxml")
                );
                Parent root = loader.load();

                Scene scene = new Scene(root);

                // ✅ Apply CSS to dashboard
                scene.getStylesheets().add(
                        getClass().getResource("/css/styles.css").toExternalForm()
                );

                Stage stage = (Stage) ((Node) event.getSource())
                        .getScene().getWindow();

                stage.setScene(scene);
                stage.setTitle("GymFlow Dashboard");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showError();
        }
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Échec de connexion");
        alert.setContentText("Nom utilisateur ou mot de passe incorrect");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }
}