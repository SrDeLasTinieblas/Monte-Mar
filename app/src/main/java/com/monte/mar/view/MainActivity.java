package com.monte.mar.view;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.monte.mar.Adaptador;
import com.monte.mar.constants.Constants;
import com.monte.mar.constants.APIs;
import com.monte.mar.model.VolleyData;
import com.monte.mar.model.data.FirebaseData;
import com.monte.mar.model.Carrito;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import java.util.ArrayList;

import monte.montemar.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    LottieAnimationView lottieAnimationView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    FirebaseData firebaseData = new FirebaseData();

    SearchView searchView;
    GridView gridView;
    Adaptador adaptador;

    ImageView imageView; // LOGO
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RequestQueue requestQueue;
    private final List<Carrito> productsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main();
    }

    private void main(){
        definingComponents();
        definingFirebase();
        onImagenView(APIs.URLImage);
        getData();
        createNotificationChannel();
        notificaty();
        tableOnItemListener();
        searchView.setOnQueryTextListener(this);
    }

    public void tableOnItemListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Detalles_activity.class);
                // llamamos a carrito que seria la key y en el otro parametro le pasamos el arrayList y lo llenamos
                intent.putExtra(Constants.INTENT_NAME, productsList.get(i));
                startActivity(intent);
            }
        });
    }

    public void definingComponents() {
        gridView = findViewById(R.id.tabla);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animateView);
        requestQueue = Volley.newRequestQueue(this);
        imageView = findViewById(R.id.imageView2);
        searchView = findViewById(R.id.textSearch);
        firebaseData.uploadDataFireBase(MainActivity.this);
    }

    private void definingFirebase(){
        //userID = Autoh.getCurrentUser().getUid();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //idUser = firebaseAuth.getCurrentUser().getUid();
    }

    private void setPreferences(){
        sharedPreferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    //http://192.168.1.17:8000/
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                APIs.UrlProductos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Sleep(5000);

                            Type typeList = new TypeToken<List<Carrito>>() {
                            }.getType();

                            /**
                             * creamos un list con la respuesta que nos llega de la api y le decimos que con gson lo convierta a un typeList (Lista de productos)
                             */
                            List<Carrito> productsListResponse = new Gson().fromJson(response, typeList);

                            // Hacemos el llenado de datos y se lo pasamos a productsList
                            productsList.addAll(productsListResponse);


                            /**
                             * Aqui usamos lo que tenemos en la clase de adaptador y lo instanciamos
                             * Le pasamos como parametro el contexto y la lista de productos
                             * Para despues actualizar todo lo que tenemos en el adaptador
                             */
                            adaptador = new Adaptador(MainActivity.this, productsList);
                            gridView.setAdapter(adaptador);

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
    private void postRequest(){
    }

    /*private void PostRequest(){
        RequestQueue requestQueue = VolleyData.newRequestQueue(MainActivity.this);
        String URL_REQUEST="http://localhost:8080/API-PRUEBA/post_data.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("POST DATA: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(("POST DATA : RESPONSE Failed" + error));
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("data_1_post", "value 1 Data");
                params.put("data_2_post", "value 2 Data");
                params.put("data_3_post", "value 4 Data");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlenconded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }*/
    public void intoCarrito(/*View view*/) {
        // Cambiamos de activity
        Intent intent = new Intent(MainActivity.this, Carrito_activity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        try {
            intoCarrito();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void onImagenView(String URLImage) {
        // Aqui le ponemos una imagen de la empresa
        Glide.with(this)
                .load(URLImage)
                .placeholder(R.drawable.logo)
                .into(imageView);
    }

    public void CerrarSession(View view) {
        // Cerramos sesion
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
        Login();
    }

    private void Login() {
        Intent i = new Intent(this, Login_Activity.class);

        // Limpiar la pilas de las activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        //Toast.makeText(MainActivity.this, "Login con exito", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        //   26                    30
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void notificaty() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("Monte-Mar");
        builder.setContentText("Iniciando... ");
        builder.setPriority(PRIORITY_DEFAULT);
        builder.setLights(Color.BLUE, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());

    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String s) {
        adaptador.filtrado(s);
        return false;
    }
    @Override
    public boolean onClose() {
        adaptador.filtrado("");
        return false;
    }

}

























