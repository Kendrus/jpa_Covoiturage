package com.example.jpa_covoiturage;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.jpa_covoiturage.Model.Reservation;
import com.example.jpa_covoiturage.Model.User;
import com.example.jpa_covoiturage.Model.Trajet;
import com.example.jpa_covoiturage.Repository.ReservationRepository;
import com.example.jpa_covoiturage.Repository.UserRepository;
import com.example.jpa_covoiturage.Repository.TrajetRepository;

import java.time.LocalDate;

public class ReservationController {

    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, Long> idColumn;
    @FXML
    private TableColumn<Reservation, String> userColumn;
    @FXML
    private TableColumn<Reservation, String> trajetColumn;
    @FXML
    private TableColumn<Reservation, LocalDate> dateColumn;

    @FXML
    private TextField userField;
    @FXML
    private TextField trajetField;
    @FXML
    private DatePicker datePicker;

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private TrajetRepository trajetRepository;

    private ObservableList<Reservation> reservationList;

    public void initialize() {
        reservationRepository = ReservationRepository.create();
        userRepository = UserRepository.create();
        trajetRepository = TrajetRepository.create();

        // Initialiser les colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getNom()));
        trajetColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrajet().getLieuDepart()));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Charger les données
        loadReservations();
    }

    private void loadReservations() {
        reservationList = FXCollections.observableArrayList(reservationRepository.getAll());
        reservationsTable.setItems(reservationList);
    }

    @FXML
    private void handleReservation() {
        try {
            Long userId = Long.parseLong(userField.getText());
            Long trajetId = Long.parseLong(trajetField.getText());
            LocalDate date = datePicker.getValue();

            User user = userRepository.findById(userId);
            Trajet trajet = trajetRepository.findById(trajetId);

            if (user == null || trajet == null) {
                showAlert("Erreur", "Utilisateur ou trajet non trouvé.");
                return;
            }

            Reservation newReservation = new Reservation(user, trajet, date);
            reservationRepository.save(newReservation);

            // Rafraîchir la liste des réservations
            loadReservations();

            // Vider les champs du formulaire
            clearForm();

            showAlert("Succès", "Réservation créée avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des ID valides pour l'utilisateur et le trajet.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la création de la réservation : " + e.getMessage());
        }
    }

    private void clearForm() {
        userField.clear();
        trajetField.clear();
        datePicker.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            reservationRepository.delete(selectedReservation);
            loadReservations();
            showAlert("Succès", "Réservation supprimée avec succès.");
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réservation à supprimer.");
        }
    }

    @FXML
    private void handleUpdateReservation() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            try {
                Long userId = Long.parseLong(userField.getText());
                Long trajetId = Long.parseLong(trajetField.getText());
                LocalDate date = datePicker.getValue();

                User user = userRepository.findById(userId);
                Trajet trajet = trajetRepository.findById(trajetId);

                if (user == null || trajet == null) {
                    showAlert("Erreur", "Utilisateur ou trajet non trouvé.");
                    return;
                }

                selectedReservation.setUser(user);
                selectedReservation.setTrajet(trajet);
                selectedReservation.setDate(date);

                reservationRepository.update(selectedReservation);
                loadReservations();
                clearForm();
                showAlert("Succès", "Réservation mise à jour avec succès.");
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des ID valides pour l'utilisateur et le trajet.");
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de la réservation : " + e.getMessage());
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réservation à mettre à jour.");
        }
    }

    @FXML
    private void handleSelectReservation() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            userField.setText(String.valueOf(selectedReservation.getUser().getId()));
            trajetField.setText(String.valueOf(selectedReservation.getTrajet().getId()));
            datePicker.setValue(selectedReservation.getDate());
        }
    }
}