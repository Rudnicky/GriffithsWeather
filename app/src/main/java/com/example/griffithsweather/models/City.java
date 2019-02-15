package com.example.griffithsweather.models;

public class City {
    private float id;
    private String name;
    Coord CoordObject;
    private String country;
    private float population;


    // Getter Methods

    public float getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return CoordObject;
    }

    public String getCountry() {
        return country;
    }

    public float getPopulation() {
        return population;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoord(Coord coordObject) {
        this.CoordObject = coordObject;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPopulation(float population) {
        this.population = population;
    }
}
