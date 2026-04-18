package com.gymflow.service;
import com.gymflow.model.Role;
import com.gymflow.model.Utilisateur;

public class AuthService {

    public Utilisateur auth(String nom, String mot) {
        Utilisateur owner = new Utilisateur(1,"12345678","Admin","Owner",java.time.LocalDate.of(1990,1,1),"12345678","owner","1234",Role.OWNER);
        Utilisateur receptionist = new Utilisateur(2,"87654321","Reception","User",java.time.LocalDate.of(1995,5,10),"87654321","reception","1234",Role.RECEPTIONIST);

        if (owner.getNomUtilisateur().equals(nom) && owner.getMotDePasse().equals(mot)) {
            return owner;
        }

        if (receptionist.getNomUtilisateur().equals(nom) && receptionist.getMotDePasse().equals(mot)) {
            return receptionist;
        }

        return null;
    }
}
