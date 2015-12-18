package com.fatec.jrss.projetofinal.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe de domínio representando os Aniversariantes
 */
public class Aniversariante extends Pessoa {

    private List<Presente> presentes;

    public Aniversariante() {
        presentes = new ArrayList<Presente>();
    }

    public List<Presente> getPresentes() {
        return presentes;
    }

    public void addPresente(Presente presente) {
        this.presentes.add(presente);
    }
}
