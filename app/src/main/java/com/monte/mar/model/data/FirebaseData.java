package com.monte.mar.model.data;

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
    FirebaseDatabase database;
    String name;
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();

    // Creamos la funcion para guardar los datos
    public String getDataUser(Context context, EventListener<DocumentSnapshot> listener) {
        final String[] apellidos = new String[1];
        try {
            // Llamamos a la funcion para definir firebase
            definingFirebase();
            // Creamos una nueva collecion en firebase de nombre "usuario"
            DocumentReference documentReference = firebaseFirestore.collection("usuario").document(idUser);
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
                // Recuperamos nombre
                name = value.getString("nombres");
                sweetAlertDialog.sweetAlertSuccessRegistro(context, name);

            }
        });
    }

    private void definingFirebase(){
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
    }

}