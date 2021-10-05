package com.brunogambeta.applistacompra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.brunogambeta.applistacompra.model.ListaDeCompra;
import com.brunogambeta.applistacompra.model.Produto;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdicionarProdutosActivity extends AppCompatActivity {

    private EditText editNomeProduto, editQuantidadeProduto;
    private Button buttonSalvarProduto;
    private String listaSelecionada;
    private AdView adViewAdicionarProduto;

    private String idListaCompra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_produtos);

        inicializarComponentes();
        carregarAnuncio();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        toolbar.setSubtitle(UsuarioFirebase.getDateTime());
        toolbar.setSubtitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);


        listaSelecionada = (String) getIntent().getSerializableExtra("listaSelecionada");
        if (listaSelecionada != null) {

            idListaCompra = listaSelecionada;
            buttonSalvarProduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomeProduto = editNomeProduto.getText().toString();
                    String qtdProduto = editQuantidadeProduto.getText().toString();

                    if (!nomeProduto.isEmpty()) {
                        if (!qtdProduto.isEmpty()) {
                            Produto produto = new Produto();
                            produto.setIdListaCompra(idListaCompra);
                            produto.setNome(nomeProduto);
                            produto.setQuantidade(qtdProduto);
                            produto.setStatusProduto("p");
                            produto.salvar();
                            exibirMensagem("Produto salvo com sucesso!");
                            finish();
                        } else {
                            exibirMensagem("Quantidade do produto não informada!");
                        }
                    } else {
                        exibirMensagem("Nome do Produto não informado!");
                    }

                }
            });
        }
    }

    private void inicializarComponentes() {

        editNomeProduto = findViewById(R.id.editNomeProdutoCadastro);
        editQuantidadeProduto = findViewById(R.id.editQuantidadeProdutoCadastro);
        buttonSalvarProduto = findViewById(R.id.buttonSalvarProduto);
        adViewAdicionarProduto = findViewById(R.id.adViewTelaAdicionarProdutos);
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    //Metodo para carregar o anuncio na tela
    private void carregarAnuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adViewAdicionarProduto.loadAd(adRequest);
    }



}