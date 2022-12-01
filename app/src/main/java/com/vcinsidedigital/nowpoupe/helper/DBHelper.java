package com.vcinsidedigital.nowpoupe.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String NAME_DB = "NOW_POUPE";
    public static final String TABLE_NAME = "emprestimos";

    public DBHelper(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " cliente VARCHAR(60) NOT NULL,"
                + " valor DECIMAL(10,2) NOT NULL,"
                + " taxa DECIMAL(10,2) NOT NULL, "
                + " data VARCHAR(20) NOT NULL, "
                + " total_a_pagar DECIMAL(10,2) NOT NULL ); ";

        try {
            db.execSQL(sql);
            Log.i("DB_INFO", "Tabela criada com sucesso.");
        }catch (Exception e){
            Log.i("DB_INFO", "Ocorreu um erro ao criar tabela.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + " ;";

        try {
            db.execSQL(sql);
            onCreate(db);
            Log.i("DB_INFO", "Tabela atualizada com sucesso.");
        }catch (Exception e){
            Log.i("DB_INFO", "Ocorreu um erro ao atualizar tabela.");
        }
    }
}
