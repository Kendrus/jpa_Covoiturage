package com.example.jpa_covoiturage;

import com.example.jpa_covoiturage.Model.Utilisateur;
import com.example.jpa_covoiturage.Repository.UtilisateurRepository;
import com.example.jpa_covoiturage.service.AuthService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField userTextF;

    @FXML
    private PasswordField passTextF;

    @FXML
    private Label warnLabel;

    // Nouveaux champs pour l'inscription
    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label messageLabel;

    private AuthService authService;
    private static Utilisateur loggedInUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UtilisateurRepository utilisateurRepository = new UtilisateurRepository();
        this.authService = new AuthService(utilisateurRepository);
    }

    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }

    public static Utilisateur getLoggedInUser() {
        return loggedInUser;
    }

    @FXML
    void login(ActionEvent event) {
        String email = userTextF.getText();
        String mdp = passTextF.getText();

        Utilisateur utilisateur = authService.authentifier(email, mdp);

        if (utilisateur != null) {
            // Authentification réussie
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connexion réussie");
            alert.setHeaderText(null);
            alert.setContentText("Vous êtes connecté avec succès!");
            alert.showAndWait();

            // Stocker l'utilisateur connecté
            HelloController.setLoggedInUser(utilisateur);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/jpa_covoiturage/dashboard.fxml"));
                Parent dashboardRoot = loader.load();
                Scene dashboardScene = new Scene(dashboardRoot);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Erreur lors du chargement de la page : " + e.getMessage());
            }
        } else {
            // Authentification échouée
            warnLabel.setText("Email ou mot de passe incorrect");
            warnLabel.setVisible(true);
        }
    }








    @FXML
    public void viewRegister(ActionEvent actionEvent) {
        System.out.println("Tentative de chargement de register.fxml");
        URL fxmlUrl = getClass().getResource("/register.fxml");
        if (fxmlUrl == null) {
            System.err.println("Le fichier register.fxml n'a pas été trouvé.");
            // Afficher une alerte à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Fichier FXML introuvable");
            alert.setContentText("Le fichier register.fxml n'a pas pu être chargé.");
            alert.showAndWait();
            return;
        }
        System.out.println("Fichier register.fxml trouvé à : " + fxmlUrl.toString());

        // Le reste du code pour charger la scène...
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (password.equals(confirmPassword)) {
            Utilisateur newUser = new Utilisateur(nom, prenom, email, password, role);
            boolean success = authService.enregistrerUtilisateur(newUser, password);
            if (success) {
                messageLabel.setText("Inscription réussie!");
                // Optionnel : rediriger vers la page de connexion
                switchToLogin(event);
            } else {
                messageLabel.setText("Erreur lors de l'inscription. L'email existe peut-être déjà.");
            }
        } else {
            messageLabel.setText("Les mots de passe ne correspondent pas.");
        }
    }

    @FXML
    public void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page de connexion : " + e.getMessage());
        }
    }
}