package com.example.jpa_covoiturage;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement de la page de connexion
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene loginScene = new Scene(loginRoot, 300, 275);
        loginScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Configuration de la fenêtre de connexion
        Stage loginStage = new Stage();
        loginStage.setTitle("Login Page");
        loginStage.setScene(loginScene);
        loginStage.show();

        // Chargement de la page de gestion des utilisateurs
        Parent userRoot = FXMLLoader.load(getClass().getResource("/user_crud.fxml"));
        Scene userScene = new Scene(userRoot, 600, 400);
        userScene.getStylesheets().add(getClass().getResource("/user_style.css").toExternalForm());

        // Configuration de la fenêtre de gestion des utilisateurs
        Stage userStage = new Stage();
        userStage.setTitle("User Management");
        userStage.setScene(userScene);

        // Vous pouvez choisir d'afficher la fenêtre de gestion des utilisateurs ici,
        // ou la faire apparaître après une connexion réussie
        // userStage.show();

        // Exemple de logique pour passer de la page de connexion à la gestion des utilisateurs
        loginStage.setOnHidden(event -> userStage.show());
    }

    public static void main(String[] args) {
        launch(args);
    }
}