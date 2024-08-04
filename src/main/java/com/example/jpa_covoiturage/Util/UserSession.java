package com.example.jpa_covoiturage.Util;

import com.example.jpa_covoiturage.Model.Utilisateur;

public class UserSession {
    private static UserSession instance;
    private Utilisateur loggedInUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInUser(Utilisateur user) {
        this.loggedInUser = user;
    }

    public Utilisateur getLoggedInUser() {
        return loggedInUser;
    }

    public void clearSession() {
        this.loggedInUser = null;
    }
}