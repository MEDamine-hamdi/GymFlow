package com.gymflow.model;

import java.time.LocalDate;

public class Client {

    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private int abonnementId;
    private String abonnementNom; // optional (for display)
    private LocalDate dateAbonnement;
    private LocalDate dateExpiration;

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    public Client() {}

    public Client(String nom, String prenom, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public Client(int id, String nom, String prenom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public int getAbonnementId() { return abonnementId; }
    public void setAbonnementId(int abonnementId) { this.abonnementId = abonnementId; }

    public String getAbonnementNom() { return abonnementNom; }
    public void setAbonnementNom(String abonnementNom) { this.abonnementNom = abonnementNom; }

    public LocalDate getDateAbonnement() { return dateAbonnement; }
    public void setDateAbonnement(LocalDate dateAbonnement) { this.dateAbonnement = dateAbonnement; }
}