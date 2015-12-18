package com.fatec.jrss.projetofinal.db.Tabelas;

/**
 * Enumeração dos nomes das todas as Tabelas do banco.
 */
public enum EnumTabelas {
    ANIVERSARIANTES("Aniversariantes"),
    PRESENTES("Presentes"),
    ANIV_PRES("AniversariantesPresentes");

    private String nome;

    EnumTabelas(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }
}
