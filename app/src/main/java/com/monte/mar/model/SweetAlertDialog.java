package com.monte.mar.model;

import android.content.Context;
import android.graphics.Color;

public class SweetAlertDialog {


    public void sweetAlertLoading(Context context){
        com.cazaea.sweetalert.SweetAlertDialog pDialog = new com.cazaea.sweetalert.SweetAlertDialog(context, com.cazaea.sweetalert.SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void sweetAlertSuccessBuy(Context context){
        new com.cazaea.sweetalert.SweetAlertDialog(context, com.cazaea.sweetalert.SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Envio exitoso")
                .setContentText("Gracias por su confianza! " +
                        "Nos pondremos en contacto con usted mediante Email.")
                .show();
    }

    public void sweetAlertSuccessRegistro(Context context,String usuario){
        new com.cazaea.sweetalert.SweetAlertDialog(context, com.cazaea.sweetalert.SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Welcome " + usuario + "!")
                .setContentText("Estamos agradecidos por su confianza.")
                .show();
    }

    public void sweetAlertError(Context context){
        new com.cazaea.sweetalert.SweetAlertDialog(context, com.cazaea.sweetalert.SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Email y/o Password incorrecto!.")
                .setConfirmText("OK")
                .show();
    }
}

