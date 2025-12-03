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

    @Column(nullable = false,length=60)
    private String Name;



   // Getter/Setter
    
   
   // Constructor
   public Services(Integer id, String name) {
        this.ID = id;
        this.Name = name;
     }
}
