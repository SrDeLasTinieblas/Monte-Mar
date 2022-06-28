package com.monte.mar;

public class users {
    private String Nombre;
    private String Apellido;
    private String Telefono;
    private String Direccion;
    private String Email;
    private String Edad;
    private String EstadoProvincia;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEdad() {
        return Edad;
    }

    public void setEdad(String Edad) {
        this.Edad = Edad;
    }

    public String getEstadoProvincia() {
        return EstadoProvincia;
    }

    public void setEstadoProvincia(String estadoProvincia) {
        EstadoProvincia = estadoProvincia;
    }

    public users() {
    }


}
