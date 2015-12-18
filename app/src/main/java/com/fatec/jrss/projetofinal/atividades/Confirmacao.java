package com.fatec.jrss.projetofinal.atividades;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fatec.jrss.projetofinal.R;

/***
 * Atividade de estilo modal responsável por exibir mensagem de confirmação ou cancelamento
 * de operações
 */
public class Confirmacao extends Activity implements View.OnClickListener {
    private TextView mensagemConfirmacao; // para receber a mensagem passada por parâmetro
    private static Integer idOperacoes; // para receber o id do item
    private Button confirmar, cancelar; // botões para confirmar ou cancelar a operação

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        mensagemConfirmacao = (TextView) findViewById(R.id.mensagemConfirmacao); // encontra o TextView
        Bundle extras = getIntent().getExtras(); // recebe os parâmetros
        if (extras != null) { // há parâmetros?
            mensagemConfirmacao.setText(extras.getString("msg")); // recebe a mensagem
            idOperacoes = extras.getInt("idP"); // recebe o id
        }
        // encontra os botões
        confirmar = (Button) findViewById(R.id.btnConfirma);
        cancelar = (Button) findViewById(R.id.btnCancela);
        // adiciona o listener de onClick aos botões
        confirmar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
    }
    /***
     * Sobrescrita do método onClick para tratar os toques nas visões da atividade
     * @param v Visão selecionada
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == confirmar.getId()) { // selecionou confirmar?
            if (idOperacoes != null) { // existe o id?
                Intent retorno = new Intent(); // intenção de retorno
                retorno.putExtra("idP", idOperacoes); // passa o ID como parâmetro
                setResult(RESULT_OK, retorno); // indica OK para o resultado
            } else {
                setResult(RESULT_OK); // indica OK para o resultado
            }
        }
        if (v.getId() == cancelar.getId()) // selecionou cancelar?
            setResult(RESULT_CANCELED); // indica CANCELED para o resultado
        finish(); // devolve o resultado
    }
}
