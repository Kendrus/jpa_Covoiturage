package com.example.jpa_covoiturage.service;

import com.example.jpa_covoiturage.Model.Utilisateur;
import com.example.jpa_covoiturage.Repository.UtilisateurRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthService {
    private final UtilisateurRepository utilisateurRepository;

    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Utilisateur authentifier(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        if (utilisateur != null && utilisateur.getMdp().equals(motDePasse)) {
            return utilisateur;
        }
        return null;
    }

    public boolean enregistrerUtilisateur(Utilisateur utilisateur, String motDePasse) {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            return false;
        }

        String hashedPassword = hashPassword(motDePasse);
        utilisateur.setMdp(hashedPassword);

        utilisateurRepository.save(utilisateur);
        return true;
    }

    public boolean changerMotDePasse(String email, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        if (utilisateur != null && utilisateur.getMdp().equals(hashPassword(ancienMotDePasse))) {
            String nouveauHashedPassword = hashPassword(nouveauMotDePasse);
            utilisateur.setMdp(nouveauHashedPassword);
            utilisateurRepository.save(utilisateur);
            return true;
        }
        return false;
    }
}