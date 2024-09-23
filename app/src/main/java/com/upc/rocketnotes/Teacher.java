package com.upc.rocketnotes;

public class Teacher {
    private String name;
    private String surname;
    private String dni;
    private String birthdate;
    private String assignedClass;

    // Constructor
    public Teacher(String name, String surname, String dni, String birthdate, String assignedClass) {
        this.name = name;
        this.surname = surname;
        this.dni = dni;
        this.birthdate = birthdate;
        this.assignedClass = assignedClass;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAssignedClass() {
        return assignedClass;
    }

    public void setAssignedClass(String assignedClass) {
        this.assignedClass = assignedClass;
    }

    // Método para copiar un profesor con valores actualizados
    public Teacher copy(String name, String surname, String dni, String birthdate, String assignedClass) {
        return new Teacher(name, surname, dni, birthdate, assignedClass);
    }
}
