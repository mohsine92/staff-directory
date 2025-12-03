package com.exemple.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ADMIN")


public class Admin extends User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false,length=60)
    private String FirstName;

    @Column(nullable = false,length=60)
    private String LastName;

    @Column(nullable = false,length=60)
    private Integer Phone;

    @Column(nullable = false,length=60)
    private Integer Cell;

    @Column(nullable = false,length=60)
    private String Email;

    @Column(nullable = false,length=60)
    private Integer IdService;

    @Column(nullable = false,length=60)
    private String IdSite;

    public Admin(Integer ID, String FirstName, String LastName, Integer Phone, Integer Cell, String Email,
            String PASSWORD_HASH, String firstName2, String lastName2, Integer phone2, Integer cell2,
            String email2, Integer idService, String idSite) {
        super(ID, FirstName, LastName, Phone, Cell, Email, PASSWORD_HASH, "ADMIN");    
    }

    protected Admin() {

    }

}
