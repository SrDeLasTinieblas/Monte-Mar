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
import com.monte.mar.model.Trolley;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import monte.montemar.R;

public class Adaptador extends BaseAdapter {

    Context context;
    List<Trolley> trolleyList;
    List<Trolley> trolleyListOriginal;

    public Adaptador(Context context,List<Trolley> trolleyList)  {
        this.context=context;
        this.trolleyList = trolleyList;
        this.trolleyListOriginal = new ArrayList<>();
        this.trolleyListOriginal.addAll(this.trolleyList);
    }

    @Override
    public int getCount() {
        return trolleyList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override       //position - convertView -  parent
    public View getView(int i, View v, ViewGroup viewGroup) {
        // Aqui decimos si vista es igual a null(nada) entonces no se va inflar
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_elements, viewGroup, false);
        }

        // Aqui obtenemos traemos lo que esta en getTitulo, getDescripcion, getPrecio y imagen
        // para despues guardarlo en esas variables
        String TituloActual = trolleyList.get(i).getTitulo();
        String DescripcionActual = trolleyList.get(i).getDescripcion();
        String PrecioActual = String.valueOf(trolleyList.get(i).getPrecioUnitario());
        String ImagenActual = trolleyList.get(i).getImage();

        // Aqui difinimos lo que esta en nuestro layout
        TextView textTitulo = v.findViewById(R.id.textTitulo);
        TextView textDescipcion = v.findViewById(R.id.textDescripcion);
        TextView textPrecio = v.findViewById(R.id.textPrecio);
        ImageView imagen = v.findViewById(R.id.iconImageView);

        // Y aqui mostramos todo los datos
        textTitulo.setText(TituloActual);
        textDescipcion.setText(DescripcionActual);
        textPrecio.setText("S/" + PrecioActual);
        //searchItem("Lavavajillas");
        Glide.with(v)
                .load(ImagenActual)
                .placeholder(R.drawable.logo)
                .into(imagen);



        return v;
    }


    /*private void searchItem(String titulo){
        for(Trolley carrito : carritoListCompra){
            if (Objects.equals(carrito.getTitulo(), titulo)){
                System.out.println("Es igual a titulo ==> "+carrito.getTitulo());
                //tv.setText("TituloActual");
            }else{
                System.out.println("No es igual a titulo ==> "+carrito.getTitulo());
            }
        }
    }*/

    public void filtrado(String textBuscar){
        int longitud = textBuscar.length();

        //Log.d("adaptador", "Longitud " + longitud);
        //Log.d("adaptador orginal", new Gson().toJson(this.trolleyListOriginal));
        trolleyList.clear();
        trolleyList.addAll(trolleyListOriginal);

        if(longitud > 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Trolley> collecion = trolleyList.stream().
                        filter(i -> i.getTitulo().toLowerCase().contains(textBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                trolleyList.clear();
                trolleyList.addAll(collecion);

            }else {
                for (Trolley c: trolleyList) {
                    if(c.getTitulo().toLowerCase().contains(textBuscar.toLowerCase())){
                        trolleyList.add(c);
                        System.out.println("Vieja version de android");
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}























