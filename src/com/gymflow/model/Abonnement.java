package com.gymflow.model;

public class Abonnement {

    private int id;
    private String nom;
    private double prix;

    public Abonnement() {}

    public Abonnement(int id, String nom, double prix) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
    }

    public Abonnement(String nom, double prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public String getNom() {   // ✅ THIS FIXES getNom ERROR
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return nom; // ✅ IMPORTANT for ComboBox display
    }
}