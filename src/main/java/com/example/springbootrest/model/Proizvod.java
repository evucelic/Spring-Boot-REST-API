package com.example.springbootrest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Proizvod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private String name;
    private Double cijena;
    private String opis;
    private String category;

    public Proizvod(){}
    public Proizvod(Long id, String imageUrl, String name, Double cijena, String opis, String category) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.cijena = cijena;
        this.opis = opis;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCijena() {
        return cijena;
    }

    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }

    public String getOpis() {
        return opis.length() < 100 ? opis : opis.substring(0,100);
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
