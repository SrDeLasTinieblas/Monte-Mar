package com.monte.mar.model;

import android.content.Context;
import android.widget.Toast;

public class Cantidad {

    public void cantSumando(Context context/*, float precio, float cantidad*/){
        CantProductos cantProductos = new CantProductos();
        //cantProductos.cantProducts(precio, cantidad);
        //cantidad.setText(String.valueOf(++valor));
        //Toast.makeText(context, "Suma mas 1" + count , Toast.LENGTH_SHORT).show();

    }

    public void cantRestando(Context context, int valor){
        Toast.makeText(context, "Resta menos 1", Toast.LENGTH_SHORT).show();
        /*try {
            //titulo.setText("aaaaa");
            cantidad.setText(String.valueOf(--valor));
            System.out.println(valor);
        } catch (Exception e) {
            System.err.println(e);
        }*/
    }

}

