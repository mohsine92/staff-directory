package com.exemple.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public abstract class User {
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

    @Column(nullable = false, length=255)
    private String PASSWORD_HASH;

    @Column(nullable = false, length=15)
    protected String ROLE;    
    
    
    
    // Constructor
    protected User(Integer ID, String FirstName, String LastName, Integer Phone, Integer Cell, String Email, String PASSWORD_HASH, String ROLE) {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Phone = Phone;
        this.Cell = Cell;
        this.Email = Email;
        this.PASSWORD_HASH = PASSWORD_HASH;
        this.ROLE = ROLE;


    }

    protected User() {

    }


    // Getters/setter

    public Integer getID() {
        return ID;
    }

    public void setID(Integer iD) {
        ID = iD;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public Integer getPhone() {
        return Phone;
    }

    public void setPhone(Integer phone) {
        Phone = phone;
    }

    public Integer getCell() {
        return Cell;
    }

    public void setCell(Integer cell) {
        Cell = cell;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPASSWORD_HASH() {
        return PASSWORD_HASH;
    }

    public void setPASSWORD_HASH(String pASSWORD_HASH) {
        PASSWORD_HASH = pASSWORD_HASH;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String rOLE) {
        ROLE = rOLE;
    }

    
}   
