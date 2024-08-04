package com.example.jpa_covoiturage.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "trajets")
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lieu_depart", nullable = false)
    private String lieuDepart;

    @Column(name = "lieu_arrivee", nullable = false)
    private String lieuArrivee;

    @Column(name = "date_depart", nullable = false)
    private LocalDate dateDepart;

    private LocalTime HeureDepart;

    public LocalTime getHeureDepart() {
        return HeureDepart;
    }

    public void setHeureDepart(LocalTime HeureDepart) {
        this.HeureDepart = HeureDepart;
    }

    @Column(name = "places_disponibles", nullable = false)
    private int placesDisponibles;

    @Column(name = "prix", nullable = false)
    private double prix;

    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private User conducteur;

    // Constructeur par défaut
    public Trajet() {}

    // Constructeur avec tous les champs
    public Trajet(String lieuDepart, String lieuArrivee, LocalDate dateDepart, LocalTime heureDepart, int placesDisponibles, double prix, User conducteur) {
        this.lieuDepart = lieuDepart;
        this.lieuArrivee = lieuArrivee;
        this.dateDepart = dateDepart;
        this.HeureDepart = heureDepart;
        this.placesDisponibles = placesDisponibles;
        this.prix = prix;
        this.conducteur = conducteur;
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuArrivee() {
        return lieuArrivee;
    }

    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public User getConducteur() {
        return conducteur;
    }

    public void setConducteur(User conducteur) {
        this.conducteur = conducteur;
    }
}