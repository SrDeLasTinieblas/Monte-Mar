package com.monte.mar.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.monte.mar.CarritoAdaptador;
import com.monte.mar.constants.Constants;
import com.monte.mar.model.ShoppingCart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import monte.montemar.R;

public class Comprando_fragment extends Fragment {

    // CLASE SIN USO
    public Comprando_fragment() {
        // Required empty public constructor
    }

    public static Comprando_fragment newInstance(String param1, String param2) {
        Comprando_fragment fragment = new Comprando_fragment();
        return fragment;
    }
    private GridView tablaCompras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.comprando_fragment, container, false);
        //this.tablaCompras = v.findViewById(R.id.tablaCompras);
        System.out.println("ShoppingCart de compras ");
        CargarDatosCarrito();
        return v;
    }

    private List<ShoppingCart> shoppingCartListCompra;
    private CarritoAdaptador adapter;
    private void CargarDatosCarrito(){
        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        if(preferences.contains(Constants.SHARED_PREFERENCES_NAME)){
            String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME,"No se encontraron datos");
            Type typeList = new TypeToken<List<ShoppingCart>>(){}.getType();

            shoppingCartListCompra = new ArrayList<>();
            shoppingCartListCompra.addAll(new Gson().fromJson(datos,typeList));
            adapter = new CarritoAdaptador(requireActivity(), shoppingCartListCompra);
            //                      Context context,List<ShoppingCart> carritoList
            //                      Context context,List<ShoppingCart> carritoList
            tablaCompras.setAdapter(adapter);
        }
    }

}