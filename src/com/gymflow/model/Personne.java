package com.gymflow.model;

import java.time.LocalDate;

public abstract class Personne {

    protected int id;
    protected String cin;
    protected String nom;
    protected String prenom;
    protected LocalDate dateNaissance;
    protected String numTel;

    public Personne(int id, String cin, String nom, String prenom,
                    LocalDate dateNaissance, String numTel) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numTel = numTel;
    }

    // Getters
    public int getId() { return id; }
    public String getCin() { return cin; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public String getNumTel() { return numTel; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCin(String cin) { this.cin = cin; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    public void setNumTel(String numTel) { this.numTel = numTel; }

    @Override
    public String toString() {
        return "ID: " + id +
                ", CIN: " + cin +
                ", Nom: " + nom +
                ", Prenom: " + prenom +
                ", Date de Naissance: " + dateNaissance +
                ", Telephone: " + numTel;
    }
}