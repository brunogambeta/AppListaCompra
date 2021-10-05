package com.brunogambeta.applistacompra.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.brunogambeta.applistacompra.BuildConfig;
import com.brunogambeta.applistacompra.R;

public class SobreActivity extends AppCompatActivity {

    private TextView textVersao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        //Inicializa os Componentes
        inicializarComponentes();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sobre");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void inicializarComponentes(){

        textVersao = findViewById(R.id.textView4);
        String versao = ".";
        versao = BuildConfig.VERSION_NAME;
        textVersao.setText("Versão: "+versao);
    }
}