package com.fatec.jrss.projetofinal.dao;

import android.database.Cursor;

import com.fatec.jrss.projetofinal.dominio.EntidadeDominio;

import java.sql.SQLException;


/**
 * Interface com as operações comuns dos DAOs
 */
public interface IDAO {
     boolean salvar(EntidadeDominio entidade) throws SQLException;
     boolean alterar(EntidadeDominio entidade) throws SQLException;
     boolean excluir(EntidadeDominio entidade) throws SQLException;
     Cursor consultar(EntidadeDominio entidade) throws SQLException;
     Cursor listar() throws SQLException;
}
