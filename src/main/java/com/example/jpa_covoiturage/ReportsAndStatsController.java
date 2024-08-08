package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.Trajet;
import com.example.jpa_covoiturage.Model.User;
import com.example.jpa_covoiturage.Repository.TrajetRepository;
import com.example.jpa_covoiturage.Repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsAndStatsController {

    @FXML
    private ComboBox<User> conducteurComboBox;

    @FXML
    private BarChart<String, Number> trajetBarChart;

    @FXML
    private PieChart revenuPieChart;

    private TrajetRepository trajetRepository;
    private UserRepository userRepository;

    public ReportsAndStatsController() {
        this.trajetRepository = TrajetRepository.create();
        this.userRepository = UserRepository.create();
    }

    @FXML
    public void initialize() {
        loadConducteurs();
        conducteurComboBox.setOnAction(event -> updateCharts());
    }

    private void loadConducteurs() {
        List<User> conducteurs = userRepository.findByRole(User.Role.CONDUCTEUR);
        conducteurComboBox.setItems(FXCollections.observableArrayList(conducteurs));
    }

    private void updateCharts() {
        User selectedConducteur = conducteurComboBox.getValue();
        if (selectedConducteur != null) {
            updateTrajetBarChart(selectedConducteur);
            updateRevenuPieChart(selectedConducteur);
        }
    }

    private void updateTrajetBarChart(User conducteur) {
        trajetBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre de trajets");

        Map<YearMonth, Integer> trajetCountByMonth = new HashMap<>();
        List<Trajet> trajets = trajetRepository.findByConducteur(conducteur);

        for (Trajet trajet : trajets) {
            YearMonth yearMonth = YearMonth.from(trajet.getDateDepart());
            trajetCountByMonth.put(yearMonth, trajetCountByMonth.getOrDefault(yearMonth, 0) + 1);
        }

        LocalDate now = LocalDate.now();
        for (int i = 0; i < 12; i++) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            String monthYear = yearMonth.getMonth().toString() + " " + yearMonth.getYear();
            int count = trajetCountByMonth.getOrDefault(yearMonth, 0);
            series.getData().add(new XYChart.Data<>(monthYear, count));
        }

        trajetBarChart.getData().add(series);
    }

    private void updateRevenuPieChart(User conducteur) {
        revenuPieChart.getData().clear();
        Map<YearMonth, Double> revenuByMonth = new HashMap<>();
        List<Trajet> trajets = trajetRepository.findByConducteur(conducteur);

        for (Trajet trajet : trajets) {
            YearMonth yearMonth = YearMonth.from(trajet.getDateDepart());
            revenuByMonth.put(yearMonth, revenuByMonth.getOrDefault(yearMonth, 0.0) + trajet.getPrix());
        }

        LocalDate now = LocalDate.now();
        for (int i = 0; i < 12; i++) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            String monthYear = yearMonth.getMonth().toString() + " " + yearMonth.getYear();
            double revenu = revenuByMonth.getOrDefault(yearMonth, 0.0);
            if (revenu > 0) {
                revenuPieChart.getData().add(new PieChart.Data(monthYear, revenu));
            }
        }
    }

    @FXML
    private void goToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/jpa_covoiturage/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);
            Stage stage = (Stage) conducteurComboBox.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load dashboard.fxml");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}