package com.vcinsidedigital.nowpoupe.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.vcinsidedigital.nowpoupe.R;
import com.vcinsidedigital.nowpoupe.helper.EmprestimoDAO;
import com.vcinsidedigital.nowpoupe.model.Emprestimo;

public class AddEditActivity extends AppCompatActivity
{

    private TextInputEditText clienteText, dataText, valorText, taxaText, totalAPagarText;
    private Emprestimo emprestimoAtual;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);


        toolbar = findViewById(R.id.toolbar_add_edit);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emprestimoAtual = (Emprestimo) getIntent().getSerializableExtra("emprestimoAtual");

        clienteText = findViewById(R.id.clienteText);
        dataText = findViewById(R.id.dataText);
        valorText = findViewById(R.id.valorText);
        taxaText = findViewById(R.id.textTaxa);
        totalAPagarText = findViewById(R.id.totalAPagarText);

        if(emprestimoAtual != null){
            clienteText.setText(emprestimoAtual.getCliente());
            dataText.setText(emprestimoAtual.getData());
            valorText.setText(Double.toString(emprestimoAtual.getValor()));
            taxaText.setText(Double.toString(emprestimoAtual.getTaxa()));
            totalAPagarText.setText(Double.toString(emprestimoAtual.getTotalAPagar()));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        EmprestimoDAO dao = new EmprestimoDAO(getApplicationContext());
        switch (item.getItemId()){
            case R.id.confirm:
                if(emprestimoAtual != null){
                    String textoCliente = clienteText.getText().toString();
                    String textoData = dataText.getText().toString();
                    String textoValor = valorText.getText().toString();
                    String textoTaxa = taxaText.getText().toString();
                    String textoTotalAPagar = totalAPagarText.getText().toString();
                    if(!textoCliente.isEmpty() || !textoData.isEmpty() || !textoValor.isEmpty() || !textoTaxa.isEmpty() || !textoTotalAPagar.isEmpty()){
                        Emprestimo emprestimo = new Emprestimo();
                        emprestimo.setId(emprestimoAtual.getId());
                        emprestimo.setCliente(textoCliente);
                        emprestimo.setData(textoData);
                        emprestimo.setValor(Double.parseDouble(textoValor));
                        emprestimo.setTaxa(Double.parseDouble(textoTaxa));
                        emprestimo.setTotalAPagar(Double.parseDouble(textoTotalAPagar));

                        if(dao.atualizar(emprestimo)){
                            finish();
                            Toast.makeText(this, "Emprestimo Salvo com Sucesso.", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(this, "Ocorreu um erro ao atualizar o emprestimo.", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this, "Você precisa preencher todos campos.", Toast.LENGTH_LONG).show();
                    }
                }else{

                    String textoCliente = clienteText.getText().toString();
                    String textoData = dataText.getText().toString();
                    String textoValor = valorText.getText().toString();
                    String textoTaxa = taxaText.getText().toString();
                    String textoTotalAPagar = totalAPagarText.getText().toString();

                    if(!textoCliente.isEmpty() || !textoData.isEmpty() || !textoValor.isEmpty() || !textoTaxa.isEmpty() || !textoTotalAPagar.isEmpty()){
                        Emprestimo emprestimo = new Emprestimo();
                        emprestimo.setCliente(textoCliente);
                        emprestimo.setData(textoData);
                        emprestimo.setValor(Double.parseDouble(textoValor));
                        emprestimo.setTaxa(Double.parseDouble(textoTaxa));
                        emprestimo.setTotalAPagar(Double.parseDouble(textoTotalAPagar));
                        if(dao.salvar(emprestimo)){
                            Toast.makeText(this, "Emprestimo Salvo com Sucesso.", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(this, "Ocorreu um erro ao salvar o emprestimo.", Toast.LENGTH_LONG).show();
                        }


                    }else {
                        Toast.makeText(this, "Você precisa preencher todos campos.", Toast.LENGTH_LONG).show();
                    }

                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
