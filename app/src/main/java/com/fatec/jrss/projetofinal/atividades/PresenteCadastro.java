package com.fatec.jrss.projetofinal.atividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.dao.PresenteDAO;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.dominio.Presente;
import com.fatec.jrss.projetofinal.util.ValidarCampos;
import com.fatec.jrss.projetofinal.util.VerificaCamposVazios;

import java.sql.SQLException;

/***
 * Atividade do cadastro de novos presentes. Todos os campos são obrigatórios.
 * Botão Home = Volta ao Menu Princpal
 * Botão Cancelar = Cancela o cadastro, voltando à tela anterior
 */

public class PresenteCadastro extends Activity implements View.OnClickListener {
    private EditText nome, preco; // campos de texto
    private Button cadastrar, home, cancelar; // botões

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presente_cadastro);
        // encontrar as visões
        nome = (EditText) findViewById(R.id.txtNomePresente);
        preco = (EditText) findViewById(R.id.txtPreco);
        cadastrar = (Button) findViewById(R.id.btnCadastrar);
        home = (Button) findViewById(R.id.btnHome);
        cancelar = (Button) findViewById(R.id.btnCancela);
        // adicionar o listener de onClick para os botões
        cadastrar.setOnClickListener(this);
        home.setOnClickListener(this);
        cancelar.setOnClickListener(this);
    }
    /***
     * Sobrescrita do método onClick para tratar os toques nas visões da atividade
     * @param v Visão selecionada
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == cadastrar.getId()) { // clicou em cadastrar?
            String nomePres = nome.getText().toString(); // pega o nome do presente
            String precoPres = preco.getText().toString(); // pega o preço do presente
            VerificaCamposVazios vcv = new VerificaCamposVazios(); // instância de verificação
            if(!vcv.verificarNomePreco(nomePres, precoPres, this)) // preço ou nome inválidos?
                return; // cancela o cadastro
            Presente pres = new Presente(); // instância de Presente
            pres.setNome(nomePres); // preenche o nome
            pres.setPreco(Double.valueOf(precoPres)); // preenche o preço

            PresenteDAO aDao = new PresenteDAO(this); // instância de DAO

            try {
                if (aDao.salvar(pres)) // salvo com sucesso?
                    Toast.makeText(this, R.string.salvoSucesso, Toast.LENGTH_SHORT).show(); // mensagem sucesso
                else
                    Toast.makeText(this, R.string.salvoFalha, Toast.LENGTH_SHORT).show(); // mensagem falha
                Intent intencao = new Intent(this, PresentesMain.class); // intenção para o menu de presentes
                startActivity(intencao); // começar a atividade

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        Intent intent = null; // intenção
        if (v.getId() == home.getId()) // selecionou home?
            intent = new Intent(this, MenuPrincipal.class); // intenção para o menu principal
        if (v.getId() == cancelar.getId()) // selecionou cancelar?
            intent = new Intent(this, PresentesMain.class); // intenção para o menu de presentes
        if (intent != null) // existe intenção?
            startActivity(intent); // começar a atividade
    }
}
