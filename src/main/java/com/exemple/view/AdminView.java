package com.exemple.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.exemple.models.Employee;
import com.exemple.models.Services;
import com.exemple.models.Sites;
import com.exemple.services.EmployeeService;
import com.exemple.services.ServiceService;
import com.exemple.services.SiteService;


/**
 * Administrator interface - CRUD management of Sites, Services and Employees
 */

public class AdminView extends Application {

    private TableView<Sites> sitesTable;
    private TableView<Services> servicesTable;
    private TableView<Employee> employeesTable;
    
    private ObservableList<Sites> sitesList;
    private ObservableList<Services> servicesList;
    private ObservableList<Employee> employeesList;
    private ObservableList<Sites> sitesComboList;
    private ObservableList<Services> servicesComboList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aggro.io ⎮ Administrator View");

        // Initialisation des listes
        sitesList = FXCollections.observableArrayList();
        servicesList = FXCollections.observableArrayList();
        employeesList = FXCollections.observableArrayList();
        sitesComboList = FXCollections.observableArrayList();
        servicesComboList = FXCollections.observableArrayList();

        // Création de l'interface avec onglets
        TabPane tabPane = new TabPane();

        // Onglet Sites
        Tab sitesTab = new Tab("Sites", createSitesTab());
        sitesTab.setClosable(false);

        // Onglet Services
        Tab servicesTab = new Tab("Services", createServicesTab());
        servicesTab.setClosable(false);

        // Employees tab
        Tab employeesTab = new Tab("Employees", createEmployeesTab());
        employeesTab.setClosable(false);

        tabPane.getTabs().addAll(sitesTab, servicesTab, employeesTab);

        // Barre de menu
        HBox menuBar = new HBox(10);
        menuBar.setPadding(new Insets(10));
        menuBar.setAlignment(Pos.CENTER_RIGHT);
        menuBar.setStyle("-fx-background-color: #000000ff;");

        Label welcomeLabel = new Label();
        if (LoginView.getCurrentAdmin() != null) {
            welcomeLabel.setText("Welcome to Aggro.io, " + LoginView.getCurrentAdmin().getFirstName() + " " + 
                               LoginView.getCurrentAdmin().getLastName());
        }
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button logoutButton = new Button("Log out");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            javafx.application.Platform.runLater(() -> {
                    LoginView.logout();
                    primaryStage.close();
                    VisitorView visitorApp = new VisitorView();
                    Stage newStage = new Stage();
                    newStage.setMaximized(true);
                    newStage.setResizable(true);
                    visitorApp.start(newStage);
            });
        });

       
        menuBar.getChildren().addAll(welcomeLabel, logoutButton);

        VBox root = new VBox();
        root.getChildren().addAll(menuBar, tabPane);

        // Chargement initial
        refreshAllData();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private VBox createSitesTab() {
        VBox tabContent = new VBox(15);
        tabContent.setPadding(new Insets(20));

        // Titre
        Label title = new Label("Site Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Formulaire
        HBox formBox = new HBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-border-radius: 5; -fx-background-radius: 5;");

        TextField cityField = new TextField();
        cityField.setPromptText("City name");
        cityField.setPrefWidth(200);

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #008035ff;; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            String city = cityField.getText().trim();
            if (city.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation");
                alert.setHeaderText("Empty field");
                alert.setContentText("Please enter a city name.");
                alert.showAndWait();
                return;
            }
            try {
                SiteService.createSite(city);
                cityField.clear();
                refreshSites();
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("Site successfully added !");
                success.showAndWait();
            } catch (Exception ex) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText(null);
                error.setContentText("Error adding site : " + ex.getMessage());
                error.showAndWait();
            }
        });

        formBox.getChildren().addAll(new Label("City : "), cityField, addButton);

        // Tableau
        sitesTable = new TableView<>();
        sitesTable.setPrefHeight(400);

        TableColumn<Sites, Integer> idCol = new TableColumn<>("N°");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idCol.setPrefWidth(80);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Sites, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("City"));
        cityCol.setPrefWidth(400);

        sitesTable.getColumns().add(idCol);
        sitesTable.getColumns().add(cityCol);
        sitesTable.setItems(sitesList);

        // Boutons d'action
        HBox actionBox = new HBox(10);
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #0015ffff; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            Sites selected = sitesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditSiteDialog(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a site to edit.");
                alert.showAndWait();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #ff1900ff; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Sites selected = sitesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setHeaderText("Delete the site");
                confirm.setContentText("Are you sure you want to delete " + selected.getCity() + " ?\n\nWarning: This action will also delete all employees associated with this site.");
                if (confirm.showAndWait().get() == ButtonType.OK) {
                    try {
                        SiteService.deleteSite(selected.getID());
                        refreshSites();
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success");
                        success.setHeaderText(null);
                        success.setContentText("Site successfully deleted !");
                        success.showAndWait();
                    } catch (Exception ex) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText(null);
                        error.setContentText("Error during deletion : " + ex.getMessage());
                        error.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a site to delete.");
                alert.showAndWait();
            }
        });

        actionBox.getChildren().addAll(editButton, deleteButton);

        tabContent.getChildren().addAll(title, formBox, sitesTable, actionBox);
        return tabContent;
    }

    private VBox createServicesTab() {
        VBox tabContent = new VBox(15);
        tabContent.setPadding(new Insets(20));

        Label title = new Label("Service Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox formBox = new HBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-border-radius: 5; -fx-background-radius: 5;");

        TextField nameField = new TextField();
        nameField.setPromptText("Service name");
        nameField.setPrefWidth(200);

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #008035ff;; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation");
                alert.setHeaderText("Empty field");
                alert.setContentText("Please enter a service name.");
                alert.showAndWait();
                return;
            }
            try {
                ServiceService.createService(name);
                nameField.clear();
                refreshServices();
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("Service successfully added !");
                success.showAndWait();
            } catch (Exception ex) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText(null);
                error.setContentText("Error while adding the service : " + ex.getMessage());
                error.showAndWait();
            }
        });

        formBox.getChildren().addAll(new Label("Service : "), nameField, addButton);

        servicesTable = new TableView<>();
        servicesTable.setPrefHeight(400);

        TableColumn<Services, Integer> idCol = new TableColumn<>("N°");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idCol.setPrefWidth(80);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Services, String> nameCol = new TableColumn<>("Service Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        nameCol.setPrefWidth(400);

        servicesTable.getColumns().add(idCol);
        servicesTable.getColumns().add(nameCol);
        servicesTable.setItems(servicesList);

        HBox actionBox = new HBox(10);
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #0400ffff; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            Services selected = servicesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditServiceDialog(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a service to modify.");
                alert.showAndWait();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Services selected = servicesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setHeaderText("Delete service");
                confirm.setContentText("Are you sure you want to delete " + selected.getName() + " ?\n\nWarning: This action will also delete all employees associated with this service.");
                if (confirm.showAndWait().get() == ButtonType.OK) {
                    try {
                        ServiceService.deleteService(selected.getID());
                        refreshServices();
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success");
                        success.setHeaderText(null);
                        success.setContentText("Service successfully deleted !");
                        success.showAndWait();
                    } catch (Exception ex) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText(null);
                        error.setContentText("Error during deletion : " + ex.getMessage());
                        error.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a service to delete.");
                alert.showAndWait();
            }
        });

        actionBox.getChildren().addAll(editButton, deleteButton);

        tabContent.getChildren().addAll(title, formBox, servicesTable, actionBox);
        return tabContent;
    }

    private VBox createEmployeesTab() {
        VBox tabContent = new VBox(15);
        tabContent.setPadding(new Insets(20));

        Label title = new Label("Employee Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Formulaire
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-border-radius: 5; -fx-background-radius: 5;");

        HBox row1 = new HBox(10);
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Name");
        row1.getChildren().addAll(new Label("First name : "), firstNameField, new Label("Last name : "), lastNameField);

        HBox row2 = new HBox(10);
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        TextField cellField = new TextField();
        cellField.setPromptText("Phone");
        row2.getChildren().addAll(new Label("Phone : "), phoneField, new Label("Mobile : "), cellField);

        HBox row3 = new HBox(10);
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        ComboBox<Sites> siteCombo = new ComboBox<>(sitesComboList);
        siteCombo.setPromptText("Select a site");
        // Personnaliser l'affichage des sites dans la ComboBox
        siteCombo.setCellFactory(listView -> new javafx.scene.control.ListCell<Sites>() {
            @Override
            protected void updateItem(Sites site, boolean empty) {
                super.updateItem(site, empty);
                if (empty || site == null) {
                    setText(null);
                } else {
                    setText(site.getCity());
                }
            }
        });
        siteCombo.setButtonCell(new javafx.scene.control.ListCell<Sites>() {
            @Override
            protected void updateItem(Sites site, boolean empty) {
                super.updateItem(site, empty);
                if (empty || site == null) {
                    setText("Select a site");
                } else {
                    setText(site.getCity());
                }
            }
        });
        
        ComboBox<Services> serviceCombo = new ComboBox<>(servicesComboList);
        serviceCombo.setPromptText("Select a service");
        // Personnaliser l'affichage des services dans la ComboBox
        serviceCombo.setCellFactory(listView -> new javafx.scene.control.ListCell<Services>() {
            @Override
            protected void updateItem(Services service, boolean empty) {
                super.updateItem(service, empty);
                if (empty || service == null) {
                    setText(null);
                } else {
                    setText(service.getName());
                }
            }
        });
        serviceCombo.setButtonCell(new javafx.scene.control.ListCell<Services>() {
            @Override
            protected void updateItem(Services service, boolean empty) {
                super.updateItem(service, empty);
                if (empty || service == null) {
                    setText("Select a service");
                } else {
                    setText(service.getName());
                }
            }
        });
        row3.getChildren().addAll(new Label("Email : "), emailField, new Label("Site : "), siteCombo, 
                                  new Label("Service : "), serviceCombo);

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #008035ff; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            if (validateEmployeeForm(firstNameField, lastNameField, phoneField, cellField, emailField, siteCombo, serviceCombo)) {
                try {
                    // Nettoyer les numéros de téléphone
                    String cleanedPhone = com.exemple.util.ValidationUtils.cleanPhoneNumber(phoneField.getText());
                    String cleanedCell = com.exemple.util.ValidationUtils.cleanPhoneNumber(cellField.getText());
                    
                    EmployeeService.createEmployee(
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        cleanedPhone,
                        cleanedCell,
                        emailField.getText().trim(),
                        serviceCombo.getValue(),
                        siteCombo.getValue()
                    );
                    clearEmployeeForm(firstNameField, lastNameField, phoneField, cellField, emailField, siteCombo, serviceCombo);
                    refreshEmployees();
                    
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText(null);
                    success.setContentText("Employee successfully added ! ");
                    success.showAndWait();
                } catch (Exception ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("Error adding employee : " + ex.getMessage());
                    error.showAndWait();
                }
            }
        });

        formBox.getChildren().addAll(row1, row2, row3, addButton);

        // Tableau
        employeesTable = new TableView<>();
        employeesTable.setPrefHeight(350);

        // Colonne ID
        TableColumn<Employee, Integer> idCol = new TableColumn<>("N°");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idCol.setPrefWidth(60);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Employee, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        firstNameCol.setPrefWidth(120);

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        lastNameCol.setPrefWidth(120);

        TableColumn<Employee, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        phoneCol.setPrefWidth(130);
        phoneCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Employee, String> cellCol = new TableColumn<>("Mobile");
        cellCol.setCellValueFactory(new PropertyValueFactory<>("Cell"));
        cellCol.setPrefWidth(140);
        cellCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Employee, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        emailCol.setPrefWidth(200);

        TableColumn<Employee, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(cellData -> {
            Services service = cellData.getValue().getService();
            return new javafx.beans.property.SimpleStringProperty(service != null ? service.getName() : "N/A");
        });
        serviceCol.setPrefWidth(180);

        TableColumn<Employee, String> siteCol = new TableColumn<>("Site");
        siteCol.setCellValueFactory(cellData -> {
            Sites site = cellData.getValue().getSite();
            return new javafx.beans.property.SimpleStringProperty(site != null ? site.getCity() : "N/A");
        });
        siteCol.setPrefWidth(200);

        employeesTable.getColumns().add(idCol);
        employeesTable.getColumns().add(firstNameCol);
        employeesTable.getColumns().add(lastNameCol);
        employeesTable.getColumns().add(phoneCol);
        employeesTable.getColumns().add(cellCol);
        employeesTable.getColumns().add(emailCol);
        employeesTable.getColumns().add(serviceCol);
        employeesTable.getColumns().add(siteCol);
        employeesTable.setItems(employeesList);
        
        // Permettre la sélection pour faciliter les opérations CRUD
        employeesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        HBox actionBox = new HBox(10);
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #002affff; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            Employee selected = employeesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditEmployeeDialog(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select an employee to edit.");
                alert.showAndWait();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #ff1900ff; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Employee selected = employeesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setHeaderText("Delete Employee");
                confirm.setContentText("Are you sure you want to delete " + selected.getFirstName() + " " + selected.getLastName() + " ?");
                if (confirm.showAndWait().get() == ButtonType.OK) {
                    try {
                        EmployeeService.deleteEmployee(selected.getID());
                        refreshEmployees();
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success");
                        success.setHeaderText(null);
                        success.setContentText("Employee successfully deleted!");
                        success.showAndWait();
                    } catch (Exception ex) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText(null);
                        error.setContentText("Error during deletion: " + ex.getMessage());
                        error.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select an employee to delete.");
                alert.showAndWait();
            }
        });

        actionBox.getChildren().addAll(editButton, deleteButton);

        tabContent.getChildren().addAll(title, formBox, employeesTable, actionBox);
        return tabContent;
    }

    private boolean validateEmployeeForm(TextField firstName, TextField lastName, TextField phone, 
                                        TextField cell, TextField email, ComboBox<Sites> site, ComboBox<Services> service) {
        // Vérifier les champs obligatoires
        if (!com.exemple.util.ValidationUtils.isNotEmpty(firstName.getText()) || 
            !com.exemple.util.ValidationUtils.isNotEmpty(lastName.getText()) ||
            !com.exemple.util.ValidationUtils.isNotEmpty(phone.getText()) || 
            !com.exemple.util.ValidationUtils.isNotEmpty(cell.getText()) ||
            !com.exemple.util.ValidationUtils.isNotEmpty(email.getText()) || 
            site.getValue() == null || service.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return false;
        }

        // Valider l'email
        if (!com.exemple.util.ValidationUtils.isValidEmail(email.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Invalid Email");
            alert.setContentText("Please enter a valid email address.");
            alert.showAndWait();
            return false;
        }

        // Valider les numéros de téléphone
        String cleanedPhone = com.exemple.util.ValidationUtils.cleanPhoneNumber(phone.getText());
        String cleanedCell = com.exemple.util.ValidationUtils.cleanPhoneNumber(cell.getText());
        
        if (!com.exemple.util.ValidationUtils.isValidPhone(cleanedPhone)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Invalid Phone Number");
            alert.setContentText("The phone number must contain 10 digits.");
            alert.showAndWait();
            return false;
        }

        if (!com.exemple.util.ValidationUtils.isValidPhone(cleanedCell)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Invalid Mobile Number");
            alert.setContentText("The mobile number must contain 10 digits.");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void clearEmployeeForm(TextField firstName, TextField lastName, TextField phone, 
                                  TextField cell, TextField email, ComboBox<Sites> site, ComboBox<Services> service) {
        firstName.clear();
        lastName.clear();
        phone.clear();
        cell.clear();
        email.clear();
        site.setValue(null);
        service.setValue(null);
    }

    private void showEditSiteDialog(Sites site) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Site");
        dialog.setHeaderText("Edit City");
        // Ensure dialog is not maximized and has a reasonable size
        dialog.setResizable(false);
        dialog.getDialogPane().setPrefSize(400, 150);

        TextField cityField = new TextField(site.getCity());
        cityField.setPrefWidth(200);

        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("City : "), cityField);
        dialog.getDialogPane().setContent(content);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return cityField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newCity -> {
            if (newCity != null && !newCity.trim().isEmpty()) {
                try {
                    site.setCity(newCity.trim());
                    SiteService.updateSite(site);
                    refreshSites();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText(null);
                    success.setContentText("Site successfully updated!");
                    success.showAndWait();
                } catch (Exception ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("Error during modification: " + ex.getMessage());
                    error.showAndWait();
                }
            }
        });
    }

    private void showEditServiceDialog(Services service) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Service");
        dialog.setHeaderText("Edit Name");
        // Ensure dialog is not maximized and has a reasonable size
        dialog.setResizable(false);
        dialog.getDialogPane().setPrefSize(400, 150);

        TextField nameField = new TextField(service.getName());
        nameField.setPrefWidth(200);

        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Name : "), nameField);
        dialog.getDialogPane().setContent(content);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return nameField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newName -> {
            if (newName != null && !newName.trim().isEmpty()) {
                try {
                    service.setName(newName.trim());
                    ServiceService.updateService(service);
                    refreshServices();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText(null);
                    success.setContentText("Service successfully updated!");
                    success.showAndWait();
                } catch (Exception ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("Error during modification: " + ex.getMessage());
                    error.showAndWait();
                }
            }
        });
    }

    private void showEditEmployeeDialog(Employee employee) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");
        dialog.setHeaderText("Edit Information");
        // Ensure dialog is not maximized and has a reasonable size
        dialog.setResizable(false);
        dialog.getDialogPane().setPrefSize(500, 400);

        TextField firstNameField = new TextField(employee.getFirstName());
        TextField lastNameField = new TextField(employee.getLastName());
        TextField phoneField = new TextField(employee.getPhone());
        TextField cellField = new TextField(employee.getCell());
        TextField emailField = new TextField(employee.getEmail());
        
        ComboBox<Sites> siteCombo = new ComboBox<>(sitesComboList);
        siteCombo.setValue(employee.getSite());
        siteCombo.setCellFactory(listView -> new javafx.scene.control.ListCell<Sites>() {
            @Override
            protected void updateItem(Sites site, boolean empty) {
                super.updateItem(site, empty);
                if (empty || site == null) {
                    setText(null);
                } else {
                    setText(site.getCity());
                }
            }
        });
        siteCombo.setButtonCell(new javafx.scene.control.ListCell<Sites>() {
            @Override
            protected void updateItem(Sites site, boolean empty) {
                super.updateItem(site, empty);
                if (empty || site == null) {
                    setText("Select a site");
                } else {
                    setText(site.getCity());
                }
            }
        });
        
        ComboBox<Services> serviceCombo = new ComboBox<>(servicesComboList);
        serviceCombo.setValue(employee.getService());
        serviceCombo.setCellFactory(listView -> new javafx.scene.control.ListCell<Services>() {
            @Override
            protected void updateItem(Services service, boolean empty) {
                super.updateItem(service, empty);
                if (empty || service == null) {
                    setText(null);
                } else {
                    setText(service.getName());
                }
            }
        });
        serviceCombo.setButtonCell(new javafx.scene.control.ListCell<Services>() {
            @Override
            protected void updateItem(Services service, boolean empty) {
                super.updateItem(service, empty);
                if (empty || service == null) {
                    setText("Select a service");
                } else {
                    setText(service.getName());
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("First name : "), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last name : "), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Phone : "), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Mobile : "), 0, 3);
        grid.add(cellField, 1, 3);
        grid.add(new Label("Email : "), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Site : "), 0, 5);
        grid.add(siteCombo, 1, 5);
        grid.add(new Label("Service : "), 0, 6);
        grid.add(serviceCombo, 1, 6);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                employee.setFirstName(firstNameField.getText().trim());
                employee.setLastName(lastNameField.getText().trim());
                employee.setPhone(phoneField.getText().trim());
                employee.setCell(cellField.getText().trim());
                employee.setEmail(emailField.getText().trim());
                employee.setSite(siteCombo.getValue());
                employee.setService(serviceCombo.getValue());
                return employee;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedEmployee -> {
            if (updatedEmployee != null) {
                try {
                    // Valider avant de sauvegarder
                    if (!com.exemple.util.ValidationUtils.isValidEmail(updatedEmployee.getEmail())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Validation");
                        alert.setHeaderText("Invalid Email");
                        alert.setContentText("Please enter a valid email address.");
                        alert.showAndWait();
                        return;
                    }
                    
                    // Nettoyer les numéros de téléphone
                    String cleanedPhone = com.exemple.util.ValidationUtils.cleanPhoneNumber(updatedEmployee.getPhone());
                    String cleanedCell = com.exemple.util.ValidationUtils.cleanPhoneNumber(updatedEmployee.getCell());
                    
                    if (!com.exemple.util.ValidationUtils.isValidPhone(cleanedPhone) || 
                        !com.exemple.util.ValidationUtils.isValidPhone(cleanedCell)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Validation");
                        alert.setHeaderText("Invalid Phone Numbers");
                        alert.setContentText("Phone numbers must contain 10 digits.");
                        alert.showAndWait();
                        return;
                    }
                    
                    updatedEmployee.setPhone(cleanedPhone);
                    updatedEmployee.setCell(cleanedCell);
                    
                    EmployeeService.updateEmployee(updatedEmployee);
                    refreshEmployees();
                    
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText(null);
                    success.setContentText("Employee successfully updated!");
                    success.showAndWait();
                } catch (Exception ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText(null);
                    error.setContentText("Error during modification: " + ex.getMessage());
                    error.showAndWait();
                }
            }
        });
    }

    private void refreshAllData() {
        refreshSites();
        refreshServices();
        refreshEmployees();
    }

    private void refreshSites() {
        sitesList.clear();
        sitesList.addAll(SiteService.getAllSites());
        sitesComboList.clear();
        sitesComboList.addAll(SiteService.getAllSites());
    }

    private void refreshServices() {
        servicesList.clear();
        servicesList.addAll(ServiceService.getAllServices());
        servicesComboList.clear();
        servicesComboList.addAll(ServiceService.getAllServices());
    }

    private void refreshEmployees() {
        employeesList.clear();
        employeesList.addAll(EmployeeService.getAllEmployees());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

