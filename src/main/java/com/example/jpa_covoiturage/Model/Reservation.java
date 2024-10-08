package com.example.jpa_covoiturage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "etat", nullable = false) // Ajoutez le champ etat
    private String etat;

    // Constructors, getters, and setters

    public Reservation() {}

    public Reservation(User user, Trajet trajet, LocalDate date, String etat) {
        this.user = user;
        this.trajet = trajet;
        this.date = date;
        this.etat = etat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}