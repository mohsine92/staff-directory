package com.exemple.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="EMPLOYEE")
public class Employee extends User {
    
    @ManyToOne
    @JoinColumn(name = "IdService", nullable = false)
    private Services service;

    @ManyToOne
    @JoinColumn(name = "IdSite", nullable = false)
    private Sites site;

    // Constructeur par défaut (requis pour Hibernate)
    protected Employee() {
        super();
    }

    // Constructeur avec tous les paramètres
    public Employee(String firstName, String lastName, String phone, String cell, String email,
            String passwordHash, Services service, Sites site) {
        super(null, firstName, lastName, phone, cell, email, passwordHash, "EMPLOYEE");
        this.service = service;
        this.site = site;
    }

    // Getters et Setters
    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public Sites getSite() {
        return site;
    }

    public void setSite(Sites site) {
        this.site = site;
    }
}
