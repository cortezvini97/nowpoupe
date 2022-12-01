package com.vcinsidedigital.nowpoupe.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vcinsidedigital.nowpoupe.model.Emprestimo;

import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO implements IEmprestimoDAO{

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public EmprestimoDAO(Context context){
        DBHelper db = new DBHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Emprestimo emprestimo) {
        ContentValues cv = new ContentValues();
        cv.put("cliente", emprestimo.getCliente());
        cv.put("valor", emprestimo.getValor());
        cv.put("taxa", emprestimo.getTaxa());
        cv.put("total_a_pagar", emprestimo.getTotalAPagar());
        cv.put("data", emprestimo.getData());

        try{
            escreve.insert(DBHelper.TABLE_NAME, null, cv);
            Log.i("INFO", "Sucesso ao Salvar Emprestimo!");
            return true;
        }catch (Exception e){
            Log.i("INFO", "Erro ao Salvar Emprestimo " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizar(Emprestimo emprestimo) {
        String[] args = {emprestimo.getId().toString()};
        ContentValues cv = new ContentValues();
        cv.put("cliente", emprestimo.getCliente());
        cv.put("valor", emprestimo.getValor());
        cv.put("taxa", emprestimo.getTaxa());
        cv.put("total_a_pagar", emprestimo.getTotalAPagar());
        cv.put("data", emprestimo.getData());
        try{
            escreve.update(DBHelper.TABLE_NAME, cv, "id=?", args);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deletar(Emprestimo emprestimo) {
        String[] args = {emprestimo.getId().toString()};
        try{
            escreve.delete(DBHelper.TABLE_NAME, "id=?", args);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Emprestimo> listar() {
        List<Emprestimo> listaEmprestimos = new ArrayList<Emprestimo>();

        String sql = "SELECT * FROM " + DBHelper.TABLE_NAME + " ;";
        Cursor c = le.rawQuery(sql, null);

        while (c.moveToNext()){

            @SuppressLint("Range") Long id = c.getLong(c.getColumnIndex("id"));
            @SuppressLint("Range") String cliente = c.getString(c.getColumnIndex("cliente"));
            @SuppressLint("Range") double valor = c.getDouble(c.getColumnIndex("valor"));
            @SuppressLint("Range") double taxa = c.getDouble(c.getColumnIndex("taxa"));
            @SuppressLint("Range") double totalAPagar = c.getDouble(c.getColumnIndex("total_a_pagar"));
            @SuppressLint("Range") String data = c.getString(c.getColumnIndex("data"));

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setId(id);
            emprestimo.setCliente(cliente);
            emprestimo.setValor(valor);
            emprestimo.setTaxa(taxa);
            emprestimo.setTotalAPagar(totalAPagar);
            emprestimo.setData(data);

            listaEmprestimos.add(emprestimo);
        }

        return listaEmprestimos;
    }

    public static boolean deleteDatabse(Context context){
        try {
            context.deleteDatabase(DBHelper.NAME_DB);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
