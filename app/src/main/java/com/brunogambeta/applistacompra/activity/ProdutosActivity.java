package com.brunogambeta.applistacompra.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.adapter.AdapterListaProduto;
import com.brunogambeta.applistacompra.helper.ConfiguracaoFirebase;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.brunogambeta.applistacompra.model.ListaDeCompra;
import com.brunogambeta.applistacompra.model.Produto;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProdutosActivity extends AppCompatActivity {

    private String listaSelecionada;
    private RecyclerView recyclerViewProdutos;
    private List<Produto> listaProdutos = new ArrayList<>();
    private AdapterListaProduto adapterListaProduto;
    private DatabaseReference listaProdutosRef;
    private DatabaseReference firebaseRef;
    private FloatingActionButton fabProdutos;
    private AdView adViewProdutos;

    private Produto produto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        swipe();
        carregarAnuncio();
        recuperarIdLista();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Produtos");
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(UsuarioFirebase.getDateTime());
        toolbar.setSubtitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurar adapter
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProdutos.setHasFixedSize(true);
        adapterListaProduto = new AdapterListaProduto(listaProdutos);
        recyclerViewProdutos.setAdapter(adapterListaProduto);

        //Recuperar Produtos
        recuperarListaProdutos();

        fabProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaProduto();
            }
        });
    }

    private void recuperarIdLista() {
        listaSelecionada = (String) getIntent().getSerializableExtra("listaSelecionada");
        String idLista = "";
        Log.i("RecuperarLista", listaSelecionada +" / "+ idLista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_produtos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.compartilhar:
                //compartilharLista();
                exibirMensagem("Função não implementada !");
                break;
            }
        switch (item.getItemId()) {
            case R.id.finalizarLista:
                finalizarLista();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo para carregar o anuncio na tela
    private void carregarAnuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adViewProdutos.loadAd(adRequest);
    }

    private void abrirTelaProduto() {
        Intent i = new Intent(ProdutosActivity.this, AdicionarProdutosActivity.class);
        i.putExtra("listaSelecionada", listaSelecionada);
        startActivity(i);
    }

    private void inicializarComponentes() {

        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        fabProdutos = findViewById(R.id.fabProdutos);
        adViewProdutos = findViewById(R.id.adViewTelaProdutos);
    }

    private void recuperarListaProdutos() {
        listaProdutosRef = firebaseRef.child("listaCompra").child(listaSelecionada).child("produtos");
        listaProdutosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaProdutos.clear();
                for (DataSnapshot lista : snapshot.getChildren()) {
                    listaProdutos.add(lista.getValue(Produto.class));

                }

                Collections.reverse(listaProdutos);
                adapterListaProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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
                excluirProduto(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewProdutos);
    }

    private void excluirProduto(RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Produto");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir este produto?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                produto = listaProdutos.get(position);
                listaProdutosRef = firebaseRef.child("listaCompra")
                        .child(listaSelecionada)
                        .child("produtos");
                listaProdutosRef.child(produto.getIdProduto()).removeValue();
                excluirProdutoUsuario(viewHolder);
                adapterListaProduto.notifyItemRemoved(position);

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exibirMensagem("Cancelado");
                adapterListaProduto.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void excluirProdutoUsuario(RecyclerView.ViewHolder viewHolder) {
        DatabaseReference listaUsuarioRef = firebaseRef
                .child("usuarios")
                .child(UsuarioFirebase.getIdUsuario())
                .child("listaCompra")
                .child(listaSelecionada)
                .child("produtos");
        ;
        int position = viewHolder.getAdapterPosition();
        produto = listaProdutos.get(position);
        listaUsuarioRef.child(produto.getIdProduto()).removeValue();
        adapterListaProduto.notifyItemRemoved(position);

    }

    private void compartilharLista() {


        String urlApp = "https://play.google.com/store/apps/details?id=com.brunogambeta.contadorjogatinar";
        String descricao = "Compartilhando Lista de Compra\n\n" +
                            "Use o aplicativo APPListaCompra para visualizar a lista\n\n"+
                            urlApp;
        String idListaCompra = listaSelecionada;

        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, descricao);
        i.setType("text/*");
        Intent share = Intent.createChooser(i, "Compartilhando Lista da Compra");
        startActivity(share);
    }

    private void finalizarLista(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Finalizar lista !");
        alertDialog.setMessage("Você confirma a finalização da lista?\n" +
                "Após finalizar, não será possivel fazer alterações.");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference listaUsuarioRef = firebaseRef
                        .child("listaCompra");
                listaUsuarioRef.child(listaSelecionada).removeValue();
                finalizarListaUsuario();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exibirMensagem("Operação cancelada");
                adapterListaProduto.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();

    }
    private void finalizarListaUsuario(){
        DatabaseReference listaUsuarioRef = firebaseRef
                .child("usuarios")
                .child(UsuarioFirebase.getIdUsuario())
                .child("listaCompra");
        listaUsuarioRef.child(listaSelecionada).removeValue();
        finish();
    }

}