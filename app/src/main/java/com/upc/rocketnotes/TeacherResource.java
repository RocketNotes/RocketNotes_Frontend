package com.upc.rocketnotes;

public class TeacherResource {
    private Long id;
    private String firstName;
    private String paternalLastName;
    private String maternalLastName;
    private String dni;
    private String phone;
    private String email;

    // Constructor con parámetros
    public TeacherResource(Long id, String firstName, String paternalLastName, String maternalLastName, String dni, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.paternalLastName = paternalLastName;
        this.maternalLastName = maternalLastName;
        this.dni = dni;
        this.phone = phone;
        this.email = email;
    }

    // Constructor por defecto (para Retrofit)
    public TeacherResource() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
