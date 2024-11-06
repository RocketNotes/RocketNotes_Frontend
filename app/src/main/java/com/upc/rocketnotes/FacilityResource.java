package com.upc.rocketnotes;

public class FacilityResource {
    private Long id;
    private String name;
    private String period;
    private String creation;
    private int budget;
    private int status;

    // Constructor con parámetros
    public FacilityResource(Long id, String name, String period, String creation, int budget, int status) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.creation = creation;
        this.budget = budget;
        this.status = status;
    }

    // Constructor por defecto (para Retrofit)
    public FacilityResource() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getCreation() { return creation; }
    public void setCreation(String creation) { this.creation = creation; }

    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}