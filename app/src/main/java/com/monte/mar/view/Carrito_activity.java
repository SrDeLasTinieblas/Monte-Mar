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
import com.android.volley.RequestQueue;
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
    //private final List<Carrito> shoppingCartListDatoes = new ArrayList<>();
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();
    String payment = "";
    FirebaseFirestore firebaseFirestore;
    List<String> products = new ArrayList<>();
    List<Integer> amount = new ArrayList<>();
    String quantityAndProducts;
    TextView price;
    LottieAnimationView lottieAnimationView;
    private final List<Carrito> shoppingCartListCompra = new ArrayList<>();
    private CarritoAdaptador adapter;
    RequestQueue requestQueue;

    //public String Response;

    //private String IP;
    //private String IPInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        main();
    }
    private void main(){
        definingComponents();
        addDataCart();
        //getDataIp("http://ip.jsontest.com/");
        //getTest();

        //onCustomers();
    }

    /*private String getCarrito(){
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME, "");

        Type typeList = new TypeToken<List<Carrito>>() {
        }.getType();
        shoppingCartListDatoes.addAll(new Gson().fromJson(datos, typeList));

        return datos;
    }*/

    //https://geo.ipify.org/api/v2/country?apiKey=at_mxXCuA3CYLZ0AD3Lzath30Oprn6YY&ipAddress=?
    //http://ip.jsontest.com/

    /*private void getDataIp(String api){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ip= jsonObject.getString("ip");
                            //Toast.makeText(Carrito_activity.this, "La ip de esta red es: ==>> "+ip, Toast.LENGTH_SHORT).show();
                            //Sleep(5000);
                            /**
                             *                             Type typeList = new TypeToken<List<Carrito>>() {
                             *                             }.getType();
                             *
                             *                             List<Carrito> productsListResponse = new Gson().fromJson(response, typeList);
                             *                             productsList.addAll(productsListResponse);
                             */

                           // IP = ip;


                        /*} catch (Exception e) {
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

    }*/

    /*private void getDataGeo(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://geo.ipify.org/api/v2/country?apiKey=at_mxXCuA3CYLZ0AD3Lzath30Oprn6YY&ipAddress="+IP,
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

                         /*   IPInfo = response;

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
    }*/

   /* private void getTest(){
        VolleyData volleyData = new VolleyData();
        volleyData.buscarJugador("https://jsonplaceholder.typicode.com/todos/1", getApplicationContext());
    }*/

    public void delete(View view){
        try {
            /**
             * Llamamos a la constante "comprasDatos" para decirle que queremos limpiar todo lo que se encuentre en las
             *  preferencias y tambien limpiamos la list para despues actualizar el adaptador.
             */
            SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
            editor.clear().apply();
            shoppingCartListCompra.clear();

            adapter = new CarritoAdaptador(this, shoppingCartListCompra);
            lottieAnimationView.playAnimation();
            tablaCompras.setAdapter(adapter);

        }catch (Exception e){
            //Toast.makeText(this, "Carrito ya limpio", Toast.LENGTH_SHORT).show();
            System.out.println("==>d "+e);
        }

    }
    // Comprobando si el carrito esta limpio
    private boolean isItClean(){
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.contains(Constants.SHARED_PREFERENCES_NAME);
    }

    /*private void test(){
        Toast.makeText(this, "PROBANDOOOOOO", Toast.LENGTH_SHORT).show();
    }*/
    
    @SuppressLint("SetTextI18n")
    private void addDataCart(){
        //Aqui traemos los datos del shared sharedPreferences
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        /**
         * Si en el CharedPrefrences contiene la key "comprasDatos" lo que hara sera
         *  es agregar lo que serializamos en la clase carrito y ponerlo en una list
         *  y hacemos el llenado de datos y actualizamos el adaptador
         */
        if(isItClean()){
            String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME,"No se encontraron datos");
            Type typeList;
            typeList = new TypeToken<List<Carrito>>(){}.getType();

            shoppingCartListCompra.addAll(new Gson().fromJson(datos,typeList));
            adapter = new CarritoAdaptador(this, shoppingCartListCompra);
            tablaCompras.setAdapter(adapter);

            /** Aqui hacemos un foreach para recorrer todo lo que esta en lista y sacar el precio, titulo y la cantidad
             *   para asi sacar el precio total, la cantidad y los nombres de los productos
             */
            for(Carrito shoppingCart : shoppingCartListCompra){
                totalPrice += shoppingCart.getPrecioTotal();
                products.add(shoppingCart.getTitulo());
                amount.add(shoppingCart.getCantidad());
            }
            price.setText("S/"+ totalPrice);
        }else{
            //test();
        }
    }

    private String TimeDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        return sdf.format(new Date());
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
                //params.put("Data", "" + IPInfo);

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
                    }
                });
            }
        });
    }

    public void onClick(View view) {}

}