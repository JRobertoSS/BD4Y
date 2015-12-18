package com.fatec.jrss.projetofinal.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fatec.jrss.projetofinal.db.DBHelper;

/**
 * Implementação da interface IDAO, que contém o adaptador do banco de dados e estabelece a conexão.
 *
 */
public abstract class AbstractDAO implements IDAO{
    private DBHelper adaptador;

    public AbstractDAO (Context contexto) {
        adaptador = DBHelper.getInstance(contexto);
    }

    public SQLiteDatabase abrirConexao(){
        return adaptador.getWritableDatabase();
    }
}
