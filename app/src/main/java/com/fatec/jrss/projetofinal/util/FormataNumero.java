package com.fatec.jrss.projetofinal.util;

/**
 * Classe com as formatações de número
 */
public class FormataNumero {
    /***
     * Método para formatar o valor do dia ou mês, adicionando um '0' a frente de valores menores que 10
     * @param valor
     * @return
     */
    public String formatarNumero(Integer valor){
        String d = "0";
        if (valor < 10)
            d += valor.toString();
        else
            d = valor.toString();
        return d;
    }
}
