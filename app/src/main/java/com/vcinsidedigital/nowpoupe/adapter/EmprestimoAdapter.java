package com.vcinsidedigital.nowpoupe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.vcinsidedigital.nowpoupe.R;
import com.vcinsidedigital.nowpoupe.activity.AddEditActivity;
import com.vcinsidedigital.nowpoupe.activity.MainActivity;
import com.vcinsidedigital.nowpoupe.helper.EmprestimoDAO;
import com.vcinsidedigital.nowpoupe.model.Emprestimo;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;


public class EmprestimoAdapter extends RecyclerView.Adapter<EmprestimoAdapter.MyViewHolder> {

    private List<Emprestimo> listaEmprestimo;
    private Context context;
    private MainActivity activity;

    public EmprestimoAdapter(List<Emprestimo> lista, Context context, MainActivity activity) {
        this.listaEmprestimo = lista;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_emprestimo_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Emprestimo emprestimo = this.listaEmprestimo.get(position);
        holder.cliente.setText(emprestimo.getCliente());
        holder.data.setText("Data: " + emprestimo.getData());
        holder.valor.setText("Valor: "+ NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(emprestimo.getValor()).replace("R$", "R$ "));
        holder.taxa.setText("Taxa: "+ NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(emprestimo.getTaxa()).replace("R$", "R$ "));
        holder.valor_total.setText("Total a Pagar: "+ NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(emprestimo.getTotalAPagar()).replace("R$", "R$ "));

    }

    @Override
    public int getItemCount() {
        return this.listaEmprestimo.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cliente, data, valor, taxa, valor_total;

        public MyViewHolder(View itemView) {
            super(itemView);
            cliente = itemView.findViewById(R.id.clienteId);
            data = itemView.findViewById(R.id.dataTextId);
            valor = itemView.findViewById(R.id.valorId);
            taxa = itemView.findViewById(R.id.taxaId);
            valor_total = itemView.findViewById(R.id.valorTotalId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Emprestimo emprestimo = listaEmprestimo.get(getAdapterPosition());
                    Intent i = new Intent(context, AddEditActivity.class);
                    i.putExtra("emprestimoAtual", emprestimo);
                    context.startActivity(i);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Emprestimo emprestimo = listaEmprestimo.get(getAdapterPosition());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Deletar Emprestimo");
                    dialog.setMessage("Tem certeza que deseja deletar o emprestimo do cliente " + emprestimo.getCliente() + " ?" );
                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EmprestimoDAO dao = new EmprestimoDAO(context.getApplicationContext());
                            if(dao.deletar(emprestimo)){
                                Toast.makeText(context, "Emprestimo deletado com sucesso.", Toast.LENGTH_LONG).show();
                                activity.carregarListaDeEmprestimos();
                            }else{
                                Toast.makeText(context, "Ocorreu um erro ao deletar emprestimo.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        }
    }

}
