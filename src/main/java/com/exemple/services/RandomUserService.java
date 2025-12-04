package com.exemple.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.exemple.models.Employee;
import com.exemple.models.Services;
import com.exemple.models.Sites;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RandomUserService {

    private static final String RANDOM_USER_API = "https://randomuser.me/api/?results=100&nat=fr";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Employee> fetchAndCreateEmployees() throws IOException {
        List<Employee> createdEmployees = new ArrayList<>();
        
        // Récupérer les sites et services existants
        List<Sites> sites = SiteService.getAllSites();
        List<Services> services = ServiceService.getAllServices();
        
        if (sites.isEmpty() || services.isEmpty()) {
            throw new IllegalStateException("Veuillez d'abord créer au moins un site et un service dans l'interface administrateur.");
        }

        // Appel à l'API
        Request request = new Request.Builder()
            .url(RANDOM_USER_API)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur lors de l'appel à l'API: " + response);
            }

            String responseBody = response.body().string();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode results = rootNode.get("results");

            if (results != null && results.isArray()) {
                for (JsonNode person : results) {
                    try {
                        // Extraire les données
                        JsonNode name = person.get("name");
                        String firstName = name.get("first").asText();
                        String lastName = name.get("last").asText();
                        JsonNode phone = person.get("phone");
                        String phoneNumber = phone != null ? phone.asText().replaceAll("[^0-9]", "") : "";
                        if (phoneNumber.length() > 10) {
                            phoneNumber = phoneNumber.substring(0, 10);
                        }
                        if (phoneNumber.isEmpty()) {
                            phoneNumber = "0123456789";
                        }
                        
                        JsonNode cell = person.get("cell");
                        String cellNumber = cell != null ? cell.asText().replaceAll("[^0-9]", "") : "";
                        if (cellNumber.length() > 10) {
                            cellNumber = cellNumber.substring(0, 10);
                        }
                        if (cellNumber.isEmpty()) {
                            cellNumber = "0612345678";
                        }
                        
                        String email = person.get("email").asText();
                        
                        // Capitaliser les noms
                        firstName = capitalize(firstName);
                        lastName = capitalize(lastName);
                        
                        // Sélectionner aléatoirement un site et un service
                        Sites randomSite = sites.get((int) (Math.random() * sites.size()));
                        Services randomService = services.get((int) (Math.random() * services.size()));
                        
                        // Créer l'employé
                        Employee employee = new Employee(
                            firstName, lastName, phoneNumber, cellNumber, email, 
                            "", randomService, randomSite
                        );
                        EmployeeService.createEmployee(
                            firstName, lastName, phoneNumber, cellNumber, email,
                            randomService, randomSite
                        );
                        
                        createdEmployees.add(employee);
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la création d'un employé: " + e.getMessage());
                    }
                }
            }
        }

        return createdEmployees;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

