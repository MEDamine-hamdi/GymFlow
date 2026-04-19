package com.gymflow.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cours {

    private int id;

    private String specialite;

    private int coachId;
    private int salleId;

    private String coachNom;
    private String salleLabel; // "Salle 1"

    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    private int nbParticipants;

    // ================= GETTERS / SETTERS =================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public int getCoachId() { return coachId; }
    public void setCoachId(int coachId) { this.coachId = coachId; }

    public int getSalleId() { return salleId; }
    public void setSalleId(int salleId) { this.salleId = salleId; }

    public String getCoachNom() { return coachNom; }
    public void setCoachNom(String coachNom) { this.coachNom = coachNom; }

    public String getSalleLabel() { return salleLabel; }
    public void setSalleLabel(String salleLabel) { this.salleLabel = salleLabel; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public int getNbParticipants() { return nbParticipants; }
    public void setNbParticipants(int nbParticipants) { this.nbParticipants = nbParticipants; }

    // ================= STATUS =================
    public String getStatus() {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (date.isAfter(today)) return "Pending";

        if (date.isEqual(today)) {
            if (now.isBefore(heureDebut)) return "Pending";
            if (now.isAfter(heureFin)) return "Done";
            return "Ongoing";
        }

        return "Done";
    }
}