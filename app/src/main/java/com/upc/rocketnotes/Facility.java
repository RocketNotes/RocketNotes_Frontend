package com.upc.rocketnotes;

public class Facility {
    private String name;
    private String period;
    private String creation;
    private int budget;
    private int status;

    // Constructor
    public Facility(String name, String period, String creation, int budget, int status) {
        this.name = name;
        this.period = period;
        this.creation = creation;
        this.budget = budget;
        this.status = status;
    }

    // Getters y Setters
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

    // Método para copiar un Facility con valores actualizados
    public Facility copy(String name, String period, String creation, int budget, int status) {
        return new Facility(name, period, creation, budget, status);
    }
}