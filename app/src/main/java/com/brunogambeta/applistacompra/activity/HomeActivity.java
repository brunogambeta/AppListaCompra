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
import android.widget.AdapterView;
import android.widget.Toast;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.adapter.AdapterListaDeCompra;
import com.brunogambeta.applistacompra.helper.ConfiguracaoFirebase;
import com.brunogambeta.applistacompra.helper.RecyclerItemClickListener;
import com.brunogambeta.applistacompra.helper.UsuarioFirebase;
import com.brunogambeta.applistacompra.model.ListaDeCompra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private AdapterListaDeCompra adapterListaDeCompra;
    private List<ListaDeCompra> listaCompras = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private DatabaseReference listaComprasRef;
    private RecyclerView recyclerView;
    private ListaDeCompra listaDeCompra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Configuracoes iniciais
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        swipe();


        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(Color.BLACK);
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

            }

            @Override
            public void onLongItemClick(View view, int position) {
                ListaDeCompra listaSelecionada = listaCompras.get(position);
                Intent i = new Intent(HomeActivity.this, ProdutosActivity.class);
                i.putExtra("listaSelecionada", listaSelecionada);
                Log.i("Lista", listaSelecionada.getIdListaDeCompra());
                startActivity(i);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }

    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.recyclerViewHome);

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
            case R.id.adicionar_Lista:
                abrirTelaAdicionarLista();
                break;
            case R.id.sobre_app:
                abrirTelaSobre();
                break;
            case R.id.adicionar_nova_lista:
                abrirtelaNovaLista();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaAdicionarLista() {
        Intent i = new Intent(HomeActivity.this, AdicionarListaActivity.class);
        startActivity(i);
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirtelaNovaLista() {
        Intent i = new Intent(HomeActivity.this, NovaListaCompraActivity.class);
        startActivity(i);
    }

    private void abrirTelaSobre(){
        Intent i = new Intent(HomeActivity.this, SobreActivity.class);
        startActivity(i);
    }

    private void recuperarListaCompras() {
        DatabaseReference listaCompraRef = firebaseRef.child("usuarios").child(UsuarioFirebase.getIdUsuario()).child("listaCompra");
        listaCompraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCompras.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    listaCompras.add(ds.getValue(ListaDeCompra.class));
                }
                adapterListaDeCompra.notifyDataSetChanged();
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
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    private void excluirProduto(RecyclerView.ViewHolder viewHolder) {

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
                exibirMensagem("Cancelado");
                adapterListaDeCompra.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
    }

    private void excluirListaUsuario(RecyclerView.ViewHolder viewHolder){
        DatabaseReference listaUsuarioRef = firebaseRef.child("usuarios").child(UsuarioFirebase.getIdUsuario()).child("listaCompra");
        int position = viewHolder.getAdapterPosition();
        listaDeCompra = listaCompras.get(position);
        listaUsuarioRef.child(listaDeCompra.getIdListaDeCompra()).removeValue();
        adapterListaDeCompra.notifyItemRemoved(position);

    }

}