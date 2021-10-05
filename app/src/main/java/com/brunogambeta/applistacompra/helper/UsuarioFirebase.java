package com.brunogambeta.applistacompra.helper;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bruno Gambeta
 */

public class UsuarioFirebase {

    public static String getIdUsuario(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return autenticacao.getCurrentUser().getUid();

    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarTipoUsuario(String tipo){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(tipo)
                    .build();
            user.updateProfile(profile);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
