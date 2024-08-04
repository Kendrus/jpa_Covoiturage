package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.User;
import com.example.jpa_covoiturage.Repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class UserController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField mdpField;
    @FXML private ComboBox<User.Role> roleComboBox;
    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, User.Role> roleColumn;
    @FXML private TextField searchField;

    private UserRepository userRepository;
    private ObservableList<User> userList = FXCollections.observableArrayList();

    public UserController() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PERSISTENCE");
        this.userRepository = new UserRepository(emf);
    }

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Initialisation du ComboBox des rôles
        roleComboBox.getItems().addAll(User.Role.values());

        // Chargement des données
        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(userRepository.findAll());
        userTableView.setItems(userList);
    }

    @FXML
    private void handleAddUser() {
        User newUser = new User(
                nomField.getText(),
                prenomField.getText(),
                emailField.getText(),
                mdpField.getText(),
                roleComboBox.getValue()
        );
        userRepository.save(newUser);
        loadUsers();
        clearFields();
    }

    @FXML
    private void handleUpdateUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setNom(nomField.getText());
            selectedUser.setPrenom(prenomField.getText());
            selectedUser.setEmail(emailField.getText());
            selectedUser.setMdp(mdpField.getText());
            selectedUser.setRole(roleComboBox.getValue());
            userRepository.save(selectedUser);
            loadUsers();
            clearFields();
        } else {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userRepository.delete(selectedUser.getId());
            loadUsers();
            clearFields();
        } else {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    @FXML
    private void handleSearchUser() {
        String searchTerm = searchField.getText();
        if (!searchTerm.isEmpty()) {
            userList.clear();
            userList.addAll(userRepository.searchUsers(searchTerm));
        } else {
            loadUsers();
        }
    }
    @FXML
    public void handleBackToDashboard(ActionEvent actionEvent) {
        try {
            URL resourceUrl = getClass().getResource("dashboard.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            if (resourceUrl == null) {
                throw new IOException("Cannot find resource: dashboard.fxml");
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            AnchorPane root = loader.load();

            Stage stage = (Stage) userTableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de revenir au tableau de bord: " + e.getMessage());
        }
    }
    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        mdpField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
