package com.monte.mar.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingCart(
        @SerializedName("Id") val id: Int,
        @SerializedName("Image") @Expose val image: String,
        @SerializedName("PrecioCosto") @Expose val precioCosto: Int,
        @SerializedName("cantidad") @Expose var cantidad: Int = 1,
        @SerializedName("Precio") @Expose val precioUnitario: Float,
        @SerializedName("PrecioTotal") @Expose var precioTotalX: Float = 0f,
        @SerializedName("Descripcion") @Expose val descripcion: String? = null,
        @SerializedName("Titulo") @Expose val titulo: String? = null,
        @SerializedName("banner") @Expose val banner: String? = null
) : Parcelable {

        fun getPrecioTotal(): Float {
        return cantidad * precioUnitario
        }
}