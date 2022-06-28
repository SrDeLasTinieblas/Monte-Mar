package com.monte.mar.data;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.monte.mar.constants.keys;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Payments {
    String SECRET_KEY = "sk_test_51K1HxzInA4seMnObqljmh8wtPw2tS3RTIOYGdMIehahSp2cA3m1wDiYBtzt2JUtqghHwM4fz0idI74PoNIURAKjA00T1XLINJ8";
    //String PUBLISH_KEY = "pk_test_51K1HxzInA4seMnObTZKEglgrDGloAeK7gZQcLMVHxKduCVKYjnUVc8hbspGDxbMBtZZlqu9cWOAPcIqLjJ4l6rf100bsncJeKk";
    PaymentSheet paymentSheet;
    String custumerID;
    String EphericalKey;
    String ClientSecret;
    //RequestQueue requestQueue;


    public void onCustomers(Context context) {
        //Toast.makeText(context, " HOLA", Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            custumerID = object.getString("id");
                            //Toast.makeText(Payments.class, custumerID, Toast.LENGTH_SHORT).show();
                            System.out.println("customerID " + custumerID);

                            getEphericalKey(custumerID, context);

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
                header.put("Authorization", "Bearer " + SECRET_KEY);

                return header;
            }
        };

        // Aqui enviamos la solicitud de la peticion
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    /*public void setPaymentSheet(Context context){
        paymentSheet = new PaymentSheet(context, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });*/

    public void getEphericalKey(String custumerID, Context context) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            //Toast.makeText(MainActivity.this, EphericalKey, Toast.LENGTH_SHORT).show();
                            System.out.println("EphericalKey " + EphericalKey);

                            getClientSecret(custumerID, EphericalKey, context);

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
                header.put("Authorization", "Bearer " + SECRET_KEY);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void getClientSecret(String custumerID, String EphericalKey, Context context) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");
                            //Toast.makeText(MainActivity.this, ClientSecret, Toast.LENGTH_SHORT).show();
                            System.out.println("ClientSecret " + ClientSecret);

                            //PaymentFlow();

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
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2020-08-27");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", custumerID);
                params.put("amount", "1000" + "00");
                params.put("currency", "usd");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        // Aqui enviamos la solicitud de la peticion
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("ABC Company",
                        new PaymentSheet.CustomerConfiguration(
                                custumerID,
                                EphericalKey
                        ))
        );
    }

    public void paymentConfiguration(Context context){
        PaymentConfiguration.init(context, keys.PUBLISH_KEY);
    }

}



















