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
 * DAO para executar as operações do banco referentes aos Presentes de um Aniversariante
 */
public class AniversariantePresenteDAO extends AbstractDAO {
    private SQLiteDatabase db;


    public AniversariantePresenteDAO(Context contexto) {
        super(contexto);
    }

    @Override
    public boolean salvar(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        // verificar se já não existe o presente adicionado
        Cursor c = presentesDoAniversariante(aniv);
        while (c.moveToNext()) {
            if (aniv.getPresentes().get(0).getId() ==
                    c.getInt(c.getColumnIndex(EnumColunas.ID.getNome()))) //existe o presente?
                return false; // retorna indicando que já existe
        }
        // caso não haja, adicionar este novo
        String sql = "insert into " + EnumTabelas.ANIV_PRES.getNome() + " (" +
                EnumColunas.ANIVERSARIANTE.getNome() + ", " + EnumColunas.PRESENTE.getNome() +
                ") values (" + aniv.getId() + ", " + aniv.getPresentes().get(0).getId() + ")";
        db.execSQL(sql);

        return true;
    }

    @Override
    public boolean alterar(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "update " + EnumTabelas.ANIV_PRES.getNome() + " set " +
                EnumColunas.ANIVERSARIANTE.getNome() + " = " + aniv.getId() +
                EnumColunas.PRESENTE.getNome() + " = " + aniv.getPresentes().get(1).getId() +
                " where " + EnumColunas.ID.getNome() + " = " + aniv.getPresentes().get(0).getId();
        db.execSQL(sql);

        return true;
    }

    @Override
    public boolean excluir(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();
        if (db.isReadOnly()) // conexão não permite escrita?
            return false; // abortaroperação

        String sql = "delete from " + EnumTabelas.ANIV_PRES.getNome() + " where " +
                EnumColunas.ANIVERSARIANTE.getNome() + " = " + aniv.getId() + " and " +
                EnumColunas.PRESENTE.getNome() + " = " + aniv.getPresentes().get(0).getId();
        db.execSQL(sql);

        return true;
    }

    @Override
    public Cursor consultar(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();

        String sql = "select " + EnumColunas.ID.getNome() + ", " +
                EnumColunas.PRESENTE.getNome() + " from " +
                EnumTabelas.ANIV_PRES.getNome() + " where " +
                EnumColunas.ID.getNome() + " = " + aniv.getId();

        return db.rawQuery(sql, new String[]{});
    }

    @Override
    public Cursor listar() throws SQLException {
        db = abrirConexao();

        String sql = "select " + EnumColunas.ID.getNome() + ", " +
                EnumColunas.ANIVERSARIANTE.getNome() + ", " +
                EnumColunas.PRESENTE.getNome() + " from " +
                EnumTabelas.ANIV_PRES.getNome();

        return db.rawQuery(sql, new String[]{});
    }

    /***
     * Método que retorna uma consulta com todos os presentes de um determinado aniversariante.
     * Utiliza a tabela auxiliar das relações entre Aniversariantes e Presentes para tal.
     * @param entidade
     * @return
     * @throws SQLException
     */
    public Cursor presentesDoAniversariante(EntidadeDominio entidade) throws SQLException {
        Aniversariante aniv = (Aniversariante) entidade;

        db = abrirConexao();

        String sql = "select " + EnumTabelas.PRESENTES.getNome() +"."+ EnumColunas.ID.getNome() + ", " + EnumColunas.NOME.getNome() +
                ", " + EnumColunas.PRECO.getNome() +
                " from " + EnumTabelas.PRESENTES.getNome() +
                " inner join " + EnumTabelas.ANIV_PRES.getNome() +
                " on " + EnumTabelas.ANIV_PRES.getNome() + "." + EnumColunas.PRESENTE.getNome() +
                " = " + EnumTabelas.PRESENTES.getNome() + "." + EnumColunas.ID.getNome() +
                " where " + EnumTabelas.ANIV_PRES.getNome() + "." + EnumColunas.ANIVERSARIANTE.getNome() +
                " = " + aniv.getId();

        return db.rawQuery(sql, new String[]{});
    }

    /***
     * Método que retorna uma consulta com todos os aniversariantes que possuem um determinado presente.
     * Utiliza a tabela auxiliar das relações entre Aniversariantes e Presentes para tal.
     * @param entidade
     * @return
     * @throws SQLException
     */
    public Cursor aniversariantesComPresente(EntidadeDominio entidade) throws SQLException {
        Presente pres = (Presente) entidade;

        db = abrirConexao();

        String sql = "select " + EnumTabelas.ANIVERSARIANTES.getNome() + "." + EnumColunas.ID.getNome() + ", "
                + EnumColunas.NOME.getNome() + " from " + EnumTabelas.ANIVERSARIANTES.getNome() +
                " inner join " + EnumTabelas.ANIV_PRES.getNome() +
                " on " + EnumTabelas.ANIV_PRES.getNome() + "." + EnumColunas.ANIVERSARIANTE.getNome() +
                " = " + EnumTabelas.ANIVERSARIANTES.getNome() + "." + EnumColunas.ID.getNome() +
                " where " + EnumTabelas.ANIV_PRES.getNome() + "." + EnumColunas.PRESENTE.getNome() +
                " = " + pres.getId();

        return db.rawQuery(sql, new String[]{});
    }
}
