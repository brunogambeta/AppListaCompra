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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProdutosActivity extends AppCompatActivity {

    private ListaDeCompra listaSelecionada;
    private RecyclerView recyclerViewProdutos;
    private List<Produto> listaProdutos = new ArrayList<>();
    private AdapterListaProduto adapterListaProduto;
    private DatabaseReference listaProdutosRef;
    private DatabaseReference firebaseRef;

    private Produto produto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        swipe();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Produtos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaSelecionada = (ListaDeCompra) getIntent().getSerializableExtra("listaSelecionada");

        //Configurar adapter
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProdutos.setHasFixedSize(true);
        adapterListaProduto = new AdapterListaProduto(listaProdutos);
        recyclerViewProdutos.setAdapter(adapterListaProduto);


        //Recuperar Produtos
        recuperarListaProdutos();
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
            case R.id.adicionar_produto:
                abrirTelaProduto();
                break;
            case R.id.compartilhar:
                compartilharLista();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaProduto() {
        Intent i = new Intent(ProdutosActivity.this, AdicionarProdutosActivity.class);
        i.putExtra("listaSelecionada", listaSelecionada);
        startActivity(i);
    }

    private void inicializarComponentes() {

        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
    }

    private void recuperarListaProdutos() {

        listaProdutosRef = firebaseRef.child("listaCompra").child(listaSelecionada.getIdListaDeCompra()).child("produtos");
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
                        .child(listaSelecionada.getIdListaDeCompra())
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
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
    }

    private void excluirProdutoUsuario(RecyclerView.ViewHolder viewHolder) {
        DatabaseReference listaUsuarioRef = firebaseRef
                .child("usuarios")
                .child(UsuarioFirebase.getIdUsuario())
                .child("listaCompra")
                .child(listaSelecionada.getIdListaDeCompra())
                .child("produtos");
        ;
        int position = viewHolder.getAdapterPosition();
        produto = listaProdutos.get(position);
        listaUsuarioRef.child(produto.getIdProduto()).removeValue();
        adapterListaProduto.notifyItemRemoved(position);

    }

    private void compartilharLista() {
        String descricao = "Compartilhando Lista de Compra\n" +
                            "Use o aplicativo APPListaCompra visualizar a lista\n" +
                            "ID: ";
        String idListaCompra = listaSelecionada.getIdListaDeCompra();

        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND_MULTIPLE);
        i.putExtra(Intent.EXTRA_TEXT, descricao + "\n"+ idListaCompra);

        i.setType("text/*");
        Intent share = Intent.createChooser(i, "Compartilhando Lista da Compra");
        startActivity(i);
    }
}