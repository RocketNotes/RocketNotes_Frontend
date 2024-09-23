package com.upc.rocketnotes;

import java.util.List;

public class StudentResource {

    private Long id;
    private String firstName;
    private String paternalLastName;
    private String maternalLastName;
    private String dni;
    private List<Integer> classrooms;  // Cambiado a List<Integer>

    // Constructor con parámetros
    public StudentResource(Long id, String firstName, String paternalLastName, String maternalLastName, String dni, List<Integer> classrooms) {
        this.id = id;
        this.firstName = firstName;
        this.paternalLastName = paternalLastName;
        this.maternalLastName = maternalLastName;
        this.dni = dni;
        this.classrooms = classrooms;
    }

    // Constructor por defecto (necesario para la deserialización de Retrofit)
    public StudentResource() {
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPaternalLastName() {
        return paternalLastName;
    }

    public void setPaternalLastName(String paternalLastName) {
        this.paternalLastName = paternalLastName;
    }

    public String getMaternalLastName() {
        return maternalLastName;
    }

    public void setMaternalLastName(String maternalLastName) {
        this.maternalLastName = maternalLastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Integer> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Integer> classrooms) {
        this.classrooms = classrooms;
    }
}