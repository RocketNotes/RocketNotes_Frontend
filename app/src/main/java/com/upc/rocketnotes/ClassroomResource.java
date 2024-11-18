package com.upc.rocketnotes;

public class ClassroomResource {
    private int id;
    private String name;
    private String section;
    private int capacity;

    public ClassroomResource(int id, String name, String section, int capacity) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.capacity = capacity;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public int getCapacity() {
        return capacity;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
