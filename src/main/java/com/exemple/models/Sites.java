package com.exemple.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Sites")
public class Sites {
@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false, length=60)
    private String City;

    // Constructeur par défaut (requis pour Hibernate)
    public Sites() {
    }
    
    // Constructeur avec paramètres
    public Sites(Integer id, String city) {
        this.ID = id;
        this.City = city;
    }

    // Constructeur avec ville uniquement
    public Sites(String city) {
        this.City = city;
    }

    // Getters et Setters
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }
}
