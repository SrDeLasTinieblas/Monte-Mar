package com.monte.mar.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.monte.mar.constants.Constants;
import com.monte.mar.model.Carrito;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import monte.montemar.R;

public class Detalles_activity extends AppCompatActivity implements View.OnClickListener {

    private Carrito carrito;
    private SharedPreferences preferences;
    //SharedPreferences.Editor editor;

    TextView textTitulo,textDescripcion , textPrecio;
    ImageView imageView;
    ImageButton addShopping;

    private int valor = 1;
    TextView Aumentar, Disminuir, textCantidad;
    List<String> productos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        main();
    }
    private void main(){
        // definimos obj
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        definingComponents();
        getClassCarrito();
        getImageView();

        // Aqui mandamos a llamar la funcion cargarDatosCarrito y le pasamos el id de la clase Carrito
        CargarDatosCarrito(carrito.getId());
        setViews();
    }

    public void btnAumentando(View view){
        // Add Cantidad en tu Carrito Model
        carrito.setCantidad(++valor);
        textCantidad.setText(String.valueOf(carrito.getCantidad()));
        textPrecio.setText("S/"+(carrito.getPrecioTotal()));
        //String titulo = textTitulo.getText().toString().replace(" ","");//s1.replace('a','e');//replaces all occurrences of 'a' to 'e'
        //System.out.println("KEY: " + titulo +" Precio: " + carrito.getPrecioTotal() + "\n");
    }

    public void btnDisminuir(View view){
        if (valor > 1) {
            carrito.setCantidad(--valor);
            textCantidad.setText(String.valueOf(carrito.getCantidad()));


            textPrecio.setText("S/"+(carrito.getPrecioTotal()));
            String titulo = textTitulo.getText().toString().replace(" ","");//s1.replace('a','e');//replaces all occurrences of 'a' to 'e'
            System.out.println("KEY: " + titulo +" Precio: " + carrito.getPrecioTotal() + "\n");
            //setPreferences((titulo), precio);
            //float precio = carrito.getPrecio() * valor;
            //setPreferences(String.valueOf(textTitulo), String.valueOf(precio));

        }else {
            textCantidad.setText(String.valueOf(carrito.getCantidad()));


            textPrecio.setText("S/"+(carrito.getPrecioTotal()));
            String titulo = textTitulo.getText().toString().replace(" ","");//s1.replace('a','e');//replaces all occurrences of 'a' to 'e'
            System.out.println("KEY: " + titulo +" Precio: " + carrito.getPrecioTotal() + "\n");
            //setPreferences((titulo), precio);
        }

    }

    //Aqui mandamos a llamar al button addShopping para cuando le den click se guarde el producto o se elimine
    public void AddShopping(View view){
        GuardarOrRemover();
    }
    // esto es para que no de error al obtener una imagen
    private void getImageView(){
        if(carrito.getImage()!=null){
            if(!carrito.getImage().isEmpty()){
                Glide.with(this)
                        .load(carrito.getImage())
                        .placeholder(R.drawable.logo)
                        .into(imageView);
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private void getClassCarrito(){
        Bundle bundle = getIntent().getExtras();

        //aqui hacemos uso de la clase carrito mandamos a llamar intent_name
        carrito = bundle.getParcelable(Constants.INTENT_NAME);
    }

    private void setViews(){
        textTitulo.setText(carrito.getTitulo());
        textDescripcion.setText(carrito.getDescripcion());
        textPrecio.setText("S/"+((carrito.getPrecioTotal())));
        // Warning
        // Si no esta carrito en el SharedPreferences, entonces la cantidad por defecto es null
        // porque el Json que traemos del server no tiene dicho valor

        textCantidad.setText(String.valueOf(carrito.getCantidad()));


    }

    boolean estaEnSharedPreference = false;
    boolean inCarrito = false;
    private final List<Carrito> carritoListDatoes = new ArrayList<>();

    private void CargarDatosCarrito(Integer idCompra) {
        // si hay datos
        if(preferences.contains(Constants.SHARED_PREFERENCES_NAME)){
            // cargamos la lista entonces
            String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME,"");
            Type typeList = new TypeToken<List<Carrito>>(){}.getType();
            carritoListDatoes.addAll(new Gson().fromJson(datos,typeList));

            // Con el foreach recorremos la lista de datos
            for (Carrito carrito : carritoListDatoes){
                // Aqui comparamos los IDs si son iguales es true
                if(carrito.getId().equals(idCompra)){
                    estaEnSharedPreference = true;
                    inCarrito = true;
                    carrito = carrito;
                    valor = this.carrito.getCantidad();
                    break;
                }
            }
            updateBackGround(estaEnSharedPreference);
        }
    }
    @Override
    public void onBackPressed() {
        if(inCarrito){
            for(Carrito carrito : carritoListDatoes){
                if(carrito.getId().equals(this.carrito.getId())){
                    carrito = this.carrito;
                    break;
                }
            }

            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.SHARED_PREFERENCES_NAME, new Gson().toJson(carritoListDatoes));
            editor.apply();
        }
        super.onBackPressed();
    }

    // Aqui actualizamos el color
    private void updateBackGround(boolean estaInDatos){
        // Si estaInDatos es verdadero entonces para pintarlo de color verde el boton
        if(estaInDatos){
            // Si el
            addShopping.setBackground(this.getResources().getDrawable(R.drawable.border_carrito_green));
            inCarrito =  true;
        } else {
            // Si no lo pintara de gris
            addShopping.setBackground(this.getResources().getDrawable(R.drawable.border_carrito));
            inCarrito =  false;
        }
    }
    // Metodo para el boton guardar
    private void GuardarOrRemover(){
        if(estaEnSharedPreference){
            // Si hay datos lo eliminara
            estaEnSharedPreference = false;
            Iterator<Carrito> itr = carritoListDatoes.iterator();

            // Mientras halla siguiente va seguir la condicion
            while (itr.hasNext()) {
                // Aqui obtenemos el id y lo comparamos con el siguiente id si es igual lo borrara
                if(carrito.getId().equals(itr.next().getId())){
                    itr.remove();
                    Toast.makeText(getApplicationContext(), "Quitado del carrito" , Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } else {
            // sino está lo agregas
            estaEnSharedPreference = true;
            carritoListDatoes.add(carrito);
            Toast.makeText(getApplicationContext(), "Añadido al carrito" , Toast.LENGTH_SHORT).show();
        }

        // Aqui le estoy diciendo que me guarde los datos en un json y aplico los cambios
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_NAME, new Gson().toJson(carritoListDatoes));
        editor.apply();

        // Aqui llamamos a la funcion updateBackGranud y como parametro le ponemos estaInDatos
        updateBackGround(estaEnSharedPreference);

    }

    private void definingComponents(){
        textTitulo = findViewById(R.id.textTituloDetalle);
        textDescripcion = findViewById(R.id.textDescripcionDetalle);
        textPrecio = findViewById(R.id.textPrecioDetalle);
        imageView = findViewById(R.id.imageView);
        addShopping = findViewById(R.id.addShopping); // Button

        Aumentar = findViewById(R.id.btnAumentar);
        Disminuir = findViewById(R.id.btnDisminuir);
        textCantidad = findViewById(R.id.txtCantidad);
    }
    @Override
    public void onClick(View view) {
    }
    /*private void getRequest(){
        RequestQueue requestQueue = VolleyData.newRequestQueue(Detalles_activity.this);
        String URL_REQUEST="http://localhost:8080/API-PRUEBA/post_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                textDescripcion.setText("data: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textDescripcion.setText("data: " + error);
            }
        });
        requestQueue.add(stringRequest);

    }*/

    /*private void onCustomers(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                APIs.ApiStripeCustomers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            custumerID = object.getString("id");
                            getEphericalKey(custumerID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("error.networkResponse: " + error.networkResponse);
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                //Empleado.variableAcomparar .. SECRET_KEY
                header.put("Authorization", "Bearer " + keys.SECRET_KEY);
                return header;
            }
        };

        // Aqui enviamos la solicitud de la peticion
        RequestQueue requestQueue = Volley.newRequestQueue(Detalles_activity.this);
        requestQueue.add(request);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            //Toast.makeText(this, "payment success", Toast.LENGTH_SHORT).show();
            sweetAlertDialog.sweetAlertSuccess(this);
        }//sweetAlertDialog.sweetAlertError(this);

    }

    private void getEphericalKey(String custumerID) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                APIs.ApiStripeEphemeral_keys,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            getClientSecret(custumerID, EphericalKey);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("error.getEphericalKey: " + error.networkResponse);
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + keys.SECRET_KEY);
                header.put("Stripe-Version", "2020-08-27");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", custumerID);
                return params;
            }
        };

        // Aqui enviamos la solicitud de la peticion
        RequestQueue requestQueue = Volley.newRequestQueue(Detalles_activity.this);
        requestQueue.add(request);
    }

    private void getClientSecret(String custumerID, String EphericalKey) {
        // First get data of DataBase :C
        FirebaseData firebaseData = new FirebaseData();
        firebaseData.getDataUser(Detalles_activity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // apellidos[0] = value.getString("apellidos");
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        APIs.ApiStripePayment_intents,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    System.out.println("RESPUESTA DE PAYMENT INTENTS===> ");
                                    ClientSecret = object.getString("client_secret");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.err.println(error.networkResponse + " error");
                            }

                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> header = new HashMap<>();
                        header.put("Authorization", "Bearer " + keys.SECRET_KEY);
                        header.put("Stripe-Version", "2020-08-27");
                        return header;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        PagoStripe = textPrecio.getText().toString();

                        String[] parts = PagoStripe.split("\\.");
                        String apellidos = value.getString("apellidos");
                        String direccion = value.getString("direccion");
                        String edad = value.getString("edad");
                        String estadoProvincia = value.getString("estadoProvincia");
                        String id = value.getString("id");
                        String mail = value.getString("mail");
                        String nombres = value.getString("nombres");
                        String telefono = value.getString("telefono");


                        String soles = parts[0];
                        String centavos = parts[1] ;
                        params.put("customer", custumerID);
                        params.put("description",
                                "[Cliente]: " + id + "\n"+
                                        "Nombres: " + nombres +" "+ apellidos +", "+
                                        "Direccion: " + direccion + ", " +
                                        "Edad: " + edad + ", " +
                                        "Estado Y/O provincia: " + estadoProvincia + ", " +
                                        "Email: " + mail + ", " +
                                        "Telefono: " + telefono + " " +
                                        "[Productos]: " + textTitulo);
                        //params.put("receipt_email", mail);
                        params.put("amount", soles + centavos+ "0");
                        params.put("currency", "pen");
                        params.put("automatic_payment_methods[enabled]", "true");
                        return params;
                    }
                };

                // Aqui enviamos la solicitud de la peticion
                RequestQueue requestQueue = Volley.newRequestQueue(Detalles_activity.this);
                requestQueue.add(request);


            }
        });

    }

    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("Monte-Mar company",
                        new PaymentSheet.CustomerConfiguration(
                                custumerID,
                                EphericalKey
                        ))

        );
    }

    private void paymentConfiguration(Context context){
        PaymentConfiguration.init(context, keys.PUBLISH_KEY);
    }

    public void onlickComprar(View view){
        try {
        PaymentFlow();
    }catch (Exception ex){
        Toast.makeText(this, "Por favor espere un momento", Toast.LENGTH_SHORT).show();
        System.out.println(ex+" <==");
    }
}
*/
}
