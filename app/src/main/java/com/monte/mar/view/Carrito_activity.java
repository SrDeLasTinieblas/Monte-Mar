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
    private final List<Carrito> carritoListDatoes = new ArrayList<>();
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();
    String payment = "";
    FirebaseFirestore firebaseFirestore;
    List<String> products = new ArrayList<>();
    List<Integer> amount = new ArrayList<>();
    String quantityAndProducts;
    TextView price;
    SharedPreferences sharedPreferences;

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        main();
    }
    private void main(){
        definingComponents();
        addDataTrolley();
        //onCustomers();
    }

    private boolean EmptyOrNoEmpty(){
        // cargamos la lista entonces
        /*String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME, "");
            Type typeList = new TypeToken<List<Carrito>>() {
            }.getType();
            carritoListDatoes.addAll(new Gson().fromJson(datos, typeList));
            Log.d("carritoListDatoes", "" + carritoListDatoes);*/
        return preferences.contains(Constants.SHARED_PREFERENCES_NAME);
    }



    public void delete(View view){
        try {
            //SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
            editor.clear().apply();

            if(preferences.contains(Constants.SHARED_PREFERENCES_NAME)) {
                // cargamos la lista entonces
                String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME, "");
                Type typeList = new TypeToken<List<Carrito>>() {
                }.getType();
                carritoListDatoes.addAll(new Gson().fromJson(datos, typeList));

                Log.d("carritoListDatoes", ""+ carritoListDatoes);

            }else {
                System.out.println("No hay datos :(");
            }

        }catch (Exception e){
            System.out.println("==> "+e);
        }

        //System.out.println(datos);
        /*Type typeList = new TypeToken<List<Carrito>>(){}.getType();
        carritoListDatoes.addAll(new Gson().fromJson(datos,typeList));
        System.out.println("hola");
        Iterator<Carrito> itr = carritoListDatoes.iterator();
        itr.remove();
        System.out.println("==>"+itr);*/

    }



    private List<Carrito> carritoListCompra;
    private CarritoAdaptador adapter;
    @SuppressLint("SetTextI18n")
    private void addDataTrolley(){
        //Aqui traemos los datos del shared sharedPreferences
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        //si hay datos en prefrerencias
        if(preferences.contains(Constants.SHARED_PREFERENCES_NAME)){
            String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME,"No se encontraron datos");
            Type typeList;
            typeList = new TypeToken<List<Carrito>>(){}.getType();

            carritoListCompra = new ArrayList<>();
            carritoListCompra.addAll(new Gson().fromJson(datos,typeList));
            adapter = new CarritoAdaptador(this, carritoListCompra);
            tablaCompras.setAdapter(adapter);

            // Aqui hacemos un foreach para recorrer todo lo que esta en las listas y guardarlos
            for(Carrito carrito : carritoListCompra){
                totalPrice += carrito.getPrecioTotal();
                products.add(carrito.getTitulo());
                amount.add(carrito.getCantidad());
            }
            price.setText("S/"+ totalPrice);
        }
    }

    private String TimeDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        return sdf.format(new Date());
    }

    private void searchItem(String titulo){
        for(Carrito carrito : carritoListCompra){
            if (Objects.equals(carrito.getTitulo(), titulo)){
                System.out.println("Es igual a titulo ==> "+ carrito.getTitulo());

            }else{
                System.out.println("No es igual a titulo ==> "+ carrito.getTitulo());
            }
        }
    }

    private void definingComponents(){
        this.tablaCompras = findViewById(R.id.tablaCompras);
        price = findViewById(R.id.PrecioTotal);
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