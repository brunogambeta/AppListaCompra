package com.brunogambeta.applistacompra.adapter;
/*
 * Created by Bruno Gambeta on 19/04/2021.
 */

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.model.ListaDeCompra;

import java.util.List; 

public class AdapterListaDeCompra extends RecyclerView.Adapter<AdapterListaDeCompra.MyViewHolder> {

    private final List<ListaDeCompra> listaDeCompras;

    public AdapterListaDeCompra(List<ListaDeCompra> compras) {
        this.listaDeCompras = compras;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_compra, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListaDeCompra listaDeCompra = listaDeCompras.get(position);
        holder.descricao.setText(listaDeCompra.getDescricao());

    }

    @Override
    public int getItemCount() {
        return listaDeCompras.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView descricao;
        TextView idListaCompra;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.textDescricaoLista);
            //idListaCompra = itemView.findViewById(R.id.editIdListaCompra);


        }
    }
}
