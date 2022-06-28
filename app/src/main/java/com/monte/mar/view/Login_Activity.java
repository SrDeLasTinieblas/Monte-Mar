package com.monte.mar.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.monte.mar.model.SweetAlertDialog;

import monte.montemar.R;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog();
    Button btnStartSession;
    TextView tvUsuario, tvContrasena;
    Button btnSignOff;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        main();
    }
    private void main(){
        lottieAnimationView();
        setPreferences();
        definingComponents();
        definitingFirebase();
        validatingLogin();
    }

    public void startSession(View view){
        try {
            // Validando usuario y contraseña
            if (awesomeValidation.validate()) {
                String mail = tvUsuario.getText().toString();
                String pass = tvContrasena.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                irHome();
                            } else {
                                //String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                sweetAlertDialog.sweetAlertError(Login_Activity.this);
                            }
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Toast.makeText(Login_Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void validatingLogin() {
        FirebaseAuth mAut = FirebaseAuth.getInstance();
        FirebaseUser user = mAut.getCurrentUser();
        if (user != null) {
            irHome();
        }
    }

    private void definingComponents(){
        btnStartSession = findViewById(R.id.btnStartSession);
        tvUsuario = findViewById(R.id.registroEmail);
        tvContrasena = findViewById(R.id.registroContraseña);
        btnSignOff = findViewById(R.id.btnRegistrar);
    }

    private void definitingFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.registroEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.registroContraseña, ".{6,}", R.string.invalid_password);
    }

    private void setPreferences(){
        sharedPreferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void irHome(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("email", tvUsuario.getText().toString());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    @Override
    public void onClick(View view) {
        //clearData();
    }

    private void lottieAnimationView(){
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animateView);
        animationView.playAnimation();
    }

    public void test(View view){
        Toast.makeText(this, "Funciona supongo... u.u", Toast.LENGTH_SHORT).show();
    }

    public void Registrarse(View view){
        Intent i = new Intent(Login_Activity.this, Registrar_activity.class);
        startActivity(i);
    }

    /*public void setAumentar(View view){
        amount.setText(String.valueOf(++valor));
    }

    public void setDisminuir(View view){
        amount.setText(String.valueOf(--valor));
    }*/


}























