package com.fatec.jrss.projetofinal.db.Colunas;

/**
 * Created by JoséRoberto on 24/11/2015.
 * Enumeração dos nomes das colunas de todas as tabelas do banco.
 */
public enum EnumColunas {
    ID("_id"),
    NOME("nome"),
    DIA_ANIVERSARIO("diaAniversario"),
    MES_ANIVERSARIO("mesAniversario"),
    EMAIL("email"),
    ANIVERSARIANTE("aniversariante"),
    PRESENTE("presente"),
    PRECO("preco");

    private String nome;

    EnumColunas(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }
}
