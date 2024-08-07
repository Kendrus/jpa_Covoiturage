package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.Reservation;
import com.example.jpa_covoiturage.Model.Trajet;
import com.example.jpa_covoiturage.Model.User;
import com.example.jpa_covoiturage.Repository.ReservationRepository;
import com.example.jpa_covoiturage.Repository.TrajetRepository;
import com.example.jpa_covoiturage.Repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
        this.reservationRepository = ReservationRepository.create();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        trajetColumn.setCellValueFactory(new PropertyValueFactory<>("trajet"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        loadUsers();
        loadTrajets();
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
            envoyerEmailConfirmation(reservation);

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

    private void envoyerEmailConfirmation(Reservation reservation) {
        String host = "sandbox.smtp.mailtrap.io";
        final String user = "1b530204760f86";
        final String password = "d0b25c0be4bfc7";

        String to = reservation.getUser().getEmail();
        String from = "sidoine@example.com";  // Modifié ici
        String subject = "Confirmation de Réservation";
        String body = "Bonjour " + reservation.getUser().getNom() + " " + reservation.getUser().getPrenom() + ",\n\n" +
                "Votre réservation a été confirmée avec succès !\n\n" +
                "Détails de la réservation :\n" +
                "Trajet : " + reservation.getTrajet().getLieuDepart() + " -> " + reservation.getTrajet().getLieuArrivee() + "\n" +
                "Date : " + reservation.getTrajet().getDateDepart() + "\n" +
                "Heure : " + reservation.getTrajet().getHeureDepart() + "\n" +
                "Nombre de places réservées : " + reservation.getTrajet().getPlacesDisponibles() + "\n" +
                "Tarif : " + reservation.getTrajet().getPrix() + "\n\n" +
                "Merci d'utiliser notre service de covoiturage.\n\n" +
                "Cordialement,\n" +
                "L'équipe de Covoiturage";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "2525");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        try {
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(user, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + to);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'envoi de l'email de confirmation : " + e.getMessage());
        }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void goToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/jpa_covoiturage/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);
            Stage stage = (Stage) reservationsTable.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error");
        }
    }
}