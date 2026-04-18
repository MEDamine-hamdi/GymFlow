package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import com.gymflow.service.AuthService;
import com.gymflow.model.Utilisateur;
import com.gymflow.util.Session;

public class LoginController {

    @FXML
    private TextField nomUtilisateurField;

    @FXML
    private PasswordField motDePasseField;

    @FXML
    private void handleLogin() {
        String nom = nomUtilisateurField.getText();
        String mot = motDePasseField.getText();

        AuthService authService = new AuthService();
        Utilisateur user = authService.auth(nom, mot);

        if (user != null) {
            Session.setUtilisateur(user);
            System.out.println("Connexion réussie : " + user.getNom());
        } else {
            System.out.println("Nom utilisateur ou mot de passe incorrect");
        }
    }
}