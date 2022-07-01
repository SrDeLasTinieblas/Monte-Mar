package com.monte.mar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Carrito implements Parcelable {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("PrecioCosto")
    @Expose
    private float precioCosto;

    /** Add */
    @SerializedName("cantidad")
    @Expose
    private int cantidad = 1;

    @SerializedName("PrecioTotal")
    @Expose
    private float precioTotal;


    @SerializedName("Precio")
    @Expose
    private float precioUnitario;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("Titulo")
    @Expose
    private String titulo;
    @SerializedName("discount")
    @Expose
    private String banner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(Integer precioCosto) {
        this.precioCosto = precioCosto;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Integer precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioTotal() {
        return getCantidad()*getPrecioUnitario();
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.image);
        dest.writeFloat(this.precioCosto);
        dest.writeInt(this.cantidad);
        dest.writeFloat(this.precioTotal);
        dest.writeFloat(this.precioUnitario);
        dest.writeString(this.descripcion);
        dest.writeString(this.titulo);
        dest.writeString(this.banner);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Integer) source.readValue(Integer.class.getClassLoader());
        this.image = source.readString();
        this.precioCosto = source.readFloat();
        this.cantidad = source.readInt();
        this.precioTotal = source.readFloat();
        this.precioUnitario = source.readFloat();
        this.descripcion = source.readString();
        this.titulo = source.readString();
        this.banner = source.readString();
    }

    public Carrito() {
    }

    protected Carrito(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
        this.precioCosto = in.readFloat();
        this.cantidad = in.readInt();
        this.precioTotal = in.readFloat();
        this.precioUnitario = in.readFloat();
        this.descripcion = in.readString();
        this.titulo = in.readString();
        this.banner = in.readString();
    }

    public static final Creator<Carrito> CREATOR = new Creator<Carrito>() {
        @Override
        public Carrito createFromParcel(Parcel source) {
            return new Carrito(source);
        }

        @Override
        public Carrito[] newArray(int size) {
            return new Carrito[size];
        }
    };
}