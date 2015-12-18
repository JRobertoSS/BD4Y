package com.fatec.jrss.projetofinal.atividades;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.dao.AniversariantePresenteDAO;
import com.fatec.jrss.projetofinal.dao.IDAO;
import com.fatec.jrss.projetofinal.dao.PresenteDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.dominio.EntidadeDominio;
import com.fatec.jrss.projetofinal.dominio.Presente;
import com.fatec.jrss.projetofinal.util.FormataNumero;

import java.sql.SQLException;
/***
 * Atividade que mostra os detalhes de um presente selecionado, inclusive a lista dos
 * aniversariantes interessados. Um toque simples sobre o aniversariante apresenta uma mensagem com os detalhes, enquanto
 * um toque longo abre a atividade de detalhes com as informações.
 * Botão Home = Volta ao Menu Principal
 * Botão Presente = Volta à tela principal dos presentes
 */
public class PresenteDetalhes extends ListActivity implements View.OnClickListener {
    private int idOperacoes; // id para operações
    private TextView nome, preco; // visões para receber o texto do banco
    private SimpleCursorAdapter adaptador; // adaptador para a lista
    private Button home, presente; // botões

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentes_detalhes);
        // encontrar as visões
        nome = (TextView) findViewById(R.id.nomePresente);
        preco = (TextView) findViewById(R.id.precoPresente);
        home = (Button) findViewById(R.id.btnHome);
        presente = (Button) findViewById(R.id.btnPresentes);
        // adicionar o listener de onClick para os botões
        home.setOnClickListener(this);
        presente.setOnClickListener(this);
        // pegar os parâmetros
        Bundle extras = getIntent().getExtras();
        if (extras != null) // existem parâmetros?
            idOperacoes = extras.getInt("idP"); // pega o id do presente

        inicializarDados(this); // inicializa os dados do presente
        inicializarLista(this); // inicializa a lista de aniversariantes com este presente

        /***
         *  Método em uma classe anônima que trata um toque longo sobre um item da lista
         */
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view; // layout do item selecionado
                TextView idClicado = (TextView) layout.findViewById(R.id.idAniversariante); // id do item no layout
                Intent intencao = new Intent(PresenteDetalhes.this, AniversarianteDetalhes.class); // intenção para os detalhes do aniversariante selecionado
                intencao.putExtra("idA", Integer.valueOf(idClicado.getText().toString())); // passa o id do aniversariante como parâmetro
                startActivity(intencao); // começa a atividade
                return true; // indica que tratou o ItemLongClick, assim não executa o onListItemClick
            }
        });
    }
    /***
     * Método para inicializar os dados do Presente diretamente do banco de dados
     * @param contexto contexto para instanciar o DAO
     */
    public void inicializarDados(Context contexto) {
        PresenteDAO pDao = new PresenteDAO(contexto); // instância de DAO
        Presente pres = new Presente(); // instância de Presente
        pres.setId(idOperacoes); // preenche o ID do presente
        try {
            Cursor c = pDao.consultar(pres); // resultado da consulta do presente
            if (c.moveToNext()) { // houve resultado?
                nome.setText(c.getString(c.getColumnIndex(EnumColunas.NOME.getNome()))); // preenche o nome do presente
                Double valor = c.getDouble(c.getColumnIndex(EnumColunas.PRECO.getNome())); // pega o preço do presente
                preco.setText(valor.toString()); // preenche o preço do presente
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /***
     * Método que inicializa a lista de aniversariantes com o presente
     * @param contexto para a instância de DAO
     */
    private void inicializarLista(Context contexto) {
        AniversariantePresenteDAO apDao = new AniversariantePresenteDAO(contexto); // Instância de DAO
        Presente pres = new Presente(); // instância de Presente
        pres.setId(idOperacoes); // preenche o id do Presente

        try {
            Cursor c = apDao.aniversariantesComPresente(pres); // resultado da consulta dos aniversariantes com o presente
            String[] colunas = {EnumColunas.ID.getNome(), EnumColunas.NOME.getNome()}; // nomes das colunas
            int[] ids = {R.id.idAniversariante, R.id.nomeAniversariante}; // ids das colunas
            adaptador = new SimpleCursorAdapter(PresenteDetalhes.this, R.layout.list_row_aniversariante2,
                    c, colunas, ids, 0); // adaptador do ListView
            setListAdapter(adaptador); // inicializa a lista
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /***
     *  Sobrescrita do método que trata toque simples em um item da lista
     * @param l ListView da atividade
     * @param v Visão clicada
     * @param position Posição do item selecionado
     * @param id Id do item selecionado
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        LinearLayout layout = (LinearLayout) v; // layout do item selecionado
        TextView idClicado = (TextView) layout.findViewById(R.id.idAniversariante); // id do item no layout selecionado
        Aniversariante aniv = new Aniversariante(); // instância de Aniversariante
        aniv.setId(Integer.valueOf(idClicado.getText().toString())); // pega o id do item selecionado
        AniversarianteDAO dao = new AniversarianteDAO(this); // instância de DAO
        try {
            Cursor c = dao.consultar(aniv); // resultado da consulta do item selecionado
            if (c.moveToNext()) { // há resultado?
                String nome = c.getString(c.getColumnIndex(EnumColunas.NOME.getNome())); // pega o nome na consulta
                Integer diaAniversario = c.getInt(c.getColumnIndex(EnumColunas.DIA_ANIVERSARIO.getNome())); // pega o dia na consulta
                Integer mesAniversario = c.getInt(c.getColumnIndex(EnumColunas.MES_ANIVERSARIO.getNome())); // pega o mês na consulta

                FormataNumero formatar = new FormataNumero(); // instância de formatação
                String dia = formatar.formatarNumero(diaAniversario); // recebe o dia formatado
                String mes = formatar.formatarNumero(mesAniversario); // recebe o mês formatado
                String dataAniversario = dia + "/" + mes; // monta a data de aniversário

                String email = c.getString(c.getColumnIndex(EnumColunas.EMAIL.getNome())); // recebe o e-mail

                String mensagem = "Nome: " + nome + "\nAniversário: " + dataAniversario +
                        "\nE-mail: " + email; // monta a string da mensagem
                Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show(); // mostra a mensagem
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
        Intent intent = null; // intenção
        if (v.getId() == home.getId()) // selecionou home?
            intent = new Intent(this, MenuPrincipal.class); // itenção para o menu principal
        if (v.getId() == presente.getId()) // selecionou presentes?
            intent = new Intent(this, PresentesMain.class); // intenção para o menu de presentes
        if (intent != null) // existe intenção?
            startActivity(intent); // começa a atividade

    }
}