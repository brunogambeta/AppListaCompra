package com.brunogambeta.applistacompra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.brunogambeta.applistacompra.model.ListaDeCompra;
import com.brunogambeta.applistacompra.model.Usuario;

public class AdicionarListaActivity extends AppCompatActivity {

    private EditText  descricaoAdcionarListaCompra;
    private Button botaoAdicionarLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_lista);

        inicializarComponentes();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nova Lista");
        toolbar.setSubtitle(UsuarioFirebase.getDateTime());
        toolbar.setSubtitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Funcão do botao adicionar nova lista
        botaoAdicionarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descricao = descricaoAdcionarListaCompra.getText().toString();
                if (!descricao.isEmpty()) {
                    ListaDeCompra lista = new ListaDeCompra();
                    lista.setDescricao(descricao);
                    lista.salvarNovaLista();
                    exibirMensagem("Lista adicionada com sucesso!");
                    finish();
                }else{
                    exibirMensagem("Descricao da lista não preenchido");
                }
            }
        });
    }

    //Metodo para inicializar os componentes
    private void inicializarComponentes() {
        descricaoAdcionarListaCompra = findViewById(R.id.editDescricaoAdicionarLista);
        botaoAdicionarLista = findViewById(R.id.buttonAdicionarLista);
    }

    //Metodo para exibir mensagem de erro
    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}