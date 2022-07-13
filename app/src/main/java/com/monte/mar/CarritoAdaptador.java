package com.monte.mar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monte.mar.model.Carrito;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import monte.montemar.R;

public class CarritoAdaptador extends BaseAdapter {
    private final Context context;
    private final List<Carrito> carritoList;

    // creamos un costructos para despues llamar al adaptador con el costructor
    public CarritoAdaptador(Context context, List<Carrito> carritoList)  {
        this.context=context;
        this.carritoList = carritoList;
    }

    @Override
    public int getCount() {
        return carritoList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private List<Carrito> carritoListCompra;
    List<String> productos = new ArrayList<>();
    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override      //position - convertView -  parent
    public View getView(int i, View v, ViewGroup viewGroup) {
        carritoListCompra = new ArrayList<>();

        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.carrito_elements, viewGroup, false);
        }

        String TituloActual = carritoList.get(i).getTitulo();
        String DescripcionActual = carritoList.get(i).getDescripcion();
        String Cantidad = String.valueOf(carritoList.get(i).getCantidad());
        String ImagenActual = carritoList.get(i).getImage();

        float PrecioTotal = carritoList.get(i).getPrecioTotal();

        TextView textTitulo = v.findViewById(R.id.textTitulo);
        TextView textDescipcion = v.findViewById(R.id.textDescripcion);
        TextView textCantidad = v.findViewById(R.id.textCantidad);
        TextView textPrecio = v.findViewById(R.id.textPrecio);

        textTitulo.setText(TituloActual);
        ImageView imagen = v.findViewById(R.id.iconImageView);
        textDescipcion.setText(DescripcionActual);
        textCantidad.setText("c: "+Cantidad);
        textPrecio.setText("S/" + PrecioTotal);
        Glide.with(v)
                .load(ImagenActual)
                .placeholder(R.drawable.logo)
                .into(imagen);

        for(Carrito carrito : carritoListCompra){
            productos.add(carrito.getTitulo());
        }

        return v;
    }
}
