package com.exemple;

import com.exemple.util.DataInitializer;
import com.exemple.view.VisitorView;
import javafx.application.Application;

/**
 * Point d'entrée principal de l'application
 */
public class Main {
    public static void main(String[] args) {
        // Initialiser les données par défaut (admin, sites, services, salariés depuis API)
        // dans un thread séparé pour éviter les conflits avec le thread JavaFX
        new Thread(() -> {
            try {
                // Initialiser Hibernate si nécessaire
                HibernateUtils.getSessionFactory();
                
                // Initialiser les données par défaut
                DataInitializer.initializeDefaultData();
            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation des données: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
        
        // Lancer l'application JavaFX directement
        Application.launch(VisitorView.class, args);
    }
}