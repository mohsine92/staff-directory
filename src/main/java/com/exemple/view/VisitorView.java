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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.exemple.models.Employee;
import com.exemple.models.Services;
import com.exemple.models.Sites;
import com.exemple.services.EmployeeService;
import com.exemple.services.PDFService;
import com.exemple.services.ServiceService;
import com.exemple.services.SiteService;

/**
 * Visitor interface - Employee search and display
 */
public class VisitorView extends Application {

    private TableView<Employee> employeeTable;
    private TextField nameSearchField;
    private ComboBox<Sites> siteComboBox;
    private ComboBox<Services> serviceComboBox;
    private ObservableList<Employee> employeeList;
    private ObservableList<Sites> siteList;
    private ObservableList<Services> serviceList;
    private Label nameDetail, phoneDetail, cellDetail, emailDetail, serviceDetail, siteDetail;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aggro.io - Staff directory");

        // Initialisation des listes
        employeeList = FXCollections.observableArrayList();
        siteList = FXCollections.observableArrayList(SiteService.getAllSites());
        serviceList = FXCollections.observableArrayList(ServiceService.getAllServices());

        // Création de l'interface
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #00000019;");

        // Titre et bouton admin
        HBox titleBox = new HBox(15);
        titleBox.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Search a Employee");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Button adminButton = new Button("Administrator");
        adminButton.setStyle("-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-weight: bold;");
        adminButton.setOnAction(e -> {
            Stage stage = (Stage) adminButton.getScene().getWindow();
            javafx.application.Platform.runLater(() -> {
                Scene loginScene = com.exemple.view.LoginView.createLoginScene(stage);
                stage.setScene(loginScene);
                stage.setTitle("Login Administrator");
                stage.setMaximized(true);
                stage.setResizable(true);
            });
        });
        
        titleBox.getChildren().addAll(titleLabel, adminButton);

        // Zone de recherche
        VBox searchBox = createSearchBox();

        // Tableau des résultats
        employeeTable = createEmployeeTable();

        // Zone de détails
        VBox detailsBox = createDetailsBox();

        // Layout principal
        root.getChildren().addAll(titleBox, searchBox, employeeTable, detailsBox);

        // Chargement initial
        refreshEmployeeList();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private VBox createSearchBox() {
        VBox searchBox = new VBox(10);
        searchBox.setPadding(new Insets(15));
        searchBox.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label searchLabel = new Label("Search criteria");
        searchLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Champ de recherche par nom
        HBox nameBox = new HBox(10);
        Label nameLabel = new Label("Last name/First name :");
        nameLabel.setMinWidth(100);
        nameSearchField = new TextField();
        nameSearchField.setPromptText("Enter a first or last name...");
        nameSearchField.setPrefWidth(300);
        nameBox.getChildren().addAll(nameLabel, nameSearchField);

        // ComboBox pour le site
        HBox siteBox = new HBox(10);
        Label siteLabel = new Label("Sites :");
        siteLabel.setMinWidth(100);
        siteComboBox = new ComboBox<>(siteList);
        siteComboBox.setPromptText("All sites");
        siteComboBox.setPrefWidth(300);
        // Personnaliser l'affichage des sites
        siteComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<Sites>() {
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
        siteComboBox.setButtonCell(new javafx.scene.control.ListCell<Sites>() {
            @Override
            protected void updateItem(Sites site, boolean empty) {
                super.updateItem(site, empty);
                if (empty || site == null) {
                    setText("Tous les sites");
                } else {
                    setText(site.getCity());
                }
            }
        });
        siteBox.getChildren().addAll(siteLabel, siteComboBox);

        // ComboBox pour le service
        HBox serviceBox = new HBox(10);
        Label serviceLabel = new Label("Services :");
        serviceLabel.setMinWidth(100);
        serviceComboBox = new ComboBox<>(serviceList);
        serviceComboBox.setPromptText("All services");
        serviceComboBox.setPrefWidth(300);
        // Personnaliser l'affichage des services
        serviceComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<Services>() {
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
        serviceComboBox.setButtonCell(new javafx.scene.control.ListCell<Services>() {
            @Override
            protected void updateItem(Services service, boolean empty) {
                super.updateItem(service, empty);
                if (empty || service == null) {
                    setText("All services");
                } else {
                    setText(service.getName());
                }
            }
        });
        serviceBox.getChildren().addAll(serviceLabel, serviceComboBox);

        // Boutons
        HBox buttonBox = new HBox(10);
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #002affff; -fx-text-fill: white; -fx-font-weight: bold;");
        searchButton.setOnAction(e -> performSearch());

        Button clearButton = new Button("Reset");
        clearButton.setStyle("-fx-background-color: #000000ff; -fx-text-fill: white;");
        clearButton.setOnAction(e -> clearSearch());

        buttonBox.getChildren().addAll(searchButton, clearButton);

        searchBox.getChildren().addAll(searchLabel, nameBox, siteBox, serviceBox, buttonBox);
        return searchBox;
    }

    private TableView<Employee> createEmployeeTable() {
        TableView<Employee> table = new TableView<>();
        table.setPrefHeight(850);
        table.setStyle("-fx-background-color: white;");

        // Colonnes
        // Colonne ID
        TableColumn<Employee, Integer> idCol = new TableColumn<>("N°");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idCol.setPrefWidth(60);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Employee, String> firstNameCol = new TableColumn<>("FirstName");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        firstNameCol.setPrefWidth(150);

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("LastName");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        lastNameCol.setPrefWidth(150);

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
        serviceCol.setPrefWidth(150);

        TableColumn<Employee, String> siteCol = new TableColumn<>("Site");
        siteCol.setCellValueFactory(cellData -> {
            Sites site = cellData.getValue().getSite();
            return new javafx.beans.property.SimpleStringProperty(site != null ? site.getCity() : "N/A");
        });
        siteCol.setPrefWidth(150);

        table.getColumns().add(idCol);
        table.getColumns().add(firstNameCol);
        table.getColumns().add(lastNameCol);
        table.getColumns().add(phoneCol);
        table.getColumns().add(cellCol);
        table.getColumns().add(emailCol);
        table.getColumns().add(serviceCol);
        table.getColumns().add(siteCol);
        table.setItems(employeeList);

        // Sélection d'une ligne pour afficher les détails
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayEmployeeDetails(newSelection);
            }
        });

        return table;
    }

    private VBox createDetailsBox() {
        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(15));
        detailsBox.setStyle("-fx-background-color: white; -fx-border-radius: 50; -fx-background-radius: 5;");

        Label detailsLabel = new Label("Details");
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(1);
        detailsGrid.setVgap(1);
        detailsGrid.setPadding(new Insets(1));

        // Labels pour les détails (seront mis à jour lors de la sélection)
        nameDetail = new Label();
        phoneDetail = new Label();
        cellDetail = new Label();
        emailDetail = new Label();
        serviceDetail = new Label();
        siteDetail = new Label();

        detailsGrid.add(new Label("Complete name : "), 0, 0);
        detailsGrid.add(nameDetail, 1, 0);
        detailsGrid.add(new Label("Phone : "), 0, 1);
        detailsGrid.add(phoneDetail, 1, 1);
        detailsGrid.add(new Label("Mobile : "), 0, 2);
        detailsGrid.add(cellDetail, 1, 2);
        detailsGrid.add(new Label("Email : "), 0, 3);
        detailsGrid.add(emailDetail, 1, 3);
        detailsGrid.add(new Label("Service : "), 0, 4);
        detailsGrid.add(serviceDetail, 1, 4);
        detailsGrid.add(new Label("Site : "), 0, 5);
        detailsGrid.add(siteDetail, 1, 5);

        // Bouton générer PDF
        Button pdfButton = new Button("Generate PDF");
        pdfButton.setStyle("-fx-background-color: #000000ff; -fx-text-fill: white; -fx-font-weight: bold;");
        pdfButton.setOnAction(e -> {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                generatePDFForEmployee(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a employee to generate the PDF file.");
                alert.showAndWait();
            }
        });

        detailsBox.getChildren().addAll(detailsLabel, detailsGrid, pdfButton);
        return detailsBox;
    }

    private void displayEmployeeDetails(Employee employee) {

        nameDetail.setText(employee.getFirstName() + " " + employee.getLastName());
        phoneDetail.setText(employee.getPhone() != null ? employee.getPhone() : "N/A");
        cellDetail.setText(employee.getCell() != null ? employee.getCell() : "N/A");
        emailDetail.setText(employee.getEmail());
        serviceDetail.setText(employee.getService() != null ? employee.getService().getName() : "N/A");
        siteDetail.setText(employee.getSite() != null ? employee.getSite().getCity() : "N/A");
    }

    private void generatePDFForEmployee(Employee employee) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save the PDF file.");
        fileChooser.setInitialFileName(employee.getFirstName() + "_" + employee.getLastName() + "_fiche.pdf");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        java.io.File file = fileChooser.showSaveDialog(employeeTable.getScene().getWindow());
        if (file != null) {
            try {
                PDFService.generateEmployeePDF(employee, file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("PDF file generated successfully: " + file.getAbsolutePath());
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error generating PDF : " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void performSearch() {
        try {
        String namePattern = nameSearchField.getText();
        Sites selectedSite = siteComboBox.getValue();
        Services selectedService = serviceComboBox.getValue();

        java.util.List<Employee> results = EmployeeService.searchEmployees(namePattern, selectedSite, selectedService);
        employeeList.clear();
        employeeList.addAll(results);
            
            if (results.isEmpty()) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Search");
                info.setHeaderText(null);
                info.setContentText("No results found for the selected criteria.");
                info.showAndWait();
            }
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Error during search: " + e.getMessage());
            error.showAndWait();
        }
    }

    private void clearSearch() {
        nameSearchField.clear();
        siteComboBox.setValue(null);
        serviceComboBox.setValue(null);
        refreshEmployeeList();
    }

    private void refreshEmployeeList() {
        employeeList.clear();
        employeeList.addAll(EmployeeService.getAllEmployees());
    }

    public static void main(String[] args) {
        launch(args);
    }
}