package com.exemple.util;

import com.exemple.services.AdminService;
import com.exemple.services.RandomUserService;
import com.exemple.services.ServiceService;
import com.exemple.services.SiteService;

/**
 * Utilitaire pour initialiser les données par défaut (admin, sites, services, salariés depuis API)
 */
public class DataInitializer {
    
    /**
     * Initializes default application data:
     * - A default administrator
     * - Sites and services
     * - Employees imported from RandomUser API
     */
    public static void initializeDefaultData() {
        try {
            // Vérifier si des données existent déjà
            if (!SiteService.getAllSites().isEmpty()) {
                System.out.println("Data already initialized. Application is ready.");
                return;
            }

            System.out.println("Initializing default data...");

            // Créer des sites
            SiteService.createSite("Paris, France");
            SiteService.createSite("Lyon, France");
            SiteService.createSite("Marseille, France");
            SiteService.createSite("Toulouse, France");
            SiteService.createSite("Londres, UK");
            SiteService.createSite("San Francisco, USA");
            SiteService.createSite("Hong Kong, Chine");

            System.out.println("✓ Sites created");
            
            // Créer des services
            ServiceService.createService("Comptabilité");
            ServiceService.createService("Production");
            ServiceService.createService("Accueil");
            ServiceService.createService("Ressources Humaines");
            ServiceService.createService("Informatique");
            ServiceService.createService("Marketing");
            ServiceService.createService("Direction Géneral");
            ServiceService.createService("R&D");

            System.out.println("✓ Services created");
            
            // Vérifier que les sites et services sont créés
            if (SiteService.getAllSites().isEmpty() || ServiceService.getAllServices().isEmpty()) {
                System.err.println("Error: Unable to create sites or services.");
                return;
            }
            
            // Créer un administrateur par défaut
            // Email: admin@example.com, Mot de passe: admin123
            AdminService.createAdmin("Admin", "System", "0123456789", "0612345678", 
                "admin@example.com", "admin123");
            System.out.println("✓ Default administrator created");
            System.out.println("  Email: admin@example.com");
            System.out.println("  Password: admin123");
            
            // Importer les employés depuis l'API RandomUser (100 personnes)
            try {
                java.util.List<com.exemple.models.Employee> importedEmployees = RandomUserService.fetchAndCreateEmployees();
                System.out.println("✓ " + importedEmployees.size() + " employee(s) imported from RandomUser API");
            } catch (Exception apiError) {
                System.err.println("⚠ Error importing from RandomUser API: " + apiError.getMessage());
                System.err.println("  You can import employees manually or via the administrator interface.");
            }
            
            System.out.println("✓ Initialization completed successfully!");
            System.out.println("  Application is ready to use.");
            
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Old method for compatibility (deprecated)
     * @deprecated Use initializeDefaultData() instead
     */
    @Deprecated
    public static void initializeTestData() {
        initializeDefaultData();
    }
}

