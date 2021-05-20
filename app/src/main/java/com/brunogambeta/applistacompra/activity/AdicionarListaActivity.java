package com.brunogambeta.applistacompra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.model.ListaDeCompra;
import com.brunogambeta.applistacompra.model.Usuario;

public class AdicionarListaActivity extends AppCompatActivity {

    private EditText idListaCompra, descricaoAdcionarListaCompra;
    private Button botaoAdicionarLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_lista);

        inicializarComponentes();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar Lista Existente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        botaoAdicionarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idLista = idListaCompra.getText().toString();
                String descricao = descricaoAdcionarListaCompra.getText().toString();
                if (!descricao.isEmpty()) {
                    if (!idLista.isEmpty()) {
                        ListaDeCompra lista = new ListaDeCompra();
                        lista.setIdListaDeCompra(idLista);
                        lista.setDescricao(descricao);
                        lista.salvarNovaLista();
                        exibirMensagem("Lista adicionada com sucesso!");
                        finish();
                    } else {
                        exibirMensagem("ID da lista não preenchido");
                    }
                }else{
                    exibirMensagem("Descricao da lista não preenchido");
                }
            }
        });
    }

    private void inicializarComponentes() {

        idListaCompra = findViewById(R.id.editIdListaCompra);
        descricaoAdcionarListaCompra = findViewById(R.id.editDescricaoAdicionarLista);
        botaoAdicionarLista = findViewById(R.id.buttonAdicionarLista);
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
    }
}