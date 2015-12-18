package com.fatec.jrss.projetofinal.dominio;

/**
 * Classe de domínio representando as Pessoas
 */
public class Pessoa extends EntidadeDominio{
    private String nome;
    private Integer diaAniversario;
    private Integer mesAniversario;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Integer getDiaAniversario() {
        return diaAniversario;
    }

    public void setDiaAniversario(Integer diaAniversario) {
        this.diaAniversario = diaAniversario;
    }

    public Integer getMesAniversario() {
        return mesAniversario;
    }

    public void setMesAniversario(Integer mesAniversario) {
        this.mesAniversario = mesAniversario;
    }
}
