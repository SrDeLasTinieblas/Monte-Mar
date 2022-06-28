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
import com.monte.mar.model.ShoppingCart;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import monte.montemar.R;

public class CarritoAdaptador extends BaseAdapter {
    private final Context context;
    private final List<ShoppingCart> shoppingCartList;

    // creamos un costructos para despues llamar al adaptador con el costructor
    public CarritoAdaptador(Context context, List<ShoppingCart> shoppingCartList)  {
        this.context=context;
        this.shoppingCartList = shoppingCartList;
    }

    @Override
    public int getCount() {
        return shoppingCartList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private List<ShoppingCart> shoppingCartListCompra;
    List<String> productos = new ArrayList<>();
    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override      //position - convertView -  parent
    public View getView(int i, View v, ViewGroup viewGroup) {
        shoppingCartListCompra = new ArrayList<>();

        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.carrito_elements, viewGroup, false);
        }

        String TituloActual = shoppingCartList.get(i).getTitulo();
        String DescripcionActual = shoppingCartList.get(i).getDescripcion();
        String PrecioActual = String.valueOf(shoppingCartList.get(i).getPrecioUnitario());
        String Cantidad = String.valueOf(shoppingCartList.get(i).getCantidad());
        String ImagenActual = shoppingCartList.get(i).getImage();

        float PrecioTotal = shoppingCartList.get(i).getPrecioTotal();

        TextView textTitulo = v.findViewById(R.id.textTitulo);
        TextView textDescipcion = v.findViewById(R.id.textDescripcion);
        TextView textCantidad = v.findViewById(R.id.textCantidad);
        TextView textPrecio = v.findViewById(R.id.textPrecio);
        //int precioFinal = PrecioActual*Cantidad;

        textTitulo.setText(TituloActual);
        ImageView imagen = v.findViewById(R.id.iconImageView);
        textDescipcion.setText(DescripcionActual);
        textCantidad.setText("c: "+Cantidad);
        textPrecio.setText("S/" + PrecioTotal);
        Glide.with(v)
                .load(ImagenActual)
                .placeholder(R.drawable.logo)
                .into(imagen);

        for(ShoppingCart shoppingCart : shoppingCartListCompra){
            //System.out.println(shoppingCart.getTitulo());
            productos.add(shoppingCart.getTitulo());
            //float e = preferencias.getFloat("precio" , 0.0f);
            //System.out.println("holaaaa");
        }
        //for(ShoppingCart carrito : shoppingCartListCompra){

            /*if (Objects.equals(carrito.getTitulo(), "Saca Grasa")){
                //System.out.println("==> "+carrito.getTitulo());
                System.out.println("Es igual a titulo ==> "+carrito.getTitulo());
                textTitulo.setText(TituloActual);
            }else{
                System.out.println("No es igual a titulo ==> "+carrito.getTitulo());
            }*/
        //}

        return v;
    }
}
