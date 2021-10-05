package com.brunogambeta.applistacompra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class InformacaoActivity extends AppCompatActivity {

    private AdView adViewTelaInformacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao);

        //Configurações iniciais
        inicializarComponentes();
        carregarAnuncio();


        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Como Utilizar o Aplicativo");
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(UsuarioFirebase.getDateTime());
        toolbar.setSubtitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Metodo para carregar o anuncio na tela
    private void carregarAnuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adViewTelaInformacao.loadAd(adRequest);
    }

    private void inicializarComponentes(){
        adViewTelaInformacao = findViewById(R.id.adviewTelaInformacao);
    }

}