package com.gymflow.model;

public class Salle {

    private int id;
    private int numSalle;
    private int capacite;

    public Salle() {}

    public Salle(int numSalle, int capacite) {
        this.numSalle = numSalle;
        this.capacite = capacite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumSalle() { return numSalle; }
    public void setNumSalle(int numSalle) { this.numSalle = numSalle; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
}