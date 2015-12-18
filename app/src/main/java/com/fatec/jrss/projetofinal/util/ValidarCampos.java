package com.fatec.jrss.projetofinal.util;

import android.content.Context;
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;

/**
 * Classe com as validações
 */
public class ValidarCampos {
    /***
     * Método para validar o dia e o mes
     * @param dia valor do Dia
     * @param mes valor do Mês
     * @param contexto contexto para mostrar a mensagem
     * @return true - campos ok | false - campo(s) inválido(s)
     */
    public boolean validarData(int dia, int mes, Context contexto){
        if (dia < 1 || dia > 31 || mes < 1 || mes > 12) { // dia ou mês com valores inválidos?
            Toast.makeText(contexto, R.string.erroData, Toast.LENGTH_SHORT).show(); // mensagem de erro
            return false; // retorna erro
        }
        return true; // retorna sucesso
    }

    /***
     * Método para verificar valores inválidos de preço
     * @param preco preço do presente
     * @param contexto contexto para mostrar a mensagem
     * @return true - campos ok | false - campo(s) inválido(s)
     */
    public boolean validarPreco(double preco, Context contexto){
        if (preco <= 0) {
            Toast.makeText(contexto, R.string.erroPreco, Toast.LENGTH_SHORT).show();
            return false; // retorna erro
        }
        return true; // retorna sucesso
    }
}
