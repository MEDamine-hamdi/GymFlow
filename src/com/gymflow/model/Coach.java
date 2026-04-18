package com.gymflow.model;

public class Coach {

    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private int anneeExperience;
    private double salaire;
    private String specialite;

    public Coach() {}

    public Coach(String nom, String prenom, String telephone,
                 int anneeExperience, String specialite) {

        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.anneeExperience = anneeExperience;
        this.specialite = specialite;

        // ✅ salaire auto
        this.salaire = 750 + (0.5 * anneeExperience);
    }

    // GETTERS / SETTERS

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public int getAnneeExperience() { return anneeExperience; }
    public void setAnneeExperience(int anneeExperience) {
        this.anneeExperience = anneeExperience;
        this.salaire = 750 + (0.5 * anneeExperience); // auto update
    }

    public double getSalaire() { return salaire; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
}