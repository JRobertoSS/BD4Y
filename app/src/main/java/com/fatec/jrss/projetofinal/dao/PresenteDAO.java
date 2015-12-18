package com.fatec.jrss.projetofinal.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.db.Tabelas.EnumTabelas;
import com.fatec.jrss.projetofinal.dominio.EntidadeDominio;
import com.fatec.jrss.projetofinal.dominio.Presente;

import java.sql.SQLException;


/**
 * DAO para executar as operações do banco referentes aos Presentes
 */
public class PresenteDAO extends AbstractDAO {
    private SQLiteDatabase db;

    public PresenteDAO(Context contexto) {
        super(contexto);
    }

    @Override
    public boolean salvar(EntidadeDominio entidade) throws SQLException {
        Presente pres = (Presente) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "insert into " + EnumTabelas.PRESENTES.getNome() + " (" +
                EnumColunas.NOME.getNome() + ", " + EnumColunas.PRECO.getNome() + ") values ('"
                + pres.getNome() + "', " + pres.getPreco() +")";
        db.execSQL(sql);
        return true;
    }

    @Override
    public boolean alterar(EntidadeDominio entidade) throws SQLException {
        Presente pres = (Presente) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "update " + EnumTabelas.PRESENTES.getNome() + " set " +
                EnumColunas.NOME.getNome() + " = '" + pres.getNome() + "', " +
                EnumColunas.PRECO.getNome() + " = " + pres.getPreco() + " where " +
                EnumColunas.ID.getNome() + " = " + pres.getId();
        db.execSQL(sql);
        return true;
    }

    @Override
    public boolean excluir(EntidadeDominio entidade) throws SQLException {
        Presente pres = (Presente) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "delete from " + EnumTabelas.PRESENTES.getNome() + " where " +
                EnumColunas.ID.getNome() + " = " + pres.getId();
        db.execSQL(sql);

        return true;
    }

    @Override
    public Cursor consultar(EntidadeDominio entidade) throws SQLException {
        Presente pres = (Presente) entidade;

        db = abrirConexao();

        String sql = "select " + EnumColunas.ID.getNome() + ", " +
                EnumColunas.NOME.getNome() + ", " + EnumColunas.PRECO.getNome() + " from " +
                EnumTabelas.PRESENTES.getNome() +  " where " +
                EnumColunas.ID.getNome() + " = " + pres.getId();

        return db.rawQuery(sql, new String[]{});
    }

    @Override
    public Cursor listar() throws SQLException {
        db = abrirConexao();

        String sql = "select " + EnumColunas.ID.getNome() + ", " +
                EnumColunas.NOME.getNome() + ", " + EnumColunas.PRECO.getNome() + " from " +
                EnumTabelas.PRESENTES.getNome();

        return db.rawQuery(sql, new String[]{});
    }


}
