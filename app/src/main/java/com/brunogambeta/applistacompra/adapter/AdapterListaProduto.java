package com.brunogambeta.applistacompra.adapter;
/*
 * Created by Bruno Gambeta on 20/04/2021.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brunogambeta.applistacompra.R;
import com.brunogambeta.applistacompra.model.Produto;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterListaProduto extends RecyclerView.Adapter<AdapterListaProduto.MyViewHolder> {

    private List<Produto> listaProdutos;

    public AdapterListaProduto(List<Produto> produtos) {
        this.listaProdutos = produtos;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produtos, parent, false);
        return new AdapterListaProduto.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto produtos = listaProdutos.get(position);
        holder.descricaoProduto.setText("Nome Produto: "+produtos.getNome());
        holder.marcaProduto.setText("Marca: "+produtos.getMarca());
        holder.qtdProduto.setText("Quantidade : " + produtos.getQuantidade());
        holder.comprado.isChecked();

    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricaoProduto;
        TextView marcaProduto;
        TextView qtdProduto;
        CheckBox comprado;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricaoProduto = itemView.findViewById(R.id.textDescricaoProduto);
            marcaProduto = itemView.findViewById(R.id.textMarcaProduto);
            qtdProduto = itemView.findViewById(R.id.textQuantidadeProduto);
            comprado = itemView.findViewById(R.id.checkBoxComprado);


        }
    }
}
