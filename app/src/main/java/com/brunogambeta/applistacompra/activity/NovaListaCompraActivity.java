package com.brunogambeta.applistacompra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.helper.ConfiguracaoFirebase;
import com.brunogambeta.applistacompra.model.ListaDeCompra;
import com.brunogambeta.applistacompra.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class NovaListaCompraActivity extends AppCompatActivity {

    private EditText descricaoListaCompra;
    private Button buttonSalvarLista;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_lista_compra);

        //Configuracoes iniciais
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nova Lista de Compras");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSalvarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descricao = descricaoListaCompra.getText().toString();

                if (!descricao.isEmpty()){
                    ListaDeCompra listaDeCompra = new ListaDeCompra();
                    Usuario usuario = new Usuario();
                    listaDeCompra.setDescricao(descricao);
                    listaDeCompra.salvar();
                    usuario.setIdListaDeCompra(listaDeCompra.getIdListaDeCompra());
                    usuario.setDescricao(listaDeCompra.getDescricao());
                    usuario.salvar();
                    exibirMensagem("Lista adicionada com sucesso!");
                    finish();

                }
            }
        });
    }

    private void inicializarComponentes(){
        descricaoListaCompra = findViewById(R.id.editDescricaoListaCompra);
        buttonSalvarLista = findViewById(R.id.byttonSalvarLista);
    }
    private void exibirMensagem(String texto){
        Toast.makeText(this,texto, Toast.LENGTH_SHORT).show();
    }
}