package com.brunogambeta.applistacompra.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.adapter.AdapterListaDeCompra;
import com.brunogambeta.applistacompra.helper.ConfiguracaoFirebase;
import com.brunogambeta.applistacompra.helper.RecyclerItemClickListener;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.brunogambeta.applistacompra.model.ListaDeCompra;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TelaPrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private AdapterListaDeCompra adapterListaDeCompra;
    private List<ListaDeCompra> listaCompras = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private DatabaseReference listaComprasRef;
    private RecyclerView recyclerView;
    private ListaDeCompra listaDeCompra;
    private FloatingActionButton fab;

    private AdView adViewTelaPrincipal;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        //Configuracoes iniciais
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        swipe();
        carregarAnuncio();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Listas Cadastradas");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setSubtitle(UsuarioFirebase.getDateTime());
        toolbar.setSubtitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //Configuracoes RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterListaDeCompra = new AdapterListaDeCompra(listaCompras);
        recyclerView.setAdapter(adapterListaDeCompra);

        //RecuperarListaDeCompras
        recuperarListaCompras();

        //Aplicar evento de clique
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ListaDeCompra listaSelecionada = listaCompras.get(position);
                Intent i = new Intent(TelaPrincipalActivity.this, ProdutosActivity.class);
                String lista = listaSelecionada.getIdListaDeCompra();
                i.putExtra("listaSelecionada", lista);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));



     fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            abrirTelaAdicionarLista();
        }
    });
}

    //Metodo de configuracoes iniciais
    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.recyclerViewHome);
        adViewTelaPrincipal = findViewById(R.id.adViewTelaPrincipal);
        fab = findViewById(R.id.fabNovaLista);

    }

    //Metodo para carregar o anuncio na tela
    private void carregarAnuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adViewTelaPrincipal.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sair:
                deslogarUsuario();
                break;
            case R.id.sobre_app:
                abrirTelaSobre();
                break;
            case R.id.informacao:
                abrirTelaInformacao();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo para chamar a tela de Informações
    private void abrirTelaInformacao() {
        Intent i = new Intent(TelaPrincipalActivity.this, InformacaoActivity.class);
        startActivity(i);
    }

    //Metodo para chamar a tela de adicionar lista
    private void abrirTelaAdicionarLista() {
        Intent i = new Intent(TelaPrincipalActivity.this, AdicionarListaActivity.class);
        startActivity(i);
    }


    //Metodo para deslogar o usuario
    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo para abrir a tela de criar uma nova lista
    private void abrirtelaNovaLista() {
        Intent i = new Intent(TelaPrincipalActivity.this, NovaListaCompraActivity.class);
        startActivity(i);
    }

    //Metodo para abrir a tela de sobre do app
    private void abrirTelaSobre() {
        Intent i = new Intent(TelaPrincipalActivity.this, SobreActivity.class);
        startActivity(i);
    }

    //Metodo para recuperar lista de compras
    private void recuperarListaCompras() {
        DatabaseReference listaCompraRef = firebaseRef.child("usuarios").child(UsuarioFirebase.getIdUsuario()).child("listaCompra");
        listaCompraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCompras.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    listaCompras.add(ds.getValue(ListaDeCompra.class));
                }
                adapterListaDeCompra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Metodo com a funcao de remover as listas
    private void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirListaCompra(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    //Metodo para excluir uma lista de compra
    private void excluirListaCompra(RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Lista de Compra");
        alertDialog.setMessage("Você tem certeza que deseja excluir está lista?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                listaDeCompra = listaCompras.get(position);
                listaComprasRef = firebaseRef.child("listaCompra");
                listaComprasRef.child(listaDeCompra.getIdListaDeCompra()).removeValue();
                excluirListaUsuario(viewHolder);
                adapterListaDeCompra.notifyItemRemoved(position);

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exibirMensagem("Operação cancelada");
                adapterListaDeCompra.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    //Metodo para exibir mensagens
    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    //Metodo para excluir a lista dentro do usuario
    private void excluirListaUsuario(RecyclerView.ViewHolder viewHolder) {
        DatabaseReference listaUsuarioRef = firebaseRef.child("usuarios").child(UsuarioFirebase.getIdUsuario()).child("listaCompra");
        int position = viewHolder.getAdapterPosition();
        listaDeCompra = listaCompras.get(position);
        listaUsuarioRef.child(listaDeCompra.getIdListaDeCompra()).removeValue();
        adapterListaDeCompra.notifyItemRemoved(position);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("Restart", "voltou a vida");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Resume", "voltou a vida");
    }
}