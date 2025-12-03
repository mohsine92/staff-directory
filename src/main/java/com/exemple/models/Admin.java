package com.exemple.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="ADMIN")
public class Admin extends User {
    
    // Constructeur par défaut (requis pour Hibernate)
    protected Admin() {
        super();
    }

    // Constructeur avec tous les paramètres
    public Admin(String firstName, String lastName, String phone, String cell, String email,
            String passwordHash) {
        super(null, firstName, lastName, phone, cell, email, passwordHash, "ADMIN");
    }
}
