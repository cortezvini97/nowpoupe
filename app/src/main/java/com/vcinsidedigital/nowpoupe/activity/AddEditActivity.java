package com.vcinsidedigital.nowpoupe.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vcinsidedigital.nowpoupe.R;
import com.vcinsidedigital.nowpoupe.helper.EmprestimoDAO;
import com.vcinsidedigital.nowpoupe.model.Emprestimo;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity
{

    private TextInputEditText clienteText, dataText_entrada, data_saidaInput, valorText, taxaText, totalAPagarText;
    private Emprestimo emprestimoAtual;

    private FloatingActionButton fab;
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
        dataText_entrada = findViewById(R.id.dataText);
        data_saidaInput = findViewById(R.id.data_saidaInput);
        valorText = findViewById(R.id.valorText);
        taxaText = findViewById(R.id.textTaxa);
        totalAPagarText = findViewById(R.id.totalAPagarText);

        if(emprestimoAtual != null){

            fab = findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);

            clienteText.setText(emprestimoAtual.getCliente());
            dataText_entrada.setText(emprestimoAtual.getDataEntrada());
            data_saidaInput.setText(emprestimoAtual.getDataSaida());
            valorText.setText(Double.toString(emprestimoAtual.getValor()));
            taxaText.setText(Double.toString(emprestimoAtual.getTaxa()));
            totalAPagarText.setText(Double.toString(emprestimoAtual.getTotalAPagar()));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PdfDocument pdfDocument = new PdfDocument();
                    Paint paint = new Paint();

                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();

                    // Paint para texto em negrito
                    Paint boldPaint = new Paint();
                    boldPaint.setTextSize(16);
                    boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    // Paint para texto normal
                    Paint normalPaint = new Paint();
                    normalPaint.setTextSize(16);
                    normalPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

                    // Texto do cliente
                    String cliente = clienteText.getText().toString();

                    // Medindo a largura do texto
                    float textWidth = boldPaint.measureText(cliente);

                    // Calculando a posição x para centralizar
                    int canvasWidth = canvas.getWidth();
                    float x = (canvasWidth - textWidth) / 2;

                    // Desenhando o texto centralizado no Canvas
                    canvas.drawText(cliente, x, 25, boldPaint);

                    canvas.drawText("Data de Entrada: ", 10, 75, boldPaint);
                    canvas.drawText(dataText_entrada.getText().toString(), 135, 75, normalPaint);

                    canvas.drawText("Data de Saída: ", 10, 100, boldPaint);
                    canvas.drawText(data_saidaInput.getText().toString(), 120, 100, normalPaint);

                    canvas.drawText("Valor: ", 10, 125, boldPaint);
                    canvas.drawText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(emprestimoAtual.getValor()), 55, 125, normalPaint);

                    canvas.drawText("Taxa: ", 10, 150, boldPaint);
                    canvas.drawText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(emprestimoAtual.getTaxa()), 55, 150, normalPaint);

                    canvas.drawText("Total a Pagar: ", 10, 175, boldPaint);
                    canvas.drawText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(emprestimoAtual.getTotalAPagar()), 115, 175, normalPaint);

                    pdfDocument.finishPage(page);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, emprestimoAtual.getCliente()+".pdf");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/nowPoupe");

                    Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

                    try {
                        OutputStream outputStream = getContentResolver().openOutputStream(uri);
                        pdfDocument.writeTo(outputStream);
                        Toast.makeText(getApplicationContext(), "PDF Criado com sucesso!", Toast.LENGTH_LONG).show();
                        outputStream.close();

                        // Abrindo o PDF
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/pdf");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Erro ao criar o PDF!", Toast.LENGTH_LONG).show();
                    }

                    pdfDocument.close();
                }
            });
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
                    String textoData_entrada = dataText_entrada.getText().toString();
                    String textoData_Saida = data_saidaInput.getText().toString();
                    String textoValor = valorText.getText().toString();
                    String textoTaxa = taxaText.getText().toString();
                    String textoTotalAPagar = totalAPagarText.getText().toString();
                    if(!textoCliente.isEmpty() || !textoData_entrada.isEmpty() || !textoData_Saida.isEmpty() || !textoValor.isEmpty() || !textoTaxa.isEmpty() || !textoTotalAPagar.isEmpty()){
                        Emprestimo emprestimo = new Emprestimo();
                        emprestimo.setId(emprestimoAtual.getId());
                        emprestimo.setCliente(textoCliente);
                        emprestimo.setDataEntrada(textoData_entrada);
                        emprestimo.setDataSaida(textoData_Saida);
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
                    String textoData_entrada = dataText_entrada.getText().toString();
                    String textoData_saida = data_saidaInput.getText().toString();
                    String textoValor = valorText.getText().toString();
                    String textoTaxa = taxaText.getText().toString();
                    String textoTotalAPagar = totalAPagarText.getText().toString();

                    if(!textoCliente.isEmpty() || !textoData_entrada.isEmpty() || !textoData_saida.isEmpty() || !textoValor.isEmpty() || !textoTaxa.isEmpty() || !textoTotalAPagar.isEmpty()){
                        Emprestimo emprestimo = new Emprestimo();
                        emprestimo.setCliente(textoCliente);
                        emprestimo.setDataEntrada(textoData_entrada);
                        emprestimo.setDataSaida(textoData_saida);
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
