package com.gymflow.util;

import com.gymflow.model.Utilisateur;

public class Session {
    private static Utilisateur utilisateur;


    public static Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public static void setUtilisateur(Utilisateur utilisateur) {
        Session.utilisateur = utilisateur;
    }
}
