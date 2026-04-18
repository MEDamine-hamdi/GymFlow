package com.gymflow.model;

import java.time.LocalDate;

public class Utilisateur extends Personne{
    protected String nomUtilisateur;
    protected String motDePasse;
    protected Role role;

    public Utilisateur(int id, String cin, String nom, String prenom, LocalDate dateNaissance, String numTel, String nomUtilisateur, String motDePasse, Role role) {
        super(id, cin, nom, prenom, dateNaissance, numTel);
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public Role getRole() {
        return role;
    }


    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
