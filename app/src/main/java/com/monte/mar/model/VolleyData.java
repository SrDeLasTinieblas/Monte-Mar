package com.monte.mar.model;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monte.mar.Adaptador;
import com.monte.mar.constants.APIs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class VolleyData {

    RequestQueue requestQueue;

    public void getData(Context context/*String urlApi, String nameJsonObject */) {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos/1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

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
