package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.Trajet;
import com.example.jpa_covoiturage.Model.User;
import com.example.jpa_covoiturage.Repository.TrajetRepository;
import com.example.jpa_covoiturage.Repository.UserRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrajetController {

    @FXML private TextField tfLieuDepart;
    @FXML private TextField tfLieuArrivee;
    @FXML private DatePicker dpDateDepart;
    @FXML private TextField tfHeureDepart;
    @FXML private Label heureErrorLabel;
    @FXML private TextField tfPlacesDisponibles;
    @FXML private TextField tfPrix;
    @FXML private ComboBox<User> cbConducteur;
    @FXML private TableView<Trajet> tableViewTrajets;
    @FXML private Button btnRetour;

    private TrajetRepository trajetRepository;
    private UserRepository userRepository;

    public TrajetController() {
        this.trajetRepository = TrajetRepository.create();
        this.userRepository = UserRepository.create();
    }

    @FXML
    public void initialize() {
        configureTableColumns();
        chargerConducteurs();
        chargerTrajets();
    }

    private void configureTableColumns() {
        TableColumn<Trajet, String> lieuDepartCol = new TableColumn<>("Lieu de départ");
        lieuDepartCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLieuDepart()));

        TableColumn<Trajet, String> lieuArriveeCol = new TableColumn<>("Lieu d'arrivée");
        lieuArriveeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLieuArrivee()));

        TableColumn<Trajet, LocalDate> dateDepartCol = new TableColumn<>("Date de départ");
        dateDepartCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDateDepart()));

        TableColumn<Trajet, LocalTime> heureDepartCol = new TableColumn<>("Heure de départ");
        heureDepartCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHeureDepart()));

        TableColumn<Trajet, Integer> placesDisponiblesCol = new TableColumn<>("Places disponibles");
        placesDisponiblesCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPlacesDisponibles()));

        TableColumn<Trajet, Double> prixCol = new TableColumn<>("Prix");
        prixCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrix()));

        TableColumn<Trajet, String> conducteurCol = new TableColumn<>("Conducteur");
        conducteurCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getConducteur().getNom()));

        tableViewTrajets.getColumns().addAll(lieuDepartCol, lieuArriveeCol, dateDepartCol, heureDepartCol, placesDisponiblesCol, prixCol, conducteurCol);
    }

    @FXML
    public void ajouterTrajet() {
        try {
            String lieuDepart = tfLieuDepart.getText();
            String lieuArrivee = tfLieuArrivee.getText();
            LocalDate dateDepart = dpDateDepart.getValue();
            String heureText = tfHeureDepart.getText();
            int placesDisponibles = Integer.parseInt(tfPlacesDisponibles.getText());
            double prix = Double.parseDouble(tfPrix.getText());
            User conducteur = cbConducteur.getSelectionModel().getSelectedItem();

            if (lieuDepart.isEmpty() || lieuArrivee.isEmpty() || dateDepart == null || heureText.isEmpty() || conducteur == null) {
                afficherErreur("Erreur de validation", "Tous les champs doivent être remplis");
                return;
            }

            LocalTime heureDepart;
            try {
                // Assurez-vous que le format de l'heure est correct
                heureDepart = LocalTime.parse(heureText, DateTimeFormatter.ofPattern("HH:mm"));
                heureErrorLabel.setVisible(false);
            } catch (Exception e) {
                heureErrorLabel.setText("Format de l'heure invalide. Utilisez HH:mm.");
                heureErrorLabel.setVisible(true);
                return;
            }

            Trajet trajet = new Trajet(lieuDepart, lieuArrivee, dateDepart, heureDepart, placesDisponibles, prix, conducteur);
            trajetRepository.save(trajet);

            chargerTrajets();
            effacerChamps();
        } catch (NumberFormatException e) {
            afficherErreur("Erreur de validation", "Les champs Places disponibles et Prix doivent contenir des valeurs numériques.");
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur d'ajout", "Impossible d'ajouter le trajet : " + e.getMessage());
        }
    }

    @FXML
    public void modifierTrajet() {
        Trajet selectedTrajet = tableViewTrajets.getSelectionModel().getSelectedItem();
        if (selectedTrajet != null) {
            try {
                selectedTrajet.setLieuDepart(tfLieuDepart.getText());
                selectedTrajet.setLieuArrivee(tfLieuArrivee.getText());
                LocalDate date = dpDateDepart.getValue();
                String heureText = tfHeureDepart.getText();
                LocalTime heure;
                try {
                    // Assurez-vous que le format de l'heure est correct
                    heure = LocalTime.parse(heureText, DateTimeFormatter.ofPattern("HH:mm"));
                    heureErrorLabel.setVisible(false);
                } catch (Exception e) {
                    heureErrorLabel.setText("Format de l'heure invalide. Utilisez HH:mm.");
                    heureErrorLabel.setVisible(true);
                    return;
                }
                selectedTrajet.setDateDepart(date);
                selectedTrajet.setHeureDepart(heure);
                selectedTrajet.setPlacesDisponibles(Integer.parseInt(tfPlacesDisponibles.getText()));
                selectedTrajet.setPrix(Double.parseDouble(tfPrix.getText()));
                selectedTrajet.setConducteur(cbConducteur.getSelectionModel().getSelectedItem());

                trajetRepository.update(selectedTrajet);
                chargerTrajets();
                effacerChamps();
            } catch (NumberFormatException e) {
                afficherErreur("Erreur de validation", "Les champs Places disponibles et Prix doivent contenir des valeurs numériques.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur de modification", "Impossible de modifier le trajet : " + e.getMessage());
            }
        } else {
            afficherErreur("Erreur de sélection", "Aucun trajet sélectionné");
        }
    }

    @FXML
    public void supprimerTrajet() {
        Trajet selectedTrajet = tableViewTrajets.getSelectionModel().getSelectedItem();
        if (selectedTrajet != null) {
            try {
                trajetRepository.delete(selectedTrajet);
                chargerTrajets();
                effacerChamps();
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur de suppression", "Impossible de supprimer le trajet : " + e.getMessage());
            }
        } else {
            afficherErreur("Erreur de sélection", "Aucun trajet sélectionné");
        }
    }

    @FXML
    public void rechercherTrajet() {
        String searchTerm = tfLieuDepart.getText();
        List<Trajet> trajets = trajetRepository.searchTrajets(searchTerm);
        tableViewTrajets.setItems(FXCollections.observableArrayList(trajets));
    }

    private void chargerConducteurs() {
        List<User> conducteurs = userRepository.findByRole(User.Role.CONDUCTEUR);
        cbConducteur.setItems(FXCollections.observableArrayList(conducteurs));
    }

    private void chargerTrajets() {
        List<Trajet> trajets = trajetRepository.getAll();
        tableViewTrajets.setItems(FXCollections.observableArrayList(trajets));
    }

    private void effacerChamps() {
        tfLieuDepart.clear();
        tfLieuArrivee.clear();
        dpDateDepart.setValue(null);
        tfHeureDepart.clear();
        heureErrorLabel.setVisible(false);
        tfPlacesDisponibles.clear();
        tfPrix.clear();
        cbConducteur.getSelectionModel().clearSelection();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
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
            Stage stage = (Stage) tableViewTrajets.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Error", "Unable to load dashboard.fxml");
        }
    }
}