package com.fatec.jrss.projetofinal.util;

import android.content.Context;
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;

/**
 * Classe com as verificações
 */
public class VerificaCamposVazios {

    /***
     * Método que verifica se o nome é vazio
     * @param nome Nome para análise
     * @param contexto contexto para mostrar mensagem de erro
     * @return true - campos ok | false - campo(s) inválido(s)
     */
    public boolean verificarNome(String nome, Context contexto){
        if (nome.trim().equals("") || nome == null) { // nome em branco?
            Toast.makeText(contexto, R.string.erroNome, Toast.LENGTH_SHORT).show(); // mensagem de erro
            return false; // retorna erro
        }
        return true; // retorna sucesso
    }


    /***
     * Método para verificar se a data está vazia
     * @param dia String do dia
     * @param mes String do mês
     * @param contexto contexto para mostrar a mensagem
     * @return true - campos ok | false - campo(s) inválido(s)
     */
    public boolean verificaDataVazia(String dia, String mes, Context contexto){
        if (dia.trim().equals("") || dia == null ||
                mes.trim().equals("") || mes == null ){ // dia ou mês em branco?
            Toast.makeText(contexto, R.string.erroDiaMes, Toast.LENGTH_SHORT).show(); // mensagem de erro
            return false;  // retorna erro
        }
        return true; // retorna sucesso
    }

    /***
     * Método que verifica que o nome ou o preço estâo vazios
     * @param nome nome do presente
     * @param preco preço do presente
     * @param contexto contexto para mostrar a mensagem
     * @return true - campos ok | false - campo(s) inválido(s)
     */
    public boolean verificarNomePreco(String nome, String preco, Context contexto){
        if (nome.trim().equals("") || nome == null ||
                preco.trim().equals("") || preco == null) { // nome ou preço vazio?
            Toast.makeText(contexto, R.string.erroCampos, Toast.LENGTH_SHORT).show(); // mensagem de erro
            return false; // retorna erro
        }
        return true; // retorna sucesso
    }
}
