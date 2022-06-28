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
import com.monte.mar.model.ShoppingCart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monte.mar.model.SweetAlertDialog;
import com.stripe.android.paymentsheet.PaymentSheet;
//import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import monte.montemar.R;

public class Detalles_activity extends AppCompatActivity implements View.OnClickListener {
    private ShoppingCart shoppingCart;
    private SharedPreferences preferences;
    //SharedPreferences.Editor editor;

    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();
    TextView textTitulo,textDescripcion , textPrecio;
    ImageView imageView;
    ImageButton addShopping;

    PaymentSheet paymentSheet;
    String custumerID;
    String EphericalKey;
    String ClientSecret;

    private int valor = 1;
    float precioTotal = 0f;
    String PagoStripe = "";
    TextView Aumentar, Disminuir, textCantidad;
    List<String> productos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);


        /*paymentConfiguration(this);
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });*/
        main();
    }
    private void main(){
        // definimos obj
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        definingComponents();
        getClassCarrito();
        getImageView();
        //onCustomers();
        // Aqui mandamos a llamar la funcion cargarDatosCarrito y le pasamos el id de la clase ShoppingCart
        CargarDatosCarrito(shoppingCart.getId());


        setViews();


    }

    @SuppressLint("SetTextI18n")
    public void btnAumentando(View view){
        // Add Cantidad en tu ShoppingCart Model
        shoppingCart.setCantidad(++valor);


        textCantidad.setText(String.valueOf(shoppingCart.getCantidad()));


        textPrecio.setText("S/"+(shoppingCart.getPrecioTotal()));
        String titulo = textTitulo.getText().toString().replace(" ","");//s1.replace('a','e');//replaces all occurrences of 'a' to 'e'
        System.out.println("KEY: " + titulo +" Precio: " + shoppingCart.getPrecioTotal() + "\n");
        // setPreferences((titulo), price);
        /*int e = sharedPreferences.getInt("price", 0);
        int datos = sharedPreferences.getInt("datos", 0);

        System.out.println(e);
        System.out.println(datos);*/

        //sharedPreferences = getSharedPreferences("LimpiatodoKristalClean", Context.MODE_PRIVATE);
        //String highScore = sharedPreferences.getString(("LimpiatodoKristalClean"), "sss");
        //Log.d("Log==> ", highScore);
        //System.out.println("dato ==> "+highScore);

    }

    public void btnDisminuir(View view){
        if (valor > 1) {
            shoppingCart.setCantidad(--valor);
            textCantidad.setText(String.valueOf(shoppingCart.getCantidad()));


            textPrecio.setText("S/"+(shoppingCart.getPrecioTotal()));
            String titulo = textTitulo.getText().toString().replace(" ","");//s1.replace('a','e');//replaces all occurrences of 'a' to 'e'
            System.out.println("KEY: " + titulo +" Precio: " + shoppingCart.getPrecioTotal() + "\n");
            //setPreferences((titulo), price);
            //float price = shoppingCart.getPrecio() * valor;
            //setPreferences(String.valueOf(textTitulo), String.valueOf(price));

        }else {
            textCantidad.setText(String.valueOf(shoppingCart.getCantidad()));


            textPrecio.setText("S/"+(shoppingCart.getPrecioTotal()));
            String titulo = textTitulo.getText().toString().replace(" ","");//s1.replace('a','e');//replaces all occurrences of 'a' to 'e'
            System.out.println("KEY: " + titulo +" Precio: " + shoppingCart.getPrecioTotal() + "\n");
            //setPreferences((titulo), price);
        }

    }

    private void setPreferences(String key, float precio){
        SharedPreferences preferencias=getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putFloat("price", precio);
        editor.apply();
        //finish();

        //float e = preferencias.getFloat(ti, 0.0f);
        //System.out.println(e);
        /*SharedPreferences preferencias=getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("mail", "et1.getText().toString()");
        editor.apply();
        finish();*/
        /*SharedPreferences preferencias=getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("price", String.valueOf(price));
        editor.apply();*/
        //finish();
    }

    //Aqui mandamos a llamar al button addShopping para cuando le den click se guarde el producto o se elimine
    public void AddShopping(View view){
        GuardarOrRemover();
    }
    // esto es para que no de error al obtener una imagen
    private void getImageView(){
        if(shoppingCart.getImage()!=null){
            if(!shoppingCart.getImage().isEmpty()){
                Glide.with(this)
                        .load(shoppingCart.getImage())
                        .placeholder(R.drawable.logo)
                        .into(imageView);
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private void getClassCarrito(){
        Bundle bundle = getIntent().getExtras();

        //aqui hacemos uso de la clase shoppingCart mandamos a llamar intent_name
        shoppingCart = bundle.getParcelable(Constants.INTENT_NAME);

        /**
         *


         Precio Leji = 10
         Cantidad    = 1
         Image      //////

         */


        // Obtenemos el contenido de la clase ShoppingCart

        //textPrecio.setText("S/"+(shoppingCart.getPrecio()) * valor);
        //textPrecio.setText("S/"+((shoppingCart.getPrecio() * valor)));
    }

    private void setViews(){
        textTitulo.setText(shoppingCart.getTitulo());
        textDescripcion.setText(shoppingCart.getDescripcion());
        textPrecio.setText("S/"+((shoppingCart.getPrecioTotal())));
        // Warning
        // Si no esta shoppingCart en el SharedPreferences, entonces la amount por defecto es null
        // porque el Json que traemos del server no tiene dicho valor

        textCantidad.setText(String.valueOf(shoppingCart.getCantidad()));


    }



    boolean estaEnSharedPreference = false;
    boolean inCarrito = false;

    private final List<ShoppingCart> shoppingCartListDatoes = new ArrayList<>();

    private void CargarDatosCarrito(Integer idCompra) {
        // si hay datos
        if(preferences.contains(Constants.SHARED_PREFERENCES_NAME)){
            // cargamos la lista entonces
            String datos = preferences.getString(Constants.SHARED_PREFERENCES_NAME,"");
            Type typeList = new TypeToken<List<ShoppingCart>>(){}.getType();
            shoppingCartListDatoes.addAll(new Gson().fromJson(datos,typeList));

            // Con el foreach recorremos la lista de datos
            for (ShoppingCart shoppingCart : shoppingCartListDatoes){
                // Aqui comparamos los IDs si son iguales es true
                if(shoppingCart.getId() == (idCompra)){
                    estaEnSharedPreference = true;
                    inCarrito = true;
                    this.shoppingCart = shoppingCart;
                    valor = this.shoppingCart.getCantidad();
                    break;
                }
            }
            updateBackGround(estaEnSharedPreference);

        }


    }

    @Override
    public void onBackPressed() {
        /**

         estaEnSharedPreference = falso
         .->>>>>>>>>>>  GuardarOrRemover() =  anadir

         estaEnSharedPreference = true
         .->>>>>>>>>>>  GuardarOrRemover() =  eliminar

         */

        if(inCarrito){

            for(ShoppingCart shoppingCart : shoppingCartListDatoes){

                if(shoppingCart.getId() == (this.shoppingCart.getId())){
                    shoppingCart = this.shoppingCart;
                    break;
                }
            }

            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.SHARED_PREFERENCES_NAME, new Gson().toJson(shoppingCartListDatoes));
            editor.apply();

        }

        super.onBackPressed();
    }

    // Aqui actualizamos el color
    private void updateBackGround(boolean estaInDatos){
        // Si estaInDatos es verdadero entonces para pintarlo de color verde el boton
        if(estaInDatos){
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
            Iterator<ShoppingCart> itr = shoppingCartListDatoes.iterator();

            // Mientras halla siguiente va seguir la condicion
            while (itr.hasNext()) {
                // Aqui obtenemos el id y lo comparamos con el siguiente id si es igual lo borrara
                if(shoppingCart.getId() == (itr.next().getId())){
                    itr.remove();
                    Toast.makeText(getApplicationContext(), "Quitado del shoppingCart" , Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } else {
            // sino está lo agregas
            estaEnSharedPreference = true;
            shoppingCartListDatoes.add(shoppingCart);
            Toast.makeText(getApplicationContext(), "Añadido al shoppingCart" , Toast.LENGTH_SHORT).show();
        }


        // Aqui le estoy diciendo que me guarde los datos en un json y aplico los cambios
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_NAME, new Gson().toJson(shoppingCartListDatoes));
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
                textDescripcion.setText("uploadDataFireBase: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textDescripcion.setText("uploadDataFireBase: " + error);
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
        // First get uploadDataFireBase of DataBase :C
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
                        payment = textPrecio.getText().toString();

                        String[] parts = payment.split("\\.");
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
