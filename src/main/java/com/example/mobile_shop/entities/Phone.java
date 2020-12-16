package com.example.mobile_shop.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phoneName;
    private String brand;
    private Double price;

    public Phone() {
    }

    public Phone(Long id, String phoneName, String brand, double price) {
        this.id = id;
        this.phoneName = phoneName;
        this.brand = brand;
        this.price = price;
    }

    public Phone(String phoneName, String brand, double price) {
        this.phoneName = phoneName;
        this.brand = brand;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return id != null && Double.compare(phone.price, price) == 0 &&
                Objects.equals(id, phone.id) &&
                Objects.equals(phoneName, phone.phoneName) &&
                Objects.equals(brand, phone.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phoneName, brand, price);
    }

}
