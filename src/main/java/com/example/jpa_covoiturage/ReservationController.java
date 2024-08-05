package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.Reservation;
import com.example.jpa_covoiturage.Model.Trajet;
import com.example.jpa_covoiturage.Model.User;
import com.example.jpa_covoiturage.Repository.ReservationRepository;
import com.example.jpa_covoiturage.Repository.TrajetRepository;
import com.example.jpa_covoiturage.Repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.util.List;

public class ReservationController {

    @FXML
    private TableView<Reservation> reservationsTable;

    @FXML
    private TableColumn<Reservation, Long> idColumn;

    @FXML
    private TableColumn<Reservation, User> userColumn;

    @FXML
    private TableColumn<Reservation, Trajet> trajetColumn;

    @FXML
    private TableColumn<Reservation, LocalDate> dateColumn;

    @FXML
    private TableColumn<Reservation, String> etatColumn;

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private ComboBox<Trajet> trajetComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button reserveButton;

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private TrajetRepository trajetRepository;
    public ReservationController() {
        this.userRepository = UserRepository.create();
        this.trajetRepository = TrajetRepository.create();
        this.reservationRepository =ReservationRepository.create();

    }
    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        trajetColumn.setCellValueFactory(new PropertyValueFactory<>("trajet"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Load users and trajets into ComboBoxes
        loadUsers();
        loadTrajets();

        // Load reservations into TableView
        loadReservations();
    }

    private void loadUsers() {
        if (userRepository == null) {
            showErrorAlert("Le dépôt des utilisateurs n'est pas initialisé.");
            return;
        }

        List<User> users = userRepository.findAll();
        userComboBox.setItems(FXCollections.observableArrayList(users));
    }

    private void loadTrajets() {
        if (trajetRepository == null) {
            showErrorAlert("Le dépôt des trajets n'est pas initialisé.");
            return;
        }

        List<Trajet> trajets = trajetRepository.getAll();
        trajetComboBox.setItems(FXCollections.observableArrayList(trajets));
    }

    private void loadReservations() {
        if (reservationRepository == null) {
            showErrorAlert("Le dépôt des réservations n'est pas initialisé.");
            return;
        }

        List<Reservation> reservations = reservationRepository.getAll();
        reservationsTable.setItems(FXCollections.observableArrayList(reservations));
    }

    @FXML
    private void handleReservation(ActionEvent event) {
        try {
            User selectedUser = userComboBox.getValue();
            Trajet selectedTrajet = trajetComboBox.getValue();
            LocalDate reservationDate = datePicker.getValue();

            if (selectedUser == null || selectedTrajet == null || reservationDate == null) {
                showErrorAlert("Veuillez remplir tous les champs.");
                return;
            }

            String etat = determineEtat(selectedTrajet.getDateDepart());

            Reservation reservation = new Reservation(selectedUser, selectedTrajet, reservationDate, etat);
            reservationRepository.save(reservation);

            loadReservations();
            showInfoAlert("Réservation ajoutée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de la création de la réservation : " + e.getMessage());
        }
    }

    private String determineEtat(LocalDate trajetDate) {
        if (trajetDate.isBefore(LocalDate.now())) {
            return "En cours";
        } else {
            return "Future";
        }
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Setters for repositories to be injected

}
