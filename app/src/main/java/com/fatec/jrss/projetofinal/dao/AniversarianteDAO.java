package com.fatec.jrss.projetofinal.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.db.Tabelas.EnumTabelas;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.dominio.EntidadeDominio;
import com.fatec.jrss.projetofinal.dominio.Presente;

import java.sql.SQLException;

/**
 * DAO para executar as operações do banco referentes aos Aniversariantes
 */
public class AniversarianteDAO extends AbstractDAO{
    private SQLiteDatabase db;

    public AniversarianteDAO(Context contexto){
        super(contexto);
    }

    @Override
    public boolean salvar(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "insert into " + EnumTabelas.ANIVERSARIANTES.getNome() + " (" +
                EnumColunas.NOME.getNome() + ", " + EnumColunas.DIA_ANIVERSARIO.getNome() + ", " +
                EnumColunas.MES_ANIVERSARIO.getNome() + ", " + EnumColunas.EMAIL.getNome() +
                ") values ('" + aniv.getNome() + "', " + aniv.getDiaAniversario() + ", " +
                aniv.getMesAniversario() + ", '" + aniv.getEmail() + "')";
        db.execSQL(sql);
        return true;
    }

    @Override
    public boolean alterar(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "update " + EnumTabelas.ANIVERSARIANTES.getNome() + " set " +
                EnumColunas.NOME.getNome() + " = '" + aniv.getNome() + "', " +
                EnumColunas.DIA_ANIVERSARIO.getNome() + " = " + aniv.getDiaAniversario() + ", " +
                EnumColunas.MES_ANIVERSARIO.getNome() + " = " + aniv.getMesAniversario() + ", " +
                EnumColunas.EMAIL + " = '" + aniv.getEmail() + "' where  " +
                EnumColunas.ID.getNome() + " = " + aniv.getId();
        db.execSQL(sql);
        return true;

    }

    @Override
    public boolean excluir(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "delete from " + EnumTabelas.ANIVERSARIANTES.getNome() + " where  " +
                EnumColunas.ID.getNome() + " = " + aniv.getId();
        db.execSQL(sql);
        return true;
    }

    @Override
    public Cursor consultar(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();

        String sql = "select " + EnumColunas.ID.getNome() + ", " + EnumColunas.NOME.getNome() +
                ", " + EnumColunas.DIA_ANIVERSARIO.getNome() + ", " + EnumColunas.MES_ANIVERSARIO.getNome() + ", "
                + EnumColunas.EMAIL.getNome() + " from " + EnumTabelas.ANIVERSARIANTES.getNome() + " where " +
                EnumColunas.ID.getNome() + " = " + aniv.getId();

        return db.rawQuery(sql, new String[]{});
    }

    @Override
    public Cursor listar() throws SQLException {
        db = abrirConexao();

        String sql = "select " + EnumColunas.ID.getNome() + ", " + EnumColunas.NOME.getNome() +
                ", " + EnumColunas.DIA_ANIVERSARIO.getNome() + ", " + EnumColunas.MES_ANIVERSARIO.getNome() + ", "
                + EnumColunas.EMAIL.getNome() + " from " + EnumTabelas.ANIVERSARIANTES.getNome();

        return db.rawQuery(sql, new String[]{});
    }


}
