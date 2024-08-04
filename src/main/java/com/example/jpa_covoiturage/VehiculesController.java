package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.Vehicule;
import com.example.jpa_covoiturage.Repository.VehiculeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class VehiculesController {

    @FXML private TableView<Vehicule> tableVehicules;
    @FXML private TableColumn<Vehicule, String> colMarque;
    @FXML private TableColumn<Vehicule, String> colModele;
    @FXML private TableColumn<Vehicule, String> colImmatriculation;

    @FXML private TextField txtMarque;
    @FXML private TextField txtModele;
    @FXML private TextField txtImmatriculation;
    @FXML private TextField txtRecherche;

    private VehiculeRepository vehiculeRepository = new

            VehiculeRepository();
    private ObservableList<Vehicule> listeVehicules = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));

        chargerVehicules();
    }

    @FXML
    private void ajouterVehicule() {
        String marque = txtMarque.getText();
        String modele = txtModele.getText();
        String immatriculation = txtImmatriculation.getText();
        Vehicule nouveauVehicule = new Vehicule(marque, modele, immatriculation);
        vehiculeRepository.addVehicule(nouveauVehicule);
        clearFields();
        chargerVehicules();
    }

    @FXML
    private void modifierVehicule() {
        Vehicule vehiculeSelectionne = tableVehicules.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            vehiculeSelectionne.setMarque(txtMarque.getText());
            vehiculeSelectionne.setModele(txtModele.getText());
            vehiculeSelectionne.setImmatriculation(txtImmatriculation.getText());
            vehiculeRepository.updateVehicule(vehiculeSelectionne);
            clearFields();
            chargerVehicules();
        }
    }

    @FXML
    private void supprimerVehicule() {
        Vehicule vehiculeSelectionne = tableVehicules.getSelectionModel().getSelectedItem();
        if (vehiculeSelectionne != null) {
            vehiculeRepository.removeVehicule(vehiculeSelectionne.getId());
            chargerVehicules();
        }
    }

    @FXML
    private void rechercherVehicule() {
        String recherche = txtRecherche.getText();
        if (recherche.isEmpty()) {
            chargerVehicules();
        } else {
            listeVehicules.clear();
            listeVehicules.addAll(vehiculeRepository.findVehiculesByMarqueOrModele(recherche));
            tableVehicules.setItems(listeVehicules);
        }
    }

    private void chargerVehicules() {
        listeVehicules.clear();
        listeVehicules.addAll(vehiculeRepository.getAllVehicules());
        tableVehicules.setItems(listeVehicules);
    }

    private void clearFields() {
        txtMarque.clear();
        txtModele.clear();
        txtImmatriculation.clear();
    }


}
