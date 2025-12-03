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

    @Column(nullable = false,length=60)
    private String City;

    // Getter/Setter
    
    
    // Constructor
    public Sites(Integer id, String city) {
        this.ID = id;
        this.City = city;
    }


    

}
