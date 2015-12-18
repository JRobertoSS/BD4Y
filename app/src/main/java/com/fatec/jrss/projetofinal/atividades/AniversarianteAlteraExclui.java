package com.fatec.jrss.projetofinal.atividades;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.dao.IDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.dominio.EntidadeDominio;
import com.fatec.jrss.projetofinal.util.ValidarCampos;
import com.fatec.jrss.projetofinal.util.VerificaCamposVazios;

import java.sql.SQLException;

/***
 *  Atividade onde o usuario pode alterar os dados de um aniversariante cadastrado
 *  ou então excluí-lo. Somente o campo e-mail pode ficar em branco, e somente valores
 *  válidos em dia e mês são aceitos.
 *  Botão Home = Menu Principal
 *  Botão Aniversariante = Volta aos detalhes do aniversariante
 */
public class AniversarianteAlteraExclui extends Activity implements View.OnClickListener {
    private EditText nome, dia, mes, email; // campos de texto
    private Button alterar, excluir, home, aniversariante; // botões
    int idOperacoes; // id recebido de outra atividade
    private static final int ID_REQUISICAO = 762; // número da requisição para o startActivityForResult
    private int tamanhoCampos = 2; // tamanho máximo dos campos de data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariante_altera_exclui);
        // encontrar os EditTexts
        nome = (EditText) findViewById(R.id.txtAltExcNome);
        dia = (EditText) findViewById(R.id.txtAltExcDia);
        mes = (EditText) findViewById(R.id.txtAltExcMes);
        email = (EditText) findViewById(R.id.txtAltExcEmail);
        // filtro de tamanho dos campos
        dia.setFilters(new InputFilter[] {new InputFilter.LengthFilter(tamanhoCampos)});
        mes.setFilters(new InputFilter[] {new InputFilter.LengthFilter(tamanhoCampos)});

        // pegar os parâmetros
        Bundle extras = getIntent().getExtras();
        if (extras != null) // existem parâmetros?
            idOperacoes = extras.getInt("idA"); // id do Aniversariante

        Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
        aniv.setId(idOperacoes); // preenche o id de acordo com o que foi recebido

        inicializarDados(new AniversarianteDAO(this), aniv); // inicializa os dados, de acordo com o id recebido

        // encontrar os Buttons
        alterar = (Button) findViewById(R.id.btnAlterar);
        excluir = (Button) findViewById(R.id.btnExcluir);
        home = (Button) findViewById(R.id.btnHome);
        aniversariante = (Button) findViewById(R.id.btnAniversarianteDetalhes);
        // habilitar escuta de click nos botões
        alterar.setOnClickListener(this);
        excluir.setOnClickListener(this);
        home.setOnClickListener(this);
        aniversariante.setOnClickListener(this);
    }

    /***
     * Método que realiza uma consulta no banco e preenche os EditText com resultados
     * @param dao DAO da consulta desejada
     * @param entidade Entidade que contém o id da consulta
     */
    public void inicializarDados(IDAO dao, EntidadeDominio entidade) {
        // realizar o Cast da interface para o tipo correto
        AniversarianteDAO aDao = (AniversarianteDAO) dao;
        Aniversariante aniv = (Aniversariante) entidade;

        try {
            Cursor c = aDao.consultar(aniv); // realiza a consulta no banco
            if (c.moveToNext()) { // houve resultado?
                nome.setText(c.getString(c.getColumnIndex(EnumColunas.NOME.getNome()))); // preenche o nome
                String d = String.valueOf(c.getInt(c.getColumnIndex(EnumColunas.DIA_ANIVERSARIO.getNome()))); //converte o dia para string
                dia.setText(d); // preenche o dia
                String m = String.valueOf(c.getInt(c.getColumnIndex(EnumColunas.MES_ANIVERSARIO.getNome()))); // converte o mês para string
                mes.setText(m); // preenhce o mês
                email.setText(c.getString(c.getColumnIndex(EnumColunas.EMAIL.getNome()))); // preenche o e-mail
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
        String nomeTexto = nome.getText().toString(); // pega a string do nome
        String diaTexto = dia.getText().toString(); // pega a string do dia
        String mesTexto = mes.getText().toString(); // pega a string do mês
        if (v.getId() == alterar.getId()) { // selecionou o botão alterar?

            // verificar se  nome é vazio
            VerificaCamposVazios vcv = new VerificaCamposVazios(); // instância das verificações
            if(!vcv.verificarNome(nomeTexto, this)) // nome vazio?
                return; //interrompe alteração

            // verificar se data é vazia
            if (!vcv.verificaDataVazia(diaTexto, mesTexto, this)) // dia ou mês estão vazios?
                return; // interrompe alteração
            int diaV = Integer.valueOf(diaTexto); // converte dia para int
            int mesV = Integer.valueOf(mesTexto); // converte mês para int

            // verificar se data é válida
            ValidarCampos vc = new ValidarCampos(); // instância da validação
            if (!vc.validarData(diaV, mesV, this)) // dia ou mês inválidos?
                return; // interrompe alteração

            Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
            aniv.setId(idOperacoes); // preenche o id do aniversariante
            aniv.setNome(nomeTexto); // preenche o nome
            aniv.setDiaAniversario(diaV); // preenche o dia
            aniv.setMesAniversario(mesV); // preenche o mês
            aniv.setEmail(email.getText().toString()); // preenche o e-mail

            AniversarianteDAO dao = new AniversarianteDAO(this); // nova instância de DAO
            try {
                if (dao.alterar(aniv)) // sucesso na alteração?
                    Toast.makeText(this, R.string.atualizadoSucesso, Toast.LENGTH_SHORT).show(); // mensagem sucesso
                else
                    Toast.makeText(this, R.string.atualizadoFalha, Toast.LENGTH_SHORT).show(); // mensagem erro
                Intent intencao = new Intent(this, AniversariantesMain.class); // nova intenção para menu aniversariantes
                startActivity(intencao); // começa a atividade

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (v.getId() == excluir.getId()) { // selecionou excluir?
            Intent intencao = new Intent(this, Confirmacao.class); // nova intenção para tela de confirmação
            intencao.putExtra("msg", getResources().getString(R.string.confirmaExcluir) + " " + nomeTexto + "?"); // passa a mensagem como parâmetro
            startActivityForResult(intencao, ID_REQUISICAO); // começa a atividade, e aguarda resposta
        }

        Intent intent = null; //intenção
        if(v.getId() == home.getId()) // selecionou botão home?
            intent = new Intent(this, MenuPrincipal.class); // nova intenção para o menu principal
        if(v.getId() == aniversariante.getId()){ // selecionou o botão aniversariante?
            intent = new Intent(this, AniversarianteDetalhes.class); // nota intenção para os detalhes do aniversariante
            intent.putExtra("idA", idOperacoes); // passa id do aniversariante como parâmetro
        }
        if (intent != null) // existe intenção?
            startActivity(intent); // começar a atividade desejada
    }

    /***
     * Sobrescrita do método onActivityResult, que trata a resposta do startActivityForResult
     * @param requestCode id da requisicão passada
     * @param resultCode resposta de confirmação ou cancelamento
     * @param data dados/parâmetros da resposta
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ID_REQUISICAO){ // é a resposta da requisição desta atividade?
            if (resultCode == RESULT_OK){ // o resultado foi ok?
                Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
                aniv.setId(idOperacoes); // preenche o id
                AniversarianteDAO dao = new AniversarianteDAO(this); // nova instância de DAO
                try {
                    if(dao.excluir(aniv)) // sucesso na exclusão?
                        Toast.makeText(this, R.string.excluidoSucesso, Toast.LENGTH_SHORT).show(); // mensagem de sucesso
                    else
                        Toast.makeText(this, R.string.excluidoFalha, Toast.LENGTH_SHORT).show(); // mensagem de falha
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Intent intencao = new Intent(this, AniversariantesMain.class); // nova intenção para o menu de aniversariantes
                startActivity(intencao); // abre a atividade
            }
        }
    }
}
