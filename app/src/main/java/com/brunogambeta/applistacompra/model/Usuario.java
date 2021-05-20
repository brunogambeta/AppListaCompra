package com.brunogambeta.applistacompra.model;
/*
 * Created by Bruno Gambeta on 21/04/2021.
 */

import android.media.DrmInitData;

import com.brunogambeta.applistacompra.helper.ConfiguracaoFirebase;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

public class Usuario {

    private String idListaDeCompra;
    private String descricao;


    public Usuario() {

    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                .child(UsuarioFirebase.getIdUsuario())
                .child("listaCompra")
                .child(getIdListaDeCompra());
        usuarioRef.setValue(this);
    }


    public String getIdListaDeCompra() {
        return idListaDeCompra;
    }

    public void setIdListaDeCompra(String idListaDeCompra) {
        this.idListaDeCompra = idListaDeCompra;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}


