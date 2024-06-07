package com.fujitsu.delivery_fee_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private int wmoCode;

    // Constructors
    public City() {
        // Default constructor
    }

    public City(String city, int wmoCode) {
        this.city = city;
        this.wmoCode = wmoCode;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getWmoCode() {
        return wmoCode;
    }

    public void setWmoCode(int wmoCode) {
        this.wmoCode = wmoCode;
    }
}
