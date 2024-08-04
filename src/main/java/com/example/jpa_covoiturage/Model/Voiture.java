package com.example.jpa_covoiturage.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voitures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voiture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    @Column(nullable = false)
    private int annee;

    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private User conducteur;

    public Voiture(String marque, String modele, String immatriculation, int annee, User conducteur) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.annee = annee;
        this.conducteur = conducteur;
    }
}
