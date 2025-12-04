package com.exemple.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.exemple.models.Admin;
import com.exemple.services.AdminService;

/**
 * Administrator login interface
 */
public class LoginView extends Application {

    private static Admin currentAdmin = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Administractor");

        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #000000ff;");

        // Titre
        Label titleLabel = new Label("Login Administrator");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Formulaire de connexion
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        formBox.setMaxWidth(400);

        // Champ email
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefHeight(35);

        // Champ mot de passe
        Label passwordLabel = new Label("Password : ");
        passwordLabel.setStyle("-fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password : ");
        passwordField.setPrefHeight(35);

        // Message d'erreur
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ff1900ff; -fx-font-size: 12px;");
        errorLabel.setVisible(false);

        // Bouton de connexion
        Button loginButton = new Button("Log in");
        loginButton.setStyle("-fx-background-color: #60b400ff; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(40);

        // Action du bouton
        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please complete all fields.");
                errorLabel.setVisible(true);
                return;
            }

            Admin admin = AdminService.authenticate(email, password);
            if (admin != null) {
                currentAdmin = admin;
                errorLabel.setVisible(false);
                // Ouvrir l'interface administrateur
                openAdminView(primaryStage);
            } else {
                errorLabel.setText("Incorrect email or password.");
                errorLabel.setVisible(true);
                passwordField.clear();
            }
        });

        // Permettre la connexion avec Entrée
        passwordField.setOnAction(e -> loginButton.fire());

        formBox.getChildren().addAll(
            emailLabel, emailField,
            passwordLabel, passwordField,
            errorLabel,
            loginButton
        );

        // Bouton retour visiteur
        Button visitorButton = new Button("Visitor Mode");
        visitorButton.setStyle("-fx-background-color: #000000ff; -fx-text-fill: white;");
        visitorButton.setOnAction(e -> {
            openVisitorView(primaryStage);
        });

        root.getChildren().addAll(titleLabel, formBox, visitorButton);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void openAdminView(Stage primaryStage) {
        AdminView adminView = new AdminView();
        adminView.start(primaryStage);
    }

    private void openVisitorView(Stage primaryStage) {
        // Créer une nouvelle scène pour l'interface visiteur
        try {
                VisitorView visitorApp = new VisitorView();
            visitorApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode statique pour créer la scène de login (utilisable depuis d'autres vues)
    public static Scene createLoginScene(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Titre
        Label titleLabel = new Label("Administrator Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #000000ff;");

        // Formulaire de connexion
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        formBox.setMaxWidth(400);

        // Champ email
        Label emailLabel = new Label("Email :");
        emailLabel.setStyle("-fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.setPrefHeight(35);

        // Champ mot de passe
        Label passwordLabel = new Label("Password :");
        passwordLabel.setStyle("-fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(35);

        // Message d'erreur
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
        errorLabel.setVisible(false);

        // Bouton de connexion
        Button loginButton = new Button("Log in");
        loginButton.setStyle("-fx-background-color: #0011ffff; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(40);

        // Action du bouton
        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please complete all fields.");
                errorLabel.setVisible(true);
                return;
            }

            Admin admin = AdminService.authenticate(email, password);
            if (admin != null) {
                currentAdmin = admin;
                errorLabel.setVisible(false);
                // Ouvrir l'interface administrateur
                javafx.application.Platform.runLater(() -> {
                    Stage currentStage = (Stage) loginButton.getScene().getWindow();
                    AdminView adminView = new AdminView();
                    currentStage.setMaximized(true);
                    currentStage.setResizable(true);
                    adminView.start(currentStage);
                });
            } else {
                // Vérifier s'il n'y a aucun admin dans la base
                if (AdminService.getAllAdmins().isEmpty()) {
                    errorLabel.setText("No administrator found. Please create an administrator account.");
                    errorLabel.setVisible(true);
                } else {
                    errorLabel.setText("Email or password incorrect.");
                    errorLabel.setVisible(true);
                }
                passwordField.clear();
            }
        });

        // Permettre la connexion avec Entrée
        passwordField.setOnAction(e -> loginButton.fire());

        formBox.getChildren().addAll(
            emailLabel, emailField,
            passwordLabel, passwordField,
            errorLabel,
            loginButton
        );

        // Bouton retour visiteur
        Button visitorButton = new Button("Visitor Mode");
        visitorButton.setStyle("-fx-background-color: #000000ff; -fx-text-fill: white;");
        visitorButton.setOnAction(e -> {
            javafx.application.Platform.runLater(() -> {
                try {
                    Stage currentStage = (Stage) visitorButton.getScene().getWindow();
                    VisitorView visitorApp = new VisitorView();
                    currentStage.setMaximized(true);
                    currentStage.setResizable(true);
                    visitorApp.start(currentStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        root.getChildren().addAll(titleLabel, formBox, visitorButton);
        Scene scene = new Scene(root);
        return scene;
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static boolean isAdminLoggedIn() {
        return currentAdmin != null;
    }

    public static void logout() {
        currentAdmin = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

