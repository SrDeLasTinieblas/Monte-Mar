package com.monte.mar.model;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monte.mar.Adaptador;
import com.monte.mar.constants.APIs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VolleyData {

    GridView tabla;
    Adaptador adaptador;
    List<String> titulo;
    List<String> descripcion;
    List<String> precio;
    List<String> images;
    List<Carrito> carritoList = new ArrayList<>();
    RequestQueue requestQueue;

    public void getData(Context context) {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                APIs.UrlProductos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Type typeList = new TypeToken<List<Carrito>>() {}.getType();
                            List<Carrito> carritoListResponse = new Gson().fromJson(response, typeList);

                            carritoList.addAll(carritoListResponse);
                            JSONArray jsonArray = new JSONArray(response);
                            List<Adaptador> lista = new ArrayList<>(); // definiendo la lista de elementos
                            titulo = new ArrayList<String>();
                            precio = new ArrayList<String>();
                            descripcion = new ArrayList<String>();
                            images = new ArrayList<String>();

                            // Hacemos un for para recorrer el array del json
                            for (int i = 0; i < jsonArray.length(); i++) {
                                // Aqui filtramos para obtener el titulo, precio, descripcion y la imagen
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String Titulo = jsonObject.getString("Titulo");
                                String Precio = jsonObject.getString("Precio");
                                String Descripcion = jsonObject.getString("Descripcion");
                                String Imagenes = jsonObject.getString("Image");

                                // Aqui añadimos todo el objeto al array list que hemos creado mas arriba
                                titulo.add(Titulo);
                                precio.add("S/" + Precio);
                                descripcion.add(Descripcion);
                                images.add(Imagenes);

                            }
                            // Aqui actualizamos el adapter para cargar los datos y que los muestre
                            adaptador = new Adaptador(context, carritoList);
                            tabla.setAdapter(adaptador);
// Cambio ooo

                        } catch (Exception e) {
                            Log.d("JSONException", e.getMessage());
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.networkResponse + " error");
                    }
                }
        );
        // Aqui enviamos la solicitud de la peticion
        requestQueue.add(request);

    }


}
