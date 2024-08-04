package com.example.jpa_covoiturage;

import jakarta.persistence.EntityManagerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.show();

        EntityManagerFactory entityManagerFactory = JPAUtil.getEntityManagerFactory();
    }


        public static void main(String[] args) {
            launch(args);
        }
}