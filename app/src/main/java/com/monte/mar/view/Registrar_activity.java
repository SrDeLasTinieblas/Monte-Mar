package com.monte.mar.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.monte.mar.model.SweetAlertDialog;
//import com.monte.mar.users;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import monte.montemar.R;

public class Registrar_activity extends AppCompatActivity implements View.OnClickListener {
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();
    EditText Nombres, Apellidos, Direccion, EstadoProvincia, Telefono, Edad, pass, email;
    Button btnregistrar;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore mFirestore;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        main();
    }

    private void main() {
        definingFireBase();
        definingComponents();
        //getUsers();
    }

    /*private void getUsers() {
        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapShot : snapshot.getChildren()) {
                    //users usuario = dataSnapShot.getValue(users.class);

                    /*String nombre = usuario.getNombre();
                    String Apellido = usuario.getApellido();
                    String Direccion = usuario.getDireccion();
                    String Edad = usuario.getEdad();
                    String Email = usuario.getEmail();
                    String EstadoProvincia = usuario.getEstadoProvincia();
                    String Telefono = usuario.getTelefono();*/

                    /*Log.e("Datos: ", ""+nombre);
                    Log.e("Datos: ", ""+Apellido);
                    Log.e("Datos: ", ""+Direccion);
                    Log.e("Datos: ", ""+Edad);
                    Log.e("Datos: ", ""+Email);
                    Log.e("Datos: ", ""+EstadoProvincia);
                    Log.e("Datos: ", ""+Telefono);*/
          /*  @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }*/

    /*public void registro() {
        String mail = email.getText().toString();
        String password = pass.getText().toString();

        //Validamos el email y el password
        if (awesomeValidation.validate()) {
            firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Registrar_activity.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String errorCode = ((FirebaseAuthActionCodeException) task.getException()).getErrorCode();
                        Toast.makeText(Registrar_activity.this, errorCode, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Coloca todo los campos", Toast.LENGTH_SHORT).show();
        }

    }*/

    private boolean dataIsEmpty(String mail, String password, String nombres, String apellidos, String direccion, String estadoProvincia, String telefono, String edad) {
        return mail.isEmpty() || password.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || direccion.isEmpty() || estadoProvincia.isEmpty() || telefono.isEmpty() || edad.isEmpty();
    }

    private void registerUser(String mail,String password, String nombre, String apellido, String direccion, String EstadoProvincia, String Telefono, String Edad){
        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id",id);
                map.put("mail",mail);
                map.put("nombres",nombre);
                map.put("apellidos",apellido);
                map.put("direccion",direccion);
                map.put("estadoProvincia",EstadoProvincia);
                map.put("telefono",Telefono);
                map.put("edad",Edad);

                sweetAlertDialog.sweetAlertLoading(Registrar_activity.this);
                mFirestore.collection("usuario").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(Registrar_activity.this, Login_Activity.class));
                        sweetAlertDialog.sweetAlertSuccessRegistro(Registrar_activity.this, nombre);
                        Toast.makeText(Registrar_activity.this, "Usuario Registrado con exito", Toast.LENGTH_SHORT).show();
                        UpDataToRealtimeDatabase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registrar_activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registrar_activity.this, "Error al Registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpDataToRealtimeDatabase(){
        Map<String, Object> userData = new HashMap<>();

        // En el map que se llama "userData" agrego los datos del usuario
        userData.put("Nombre",Nombres.getText().toString());
        userData.put("Apellido", Apellidos.getText().toString());
        userData.put("Direccion",Direccion.getText().toString());
        userData.put("Telefono",Telefono.getText().toString());
        userData.put("EstadoProvincia", EstadoProvincia.getText().toString());
        userData.put("Edad", Edad.getText().toString());
        //userData.put("Contraseña", pass.getText().toString());
        userData.put("Email", email.getText().toString());

        // En la base de datos creo un hijo que se llame "Usuarios" y agrego el valor que se encuentra en "userData"
        databaseReference.child("Usuarios").push().setValue(userData);
        //Register();
    }

    public void UpDataToAuthentication(View view) {
        String mail = email.getText().toString();
        String password = pass.getText().toString();
        String nombres = Nombres.getText().toString();
        String apellidos = Apellidos.getText().toString();
        String direccion = Direccion.getText().toString();
        String estadoProvincia = EstadoProvincia.getText().toString();
        String telefono = Telefono.getText().toString();
        String edad = Edad.getText().toString();

        // Vereficamos que el email y password no esten vacios
        if (dataIsEmpty(mail, password, nombres, apellidos, direccion, estadoProvincia, telefono, edad)) {
            Toast.makeText(this, "Complete los datos", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (awesomeValidation.validate()) {
                    registerUser(mail, password, nombres, apellidos, direccion, estadoProvincia, telefono, edad);

                }else{
                    Toast.makeText(this, "Debes poner los datos correctos", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception E){
                Toast.makeText(this, E.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void definingFireBase(){
        mFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.registroEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.registroContraseña, ".{6,}", R.string.invalid_password);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void definingComponents(){
        email = findViewById(R.id.registroEmail);
        pass = findViewById(R.id.registroContraseña);
        Direccion = findViewById(R.id.registroDireccion);
        Nombres = findViewById(R.id.registroNombre);
        Apellidos = findViewById(R.id.registroApellido);
        Telefono = findViewById(R.id.registroTelefono);
        EstadoProvincia = findViewById(R.id.registroEstado);
        Edad = findViewById(R.id.registroEdad);
        btnregistrar = findViewById(R.id.btnRegistrar);
    }
    @Override
    public void onClick(View view) {
    }


}




























































