package com.monte.mar.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.monte.mar.CarritoAdaptador;
import com.monte.mar.constants.Constants;
import com.monte.mar.model.Carrito;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monte.mar.model.Ubicacion;
import com.monte.mar.model.data.FirebaseData;
import com.monte.mar.model.SweetAlertDialog;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import monte.montemar.R;

public class Carrito_activity extends AppCompatActivity {
    private GridView tablaCompras;
    float totalPrice = 0f;
    private final List<Carrito> shoppingCartListDatoes = new ArrayList<>();
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();
    String payment = "";
    FirebaseFirestore firebaseFirestore;
    List<String> products = new ArrayList<>();
    List<Integer> amount = new ArrayList<>();
    String quantityAndProducts;
    TextView price;
    LottieAnimationView lottieAnimationView;
    private List<Carrito> shoppingCartListCompra;
    private CarritoAdaptador adapter;
    RequestQueue requestQueue;

    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        main();
    }
    private void main(){
        definingComponents();
        addDataCart();
        getDataLocalitation("http://ip.jsontest.com/");

        //onCustomers();
    }

    private String getCarrito(){
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME, "");

        Type typeList = new TypeToken<List<Carrito>>() {
        }.getType();
        shoppingCartListDatoes.addAll(new Gson().fromJson(datos, typeList));

        return datos;
    }


    //https://geo.ipify.org/api/v2/country?apiKey=at_mxXCuA3CYLZ0AD3Lzath30Oprn6YY&ipAddress=190.238.238.248
    //http://ip.jsontest.com/
    private void getDataLocalitation(String api){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Sleep(5000);
                            /**
                             *                             Type typeList = new TypeToken<List<Carrito>>() {
                             *                             }.getType();
                             *
                             *                             List<Carrito> productsListResponse = new Gson().fromJson(response, typeList);
                             *                             productsList.addAll(productsListResponse);
                             */

                            //Toast.makeText(Carrito_activity.this, response, Toast.LENGTH_SHORT).show();
                            Type typeList = new TypeToken<Ubicacion>() {
                            }.getType();
                            String iPS = new Gson().fromJson(response, typeList);
                            ip = iPS;

                            System.out.println("==>> IP: " + ip);
                            Toast.makeText(Carrito_activity.this, ip, Toast.LENGTH_SHORT).show();


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

    public void delete(View view){

        try {

            SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
            editor.clear().apply();
            shoppingCartListCompra.clear();

            adapter = new CarritoAdaptador(this, shoppingCartListCompra);
            lottieAnimationView.playAnimation();
            tablaCompras.setAdapter(adapter);

        }catch (Exception e){
            Toast.makeText(this, "Carrito ya limpio", Toast.LENGTH_SHORT).show();
            System.out.println("==>d "+e);
        }

    }

    @SuppressLint("SetTextI18n")
    private void addDataCart(){
        //Aqui traemos los datos del shared sharedPreferences
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        //si hay datos en prefrerencias
        if(preferences.contains(Constants.SHARED_PREFERENCES_NAME)){
            String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME,"No se encontraron datos");
            Type typeList;
            typeList = new TypeToken<List<Carrito>>(){}.getType();

            shoppingCartListCompra = new ArrayList<>();
            shoppingCartListCompra.addAll(new Gson().fromJson(datos,typeList));
            adapter = new CarritoAdaptador(this, shoppingCartListCompra);
            tablaCompras.setAdapter(adapter);

            // Aqui hacemos un foreach para recorrer todo lo que esta en las listas y guardarlos
            for(Carrito shoppingCart : shoppingCartListCompra){
                totalPrice += shoppingCart.getPrecioTotal();
                products.add(shoppingCart.getTitulo());
                amount.add(shoppingCart.getCantidad());
            }
            price.setText("S/"+ totalPrice);
        }
    }

    private String TimeDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        return sdf.format(new Date());
    }

    private void searchItem(String titulo){
        for(Carrito shoppingCart : shoppingCartListCompra){
            if (Objects.equals(shoppingCart.getTitulo(), titulo)){
                System.out.println("Es igual a titulo ==> "+ shoppingCart.getTitulo());

            }else{
                System.out.println("No es igual a titulo ==> "+ shoppingCart.getTitulo());
            }
        }
    }

    private void definingComponents(){
        this.tablaCompras = findViewById(R.id.tablaCompras);
        price = findViewById(R.id.PrecioTotal);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animateView);
        requestQueue = Volley.newRequestQueue(this);

    }

    public void onClickBuy(View view) {
        try {
            uploadDataFireBase();
        }catch (Exception ex){
            Toast.makeText(this, "Por favor espere un momento", Toast.LENGTH_SHORT).show();
            Log.e("onClickBuy: ", ""+ex);
        }
    }

    public void uploadDataFireBase() {
        FirebaseData firebaseData = new FirebaseData();
        firebaseData.getDataUser(Carrito_activity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                Map<String, String> params = new HashMap<>();
                payment = price.getText().toString();
                firebaseFirestore = FirebaseFirestore.getInstance();

                String lastName = value.getString("apellidos");
                String address = value.getString("direccion");
                String age = value.getString("edad");
                String stateAndProvince = value.getString("estadoProvincia");
                String id = value.getString("id");
                String mail = value.getString("mail");
                String name = value.getString("nombres");
                String phone = value.getString("telefono");

                String[] parts = payment.split("\\.");
                String suns = parts[0];
                String cents = parts[1];

                params.put("ID", "" + id);
                params.put("Nombres", "" + name + " " + lastName);
                params.put("Direccion", "" + address);
                params.put("Edad", "" + age);
                params.put("Estado Y/O provincia", "" + stateAndProvince);
                params.put("Telefono", "" + phone);
                params.put("Fecha y hora", "" + TimeDate());

                for (int i = 0; i < products.size(); i++) {
                    quantityAndProducts = products + " amount: " + amount;
                }
                params.put("Productos", " " + quantityAndProducts);
                params.put("Email", mail);
                params.put("amount", suns + "." + cents + "0");
                params.put("currency", "pen");
                firebaseFirestore.collection("Pedidos").document(name + " " + lastName).set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        sweetAlertDialog.sweetAlertSuccessBuy(Carrito_activity.this);
                        //Toast.makeText(Carrito_activity.this, "Los Datos se enviaron, por favor espere atentamente a su correo", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void onClick(View view) {}

}