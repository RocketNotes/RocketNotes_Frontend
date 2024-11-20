package com.upc.rocketnotes;


public class Facility {
    private Long id;
    private String period = ""; // Valor predeterminado
    private Integer budget = 0; // Valor predeterminado
    private String creation = ""; // Valor predeterminado
    private String name = ""; // Valor predeterminado

    // Constructor
    public Facility(Long id, String period, Integer budget, String creation, String name) {
        this.id = id;
        this.period = period;
        this.budget = budget;
        this.creation = creation;
        this.name = name;
    }

    // Constructor vacío (necesario para frameworks como Retrofit)
    public Facility() {
    }


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
