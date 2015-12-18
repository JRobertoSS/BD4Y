package com.fatec.jrss.projetofinal.dominio;

/**
 * Classe de domínio representando os Presentes
 */
public class Presente extends EntidadeDominio{
    private String nome;
    private double preco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
