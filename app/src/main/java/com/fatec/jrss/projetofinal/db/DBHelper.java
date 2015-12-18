package com.fatec.jrss.projetofinal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.db.Tabelas.EnumTabelas;

/**
 * Created by JoséRoberto on 20/11/2015.
 * Classe responsável pela criação do banco e conexão.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final Integer DB_VERSION = 1;
    private static final String DB_NAME = "Bd4y.DB";
    private static final String CREATE = "create table ";
    private static final String PK = "integer primary key autoincrement, ";
    private static final String FK = "foreign key";
    private static final String TB_CREATE_ANIVERSARIANTES = CREATE + EnumTabelas.ANIVERSARIANTES.getNome() + " (" +
            EnumColunas.ID.getNome() + " " + PK +
            EnumColunas.NOME.getNome() + " text not null, " +
            EnumColunas.DIA_ANIVERSARIO.getNome() + " integer not null, " +
            EnumColunas.MES_ANIVERSARIO.getNome() + " integer not null, " +
            EnumColunas.EMAIL.getNome() + " text)";
    private static final String TB_CREATE_PRESENTES = CREATE + EnumTabelas.PRESENTES.getNome() + " (" +
            EnumColunas.ID.getNome() + " " + PK +
            EnumColunas.NOME.getNome() + " text not null, " +
            EnumColunas.PRECO.getNome() + " real not null)";
    private static final String TB_CREATE_ANIVERSARIANTES_PRESENTES = CREATE + EnumTabelas.ANIV_PRES.getNome() + " (" +
            EnumColunas.ID.getNome() + " " + PK +
            EnumColunas.ANIVERSARIANTE.getNome() + " integer not null, " +
            EnumColunas.PRESENTE.getNome() + " integer not null, " +
            FK + "(" + EnumColunas.ANIVERSARIANTE.getNome() + ") references " + EnumTabelas.ANIVERSARIANTES.getNome() + "(" +
            EnumColunas.ID.getNome() + "), " +
            FK + "(" + EnumColunas.PRESENTE.getNome() + ") references " + EnumTabelas.PRESENTES.getNome() + "(" +
            EnumColunas.ID.getNome() + "))";

    private static DBHelper singleton = null;

    private DBHelper(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
        if(singleton != null) {
            throw new IllegalStateException("Apenas uma instância da classe pode ser criada");
        }
    }

    public static DBHelper getInstance(Context contexto){
        if(singleton == null) {
            singleton = new DBHelper(contexto);
        }
        return singleton;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TB_CREATE_ANIVERSARIANTES);
        db.execSQL(TB_CREATE_PRESENTES);
        db.execSQL(TB_CREATE_ANIVERSARIANTES_PRESENTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
