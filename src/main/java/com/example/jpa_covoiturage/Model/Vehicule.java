package com.example.jpa_covoiturage.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marque;
    private String modele;
    private String immatriculation;

    // Constructeurs, getters et setters

    public Vehicule() {}

    public Vehicule(String marque, String modele, String immatriculation) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    // Méthodes pour les propriétés JavaFX
    public StringProperty marqueProperty() {
        return new SimpleStringProperty(marque);
    }

    public StringProperty modeleProperty() {
        return new SimpleStringProperty(modele);
    }

    public StringProperty immatriculationProperty() {
        return new SimpleStringProperty(immatriculation);
    }
}
