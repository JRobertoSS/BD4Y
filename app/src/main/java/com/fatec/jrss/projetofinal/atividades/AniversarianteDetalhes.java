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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Atividade que mostra os detalhes de um aniversariante selecionado, inclusive a sua lista
 * de presentes. Um toque simples sobre o presente apresenta uma mensagem com os detalhes, enquanto
 * um toque longo possibilita excluir o presente da lista (caso confirmado pelo usuário.
 * Botão Home = Volta ao Menu Principal
 * Botão Add Presente = Possibilita adicionar presentes cadastrados à lista deste aniversariante
 */
public class AniversarianteDetalhes extends ListActivity implements View.OnClickListener {
    private int idAniversariante, idPresente; // recebe ids de outras atividades
    private TextView nome, dia, mes, email; // recebem os dados do banco
    private Button addPresente, home; // botões
    private static final int ID_REQUISICAO = 500; // ID da requisição para o startActivityForResult

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariante_detalhes);
        // encontrar os TextViews
        nome = (TextView) findViewById(R.id.nomeAniversariante);
        dia = (TextView) findViewById(R.id.diaAniversario);
        mes = (TextView) findViewById(R.id.mesAniversario);
        email = (TextView) findViewById(R.id.emailAniversariante);
        // encontrar os botões
        addPresente = (Button) findViewById(R.id.btnAdd);
        home = (Button) findViewById(R.id.btnHome);
        // adicionar os botões ao listener de clicks
        addPresente.setOnClickListener(this);
        home.setOnClickListener(this);
        // pegar os parâmetros
        Bundle extras = getIntent().getExtras();
        if (extras != null) // existem parâmetros?
            idAniversariante = extras.getInt("idA"); // pegar o id do aniversariante
        // inicializar a lista de presentes
        inicializarLista(this);
        // inicializar os dados do aniversariante
        inicializarDados(this);

        /***
         *  Método em uma classe anônima que trata um toque longo sobre um item da lista
         */
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view; // layout do item clicado
                TextView idClicado = (TextView) layout.findViewById(R.id.idPresente); // id do presente clicado na lista (oculto)
                idPresente = Integer.parseInt(idClicado.getText().toString()); // converter para int e armazenar
                Intent intencao = new Intent(AniversarianteDetalhes.this, Confirmacao.class); // nova intenção para tela de confirmação
                intencao.putExtra("idP", idPresente); // passar o id do presente como parâmetro
                intencao.putExtra("msg", getResources().getString(R.string.confirmaExcluirLista)); // passar a mensagem como parâmetro
                startActivityForResult(intencao, ID_REQUISICAO); // começar a atividade e aguardar o resultado
                return true; // indicar que o click longo foi tratado, para evitar a execução do onListItemClick após este
            }
        });
    }

    /***
     * Método para inicializar os dados do Aniversariante diretamente do banco de dados
     * @param contexto contexto para instanciar o DAO
     */
    private void inicializarDados(Context contexto) {
        AniversarianteDAO aDao = new AniversarianteDAO(contexto); // nova instância de DAO
        Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
        aniv.setId(idAniversariante); // preenche o id do aniversariante

        try {
            Cursor c = aDao.consultar(aniv); // armazena o resultado da consulta
            if (c.moveToNext()) { // existe resultado?
                nome.setText(c.getString(c.getColumnIndex(EnumColunas.NOME.getNome()))); // preenche o nome com o valor da consulta

                Integer vDia = c.getInt(c.getColumnIndex(EnumColunas.DIA_ANIVERSARIO.getNome())); // pega o valor dia da consulta

                FormataNumero formata = new FormataNumero(); // nova instância de FormataNumero

                String d = formata.formatarNumero(vDia); // formata o dia
                dia.setText(d); // preenche o dia com o valor formatado

                Integer vMes = c.getInt(c.getColumnIndex(EnumColunas.MES_ANIVERSARIO.getNome())); // pega o valor mes da consulta
                String m = formata.formatarNumero(vMes); // formata o mês
                mes.setText(m); // preenche o mes com o valor formatado

                email.setText(c.getString(c.getColumnIndex(EnumColunas.EMAIL.getNome()))); // preenche o e-mail
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * Método que inicializa a lista de presentes do aniversariante
     * @param contexto para a instância de DAO
     */
    private void inicializarLista(Context contexto) {

        AniversariantePresenteDAO apDao = new AniversariantePresenteDAO(contexto); // nova inst&ancia de DAO
        Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
        aniv.setId(idAniversariante); // preenche o id do aniversariante
        try {
            Cursor c = apDao.presentesDoAniversariante(aniv); // resultado da consulta dos presentes de um determinado aniversariante
            String[] colunas = {EnumColunas.ID.getNome(), EnumColunas.NOME.getNome()}; // nomes das colunas
            int[] ids = {R.id.idPresente, R.id.nomePresente}; // ids das colunas no layout da lista
            SimpleCursorAdapter adaptador = new SimpleCursorAdapter(contexto, R.layout.list_row_presente2,
                    c, colunas, ids, 0); // adaptador do resultado do banco para a lista
            setListAdapter(adaptador); // inicializa a lista
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
        Intent intencao = null; // intenção
        if (v.getId() == addPresente.getId()) { // selecionou adicionar um presente?
            intencao = new Intent(this, AddPresenteDoAniversariante.class); // intenção para a nova atividade Adicionar Presentes ao Aniversariante
            intencao.putExtra("idA", idAniversariante); // passa o id do aniversariante como parâmetro
            startActivity(intencao); // começa a atividade
        }
        if (v.getId() == home.getId()) { // selecionou o botão home?
            intencao = new Intent(this, MenuPrincipal.class); // intenção para a atividade do menu principal
            startActivity(intencao); // começa a atividade
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
        TextView idClicado = (TextView) layout.findViewById(R.id.idPresente); // id do item selecionado no layout
        idPresente = Integer.parseInt(idClicado.getText().toString()); //converte para int e armazena o id
        Presente pres = new Presente(); // nova instância de Presente
        pres.setId(idPresente); // preenche o id do presente
        PresenteDAO dao = new PresenteDAO(this); // nova instância de DAO
        try {
            Cursor c = dao.consultar(pres); // resultado da consulta
            if (c.moveToNext()) { // houve resultado?
                String nomePresente = c.getString(c.getColumnIndex(EnumColunas.NOME.getNome())); // armazena o nome
                Double precoPresente = c.getDouble(c.getColumnIndex(EnumColunas.PRECO.getNome())); // armazena o preço
                Toast.makeText(this, "Nome: " + nomePresente + "\nPreço: " + precoPresente, Toast.LENGTH_SHORT).show(); // mensagem com os dados
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                Bundle extras = data.getExtras(); // pega os dados da resposta
                if (extras != null) { // há dados na resposta?
                    idPresente = extras.getInt("idP"); // pega o id do presente
                    Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
                    Presente pres = new Presente(); // nova instância de presente
                    pres.setId(idPresente); // preenche o id do presente
                    aniv.setId(idAniversariante); // preenche o id do aniversariante
                    aniv.addPresente(pres); // adiciona o presentete ao aniversariante para, a partir do id, excluí-lo no banco
                    AniversariantePresenteDAO dao = new AniversariantePresenteDAO(this); // DAO para realizar a exclusão do presente do aniversariante
                    try {
                        if (dao.excluir(aniv)) { // sucesso ao excluir?
                            inicializarLista(this); // atualizar a lista
                            Toast.makeText(this, R.string.presenteExcluidoLista, Toast.LENGTH_SHORT).show(); // mensagem de sucesso
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
