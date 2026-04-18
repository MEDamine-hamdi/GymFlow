package com.gymflow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import com.gymflow.dao.ClientDAO;

public class HomeController {

    @FXML private Label clientCount;
    @FXML private Label activeSubs;
    @FXML private Label expiringCount;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void initialize() {

        int total = clientDAO.getAllClients().size();

        clientCount.setText("Clients: " + total);
        activeSubs.setText("Abonnements actifs: " + total);
        expiringCount.setText("Expiring soon: 0");
    }
}