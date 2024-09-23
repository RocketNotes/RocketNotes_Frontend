package com.upc.rocketnotes;

public class Classroom {
    private String id;
    private String name;
    private String section;
    private String capacity;

    //constructor
    public Classroom(String id, String name, String section, String capacity) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.capacity = capacity;
    }

    //Getters y setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String capacity) {
        this.section = section;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    // Método para copiar una clase con valores actualizados
    public Classroom copy(String id, String name, String section, String capacity) {
        return new Classroom(id, name, section, capacity);
    }
}
