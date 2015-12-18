package com.fatec.jrss.projetofinal.atividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.servicos.ServicoNotificacao;

/***
 * Atividade do Menu principal, onde se encontra os botões para iniciar as principais outras
 * atividades do aplicativo.
 * Botões auto-explicativos.
 */

public class MenuPrincipal extends Activity implements View.OnClickListener{
    private Button aniversariantes, presentes, configuracoes, aniversariantesMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        startService(new Intent(this, ServicoNotificacao.class));
        // encontra os botões
        aniversariantes = (Button) findViewById(R.id.btnAniversariantes);
        presentes = (Button) findViewById(R.id.btnPresentes);
        configuracoes = (Button) findViewById(R.id.btnConfiguracoes);
        aniversariantesMes = (Button) findViewById(R.id.btnAniversariantesMes);
        // adiciona o listener de onClick para os botões
        aniversariantes.setOnClickListener(this);
        presentes.setOnClickListener(this);
        configuracoes.setOnClickListener(this);
        aniversariantesMes.setOnClickListener(this);

    }
    /***
     * Sobrescrita do método onClick para tratar os toques nas visões da atividade
     * @param v Visão selecionada
     */
    @Override
    public void onClick(View v) {
        Intent intencao = null; //intenção

        if (v.getId() == aniversariantes.getId()) // escolheu menu aniversariantes?
            intencao = new Intent(this, AniversariantesMain.class); // intenção para a atividade AniversariantesMain
        if (v.getId() == presentes.getId()) // escolheu menu presentes?
            intencao = new Intent(this, PresentesMain.class); // intenção para a atividade PresentesMain
        if (v.getId() == configuracoes.getId()) // escolheu configurações?
            intencao = new Intent(this, Configuracoes.class); // intenção para a atividade Configuracoes
        if(v.getId() == aniversariantesMes.getId()) // escolheu Aniversariantes do Mês?
            intencao = new Intent(this, AniversariantesMes.class); // intenção para a atividade AniversariantesMes
        if (intencao != null) // existe intenção?
            startActivity(intencao); // começar a atividade

    }
}
