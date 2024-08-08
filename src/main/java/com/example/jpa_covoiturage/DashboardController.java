package com.example.jpa_covoiturage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private BarChart<String, Number> tripsPerMonthChart;

    @FXML
    private PieChart earningsPerMonthChart;

    @FXML
    private void initialize() {
        // Initialiser les graphiques avec des données d'exemple
        initializeTripsPerMonthChart();
        initializeEarningsPerMonthChart();
    }

    private void initializeTripsPerMonthChart() {
        // Ajouter des données d'exemple
        tripsPerMonthChart.getData().add(new BarChart.Series<>("Trajets", javafx.collections.FXCollections.observableArrayList(
                new BarChart.Data<>("Jan", 10),
                new BarChart.Data<>("Fév", 15),
                new BarChart.Data<>("Mar", 20),
                new BarChart.Data<>("Avr", 25)
        )));
    }

    private void initializeEarningsPerMonthChart() {
        // Ajouter des données d'exemple
        earningsPerMonthChart.setData(javafx.collections.FXCollections.observableArrayList(
                new PieChart.Data("Jan", 500),
                new PieChart.Data("Fév", 750),
                new PieChart.Data("Mar", 1000),
                new PieChart.Data("Avr", 1250)
        ));
    }

    @FXML
    private void handleUserManagement(ActionEvent event) {
        try {
            loadFXML(event, "user.fxml", "Gestion des Utilisateurs");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de la page utilisateurs : " + e.getMessage());
        }
    }

    @FXML
    private void handleTripManagement(ActionEvent event) {
        try {
            loadFXML(event, "trajet.fxml", "Gestion des Trajets");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de la page trajets : " + e.getMessage());
        }
    }

    @FXML
    private void handleVehicleManagement(ActionEvent event) {
        try {
            loadFXML(event, "voiture.fxml", "Gestion des Véhicules");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de la page véhicules : " + e.getMessage());
        }
    }

    private void loadFXML(ActionEvent event, String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    @FXML
    private void handleReservationManagement(ActionEvent event) {
        try {
            loadFXML(event, "reservation-view.fxml", "Gestion des Reservations");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de la page reservation : " + e.getMessage());
        }
    }

    @FXML
    private void handleReportsAndStats(ActionEvent event) {
        try {
            loadFXML(event, "ReportsAndStats.fxml", "Rapports et Statistiques");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de la page des rapports et statistiques : " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        showNotImplementedAlert("Déconnexion");
    }

    private void showNotImplementedAlert(String feature) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Fonctionnalité non implémentée");
        alert.setHeaderText(null);
        alert.setContentText("La fonctionnalité '" + feature + "' n'est pas encore implémentée.");
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}