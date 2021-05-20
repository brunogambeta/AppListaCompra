package com.brunogambeta.applistacompra.model;
/*
 * Created by Bruno Gambeta on 20/04/2021.
 */

import com.brunogambeta.applistacompra.helper.ConfiguracaoFirebase;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

public class Produto {

    private String idListaCompra;
    private String idProduto;
    private String nome;
    private String marca;
    private String quantidade;
    private String statusProduto;


    public Produto() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos");
        setIdProduto(produtoRef.push().getKey());
    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("listaCompra")
                .child(getIdListaCompra())
                .child("produtos")
                .child(getIdProduto());
        produtoRef.setValue(this);
        salvarNoUsuario();

    }
    public void salvarNoUsuario() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("usuarios")
                .child(UsuarioFirebase.getIdUsuario())
                .child("listaCompra")
                .child(getIdListaCompra())
                .child("produtos")
                .child(getIdProduto());
        produtoRef.setValue(this);

    }

    public String getStatusProduto() {
        return statusProduto;
    }

    public void setStatusProduto(String statusProduto) {
        this.statusProduto = statusProduto;
    }

    public void remover() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(getIdProduto());
        produtoRef.removeValue();
    }

    public String getIdListaCompra() {
        return idListaCompra;
    }

    public void setIdListaCompra(String idListaCompra) {
        this.idListaCompra = idListaCompra;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}