package com.monte.mar.data;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.monte.mar.model.SweetAlertDialog;

public class FirebaseData {
    private String idUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase database;
    String name;
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();

    // Creamoss la funcion para guardar los datos
    public String getDataUser(Context context, EventListener<DocumentSnapshot> listener) {
        final String[] apellidos = new String[1];
        try {
            definingFirebase();
            DocumentReference documentReference = fStore.collection("usuario").document(idUser);
            documentReference.addSnapshotListener(listener);
        } catch (Exception e) {
            System.err.println(e);
        }
        return apellidos[0];
    }

    public void uploadDataFireBase(Context context) {
        FirebaseData firebaseData = new FirebaseData();
        firebaseData.getDataUser(context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                name = value.getString("nombres");
                sweetAlertDialog.sweetAlertSuccessRegistro(context, name);

            }
        });
    }


    public void getDatabase(@Nullable DocumentSnapshot value) {
        String apellidos = value.getString("apellidos");
        String direccion = value.getString("direccion");
        String edad = value.getString("edad");
        String estadoProvincia = value.getString("estadoProvincia");
        String id = value.getString("id");
        String mail = value.getString("mail");
        String nombres = value.getString("nombres");
        String telefono = value.getString("telefono");

        System.out.println("apellidos: " + apellidos);
        System.out.println("direccion: " + direccion);
        System.out.println("edad: " + edad);
        System.out.println("estadoProvincia: " + estadoProvincia);
        System.out.println("id: " + id);
        System.out.println("mail: " + mail);
        System.out.println("nombres: " + nombres);
        System.out.println("telefono: " + telefono);
    }


    private void definingFirebase(){
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
    }

}