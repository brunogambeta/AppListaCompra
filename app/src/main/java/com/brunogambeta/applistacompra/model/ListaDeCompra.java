package com.brunogambeta.applistacompra.model;
/*
 * Created by Bruno Gambeta on 19/04/2021.
 */

import android.provider.ContactsContract;

import com.brunogambeta.applistacompra.helper.ConfiguracaoFirebase;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class ListaDeCompra implements Serializable {

    private String idListaDeCompra;
    private String descricao;


    public ListaDeCompra() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase()
                .child("minhaLista");
        setIdListaDeCompra(firebaseRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference listaCompraRef = firebaseRef
                .child("listaCompra")
                .child(getIdListaDeCompra());
        listaCompraRef.setValue(this);
    }

    public void salvarNovaLista() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference listaCompraRef = firebaseRef.child("usuarios").child(UsuarioFirebase.getIdUsuario())
                .child("listaCompra")
                .child(getIdListaDeCompra());
        listaCompraRef.setValue(this);


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
