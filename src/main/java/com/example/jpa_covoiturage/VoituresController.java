package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.User;
import com.example.jpa_covoiturage.Model.Voiture;
import com.example.jpa_covoiturage.Repository.UserRepository;
import com.example.jpa_covoiturage.Repository.VoitureRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class VoituresController {

    @FXML private TextField marqueField;
    @FXML private TextField modeleField;
    @FXML private TextField immatriculationField;
    @FXML private TextField anneeField;
    @FXML private ComboBox<User> conducteurComboBox;
    @FXML private TableView<Voiture> voitureTableView;
    @FXML private TableColumn<Voiture, String> marqueColumn;
    @FXML private TableColumn<Voiture, String> modeleColumn;
    @FXML private TableColumn<Voiture, String> immatriculationColumn;
    @FXML private TableColumn<Voiture, Integer> anneeColumn;
    @FXML private TableColumn<Voiture, String> conducteurColumn;
    @FXML private TextField searchField;

    private VoitureRepository voitureRepository;
    public UserRepository userRepository;
    private ObservableList<Voiture> voitureList = FXCollections.observableArrayList();

    public VoituresController() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PERSISTENCE");
        this.voitureRepository = new VoitureRepository(emf);
        this.userRepository = new UserRepository(emf);
    }

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        immatriculationColumn.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("annee"));
        conducteurColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getConducteur().getNom() + " " + cellData.getValue().getConducteur().getPrenom()));

        // Chargement des données
        loadVoitures();
        loadConducteurs();
    }

    private void loadVoitures() {
        voitureList.clear();
        voitureList.addAll(voitureRepository.findAll());
        voitureTableView.setItems(voitureList);
    }

    private void loadConducteurs() {
        ObservableList<User> conducteurs = FXCollections.observableArrayList(
                userRepository.findByRole(User.Role.CONDUCTEUR)
        );
        conducteurComboBox.setItems(conducteurs);
    }

    @FXML
    private void handleAddVoiture() {
        try {
            String marque = marqueField.getText();
            String modele = modeleField.getText();
            String immatriculation = immatriculationField.getText();
            int annee = Integer.parseInt(anneeField.getText());
            User conducteur = conducteurComboBox.getValue();

            if (conducteur == null || !conducteur.estConducteur()) {
                showAlert("Erreur", "Veuillez sélectionner un conducteur valide.");
                return;
            }

            Voiture newVoiture = new Voiture(marque, modele, immatriculation, annee, conducteur);
            voitureRepository.save(newVoiture);
            loadVoitures();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'année doit être un nombre valide.");
        }
    }

    @FXML
    private void handleUpdateVoiture() {
        Voiture selectedVoiture = voitureTableView.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            try {
                selectedVoiture.setMarque(marqueField.getText());
                selectedVoiture.setModele(modeleField.getText());
                selectedVoiture.setImmatriculation(immatriculationField.getText());
                selectedVoiture.setAnnee(Integer.parseInt(anneeField.getText()));
                User conducteur = conducteurComboBox.getValue();
                if (conducteur != null && conducteur.estConducteur()) {
                    selectedVoiture.setConducteur(conducteur);
                } else {
                    showAlert("Erreur", "Veuillez sélectionner un conducteur valide.");
                    return;
                }
                voitureRepository.save(selectedVoiture);
                loadVoitures();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'année doit être un nombre valide.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une voiture à modifier.");
        }
    }

    @FXML
    private void handleDeleteVoiture() {
        Voiture selectedVoiture = voitureTableView.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            voitureRepository.delete(selectedVoiture.getId());
            loadVoitures();
            clearFields();
        } else {
            showAlert("Erreur", "Veuillez sélectionner une voiture à supprimer.");
        }
    }

    @FXML
    private void handleSearchVoiture() {
        String searchTerm = searchField.getText();
        if (!searchTerm.isEmpty()) {
            voitureList.clear();
            voitureList.addAll(voitureRepository.findByMarqueOrModeleOrImmatriculation(searchTerm));
        } else {
            loadVoitures();
        }
    }

    private void clearFields() {
        marqueField.clear();
        modeleField.clear();
        immatriculationField.clear();
        anneeField.clear();
        conducteurComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}