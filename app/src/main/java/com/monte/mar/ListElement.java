package com.monte.mar;

public class ListElement {
    // CLASE SIN USO
    public String Titulo;
    public String Precio;
    public String Descripcion;

    //public String status;
    public ListElement() {
    }

    public ListElement(String titulo, String descripcion, String precio/*, String imagenes*/) {
        Titulo = titulo;
        Precio = precio;
        Descripcion = descripcion;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

}

