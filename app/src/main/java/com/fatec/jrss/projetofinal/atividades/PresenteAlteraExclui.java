package com.fatec.jrss.projetofinal.atividades;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.dao.IDAO;
import com.fatec.jrss.projetofinal.dao.PresenteDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.dominio.EntidadeDominio;
import com.fatec.jrss.projetofinal.dominio.Presente;
import com.fatec.jrss.projetofinal.util.ValidarCampos;
import com.fatec.jrss.projetofinal.util.VerificaCamposVazios;

import java.sql.SQLException;

public class PresenteAlteraExclui extends Activity implements View.OnClickListener {
    private EditText nome, preco;
    private Button alterar, excluir, home, presentes;
    int idPresente, idAniversariante;
    private static final int ID_REQUISICAO = 556;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presente_altera_exclui);

        // encontrar as visões
        nome = (EditText) findViewById(R.id.txtAltExcNome);
        preco = (EditText) findViewById(R.id.txtAltExcPreco);
        alterar = (Button) findViewById(R.id.btnAlterar);
        excluir = (Button) findViewById(R.id.btnExcluir);
        home = (Button) findViewById(R.id.btnHome);
        presentes = (Button) findViewById(R.id.btnPresentes);
        // adicionar o listener de onClick para os botões
        alterar.setOnClickListener(this);
        excluir.setOnClickListener(this);
        home.setOnClickListener(this);
        presentes.setOnClickListener(this);

        Bundle extras = getIntent().getExtras(); // pegar os parâmetros
        if (extras != null) // existem parâmetros?
            idPresente = extras.getInt("idP"); // pega o id do Presente

        Presente pres = new Presente(); // nova instância de Presente
        pres.setId(idPresente); // preenche o id do presente

        inicializarDados(new PresenteDAO(this), pres); // inincializa os dados do Presente
    }

    /***
     * Método que realiza uma consulta no banco e preenche os EditText com resultados
     * @param dao DAO da consulta desejada
     * @param entidade Entidade que contém o id da consulta
     */
    public void inicializarDados(IDAO dao, EntidadeDominio entidade) {
        // realizar o Cast da interface para o tipo correto
        PresenteDAO pDao = (PresenteDAO) dao;
        Presente pres = (Presente) entidade;

        try {
            Cursor c = pDao.consultar(pres); // realiza a consulta no banco
            if (c.moveToNext()) { // houve resultado?
                nome.setText(c.getString(c.getColumnIndex(EnumColunas.NOME.getNome()))); // preenche o nome
                Double valor = c.getDouble(c.getColumnIndex(EnumColunas.PRECO.getNome())); // pega o preço
                preco.setText(valor.toString()); // converte para string e preenche o preço
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /***
     * Sobrescrita do método onClick para tratar os toques nas visões da atividade
     * @param v Visão selecionada
     */
    @Override
    public void onClick(View v) {
        String nomePres = nome.getText().toString(); // pega o nome do presente
        String strPreco = preco.getText().toString(); // pega o preço do presente
        if (v.getId() == alterar.getId()) { // selecionou alterar?
            Presente pres = new Presente(); // nota instância de Presente
            pres.setId(idPresente); // preenche o id do Presente

            VerificaCamposVazios vcv = new VerificaCamposVazios(); // instância de verificção

            if (!vcv.verificarNomePreco(nomePres, strPreco, this)) // nome ou preço vazio?
                return; // cancela a alteração

            Double precoPres = Double.valueOf(strPreco); // converte o preço para Double

            ValidarCampos vc = new ValidarCampos(); // instância de validação

            if(!vc.validarPreco(precoPres, this)) // preço inválido?
                return; // cancela a alteração

            pres.setNome(nomePres); // preenche o nome
            pres.setPreco(precoPres); // preenche o preço

            PresenteDAO dao = new PresenteDAO(this); // nova instância de DAO
            try {
                if (dao.alterar(pres)) // sucesso na alteração?
                    Toast.makeText(this, R.string.atualizadoSucesso, Toast.LENGTH_SHORT).show(); // mensagem de sucesso
                else
                    Toast.makeText(this, R.string.atualizadoFalha, Toast.LENGTH_SHORT).show(); // mensagem de falha
                Intent intencao = new Intent(this, PresentesMain.class); // intenção para o menu de presentes
                startActivity(intencao); // começar a atividade

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Intent intent; // intenção
        if (v.getId() == excluir.getId()) { // selecionou excluir?
            intent = new Intent(this, Confirmacao.class); // intenção para a atividade de confirmação
            intent.putExtra("msg", getResources().getString(R.string.confirmaExcluir) + " " + nomePres + "?"); // passa  mensagem como parâmetro
            startActivityForResult(intent, ID_REQUISICAO); // começa a atividade e aguarda resposta
        }
        if (v.getId() == home.getId()) { // selecionou home?
            intent = new Intent(this, MenuPrincipal.class); // intenção para o menu principal
            startActivity(intent); // começa a atividade
        }
        if (v.getId() == presentes.getId()) { // selecionou o botão Detalhes do Presente?
            intent = new Intent(this, PresenteDetalhes.class); // intenção para a atividad de Detalhes do Presente
            intent.putExtra("idP", idPresente); // passa o id do presente como parâmetro
            startActivity(intent); // começa a atividade
        }

    }
    /***
     * Sobrescrita do método onActivityResult, que trata a resposta do startActivityForResult
     * @param requestCode id da requisicão passada
     * @param resultCode resposta de confirmação ou cancelamento
     * @param data dados/parâmetros da resposta
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ID_REQUISICAO) { // é a resposta da requisição desta atividade?
            if (resultCode == RESULT_OK) { // o resultado foi ok?
                Presente pres = new Presente(); // isntância de Presente
                pres.setId(idPresente); // preenche o Id
                PresenteDAO dao = new PresenteDAO(this); // instância de DAO
                try {
                    if (dao.excluir(pres)) // sucesso na exclusão?
                        Toast.makeText(this, R.string.excluidoSucesso, Toast.LENGTH_SHORT).show(); // mensagem de sucesso
                    else
                        Toast.makeText(this, R.string.excluidoFalha, Toast.LENGTH_SHORT).show(); // mensagem de falha
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Intent intencao = new Intent(this, PresentesMain.class); // nova intenção para o menu de presentes
                startActivity(intencao); // começa a atividade
            }
        }
    }

}
