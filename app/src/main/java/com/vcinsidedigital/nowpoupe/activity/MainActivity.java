package com.vcinsidedigital.nowpoupe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vcinsidedigital.nowpoupe.R;
import com.vcinsidedigital.nowpoupe.adapter.EmprestimoAdapter;
import com.vcinsidedigital.nowpoupe.databinding.ActivityMainBinding;
import com.vcinsidedigital.nowpoupe.helper.EmprestimoDAO;
import com.vcinsidedigital.nowpoupe.model.Emprestimo;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private EmprestimoAdapter emprestimoAdapter;
    private List<Emprestimo> listaEmprestimos = new ArrayList<Emprestimo>();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddEditActivity.class);
                startActivity(i);
            }
        });




        getApplicationContext();

    }

    @Override
    protected void onStart() {
        carregarListaDeEmprestimos();
        super.onStart();
    }

    public void carregarListaDeEmprestimos(){
        //Listar Emprestimos

        EmprestimoDAO db = new EmprestimoDAO(getApplicationContext());

        listaEmprestimos = db.listar();


        //Exibir no RecyclerView
        //Configurar Adapter
        emprestimoAdapter = new EmprestimoAdapter(listaEmprestimos, MainActivity.this, MainActivity.this);
        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(emprestimoAdapter);
        /*recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Curto", Toast.LENGTH_LONG).show();
                Emprestimo emprestimo = listaEmprestimos.get(position);
                Intent i = new Intent(MainActivity.this, AddEditActivity.class);
                i.putExtra("emprestimoAtual", emprestimo);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "Long Click", Toast.LENGTH_LONG).show();
                Emprestimo emprestimo = listaEmprestimos.get(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Deletar Emprestimo");
                dialog.setMessage("Tem certeza que deseja deletar o emprestimo do cliente " + emprestimo.getCliente() + " ?" );
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EmprestimoDAO dao = new EmprestimoDAO(getApplicationContext());
                        if(dao.deletar(emprestimo)){
                            Toast.makeText(MainActivity.this, "Emprestimo deletado com sucesso.", Toast.LENGTH_LONG).show();
                            carregarListaDeEmprestimos();
                        }else{
                            Toast.makeText(MainActivity.this, "Ocorreu um erro ao deletar emprestimo.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(EmprestimoDAO.deleteDatabse(getApplicationContext())) {
                finish();
                Intent i = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(i);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}