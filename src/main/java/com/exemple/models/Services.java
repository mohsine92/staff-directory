package com.exemple.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Services")
public class Services {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false, length=60)
    private String Name;

    // Constructeur par défaut (requis pour Hibernate)
    public Services() {
    }

    // Constructeur avec paramètres
   public Services(Integer id, String name) {
        this.ID = id;
        this.Name = name;
    }

    // Constructeur avec nom uniquement
    public Services(String name) {
        this.Name = name;
    }

    // Getters et Setters
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
     }
}
