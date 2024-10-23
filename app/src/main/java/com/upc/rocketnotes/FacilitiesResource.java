package com.upc.rocketnotes;

public class FacilitiesResource {
    private Long id;
    private String name;
    private Integer quantity;
    private Integer budget;
    private String creation;
    private String period;
    private Integer status;

    public FacilitiesResource(Long id, String name, Integer quantity, Integer budget, String creation, String period, Integer status) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.budget = budget;
        this.creation = creation;
        this.period = period;
        this.status = status;
    }

    public FacilitiesResource(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
