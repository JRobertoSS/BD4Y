package com.fatec.jrss.projetofinal.atividades;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.util.FormataNumero;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Atividade da Tela principal dos Aniversariantes. Exibe uma lista com todos os aniversariantes
 * cadastrados, e é possível utilizar o campo filtrar para a lista. Um toque simples em um dos aniversariantes
 * abre a tela de detalhes deste aniversariante, enquanto um toque longo possibilita alterar ou excluir.
 * Botão Home = Volta ao Menu Princpal
 * Botão Add Aniversariante = Possibilita cadastrar um novo aniversariante.
 */
public class AniversariantesMain extends ListActivity implements View.OnClickListener {
    private SimpleAdapter adaptador; // adaptador para a lista
    private EditText filtrar; // filtro de strings para a lista
    private Button home, addAniversariante; // botões


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes_main);

        atualizarLista(this); // atualiza a lista

        getListView().setTextFilterEnabled(true); // habilita o filtro

        filtrar = (EditText) findViewById(R.id.filtrar); // encontra a visão do filtro
        /***
         * Método de classe anônima para filtrar os resultados da lista a medida que o usuário digita no filtro
         */
        filtrar.addTextChangedListener(new TextWatcher() {
            @Override
            // antes da mudança
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            // durante a mudança
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s); // filtra os resultados da lista de acordo com a sequência de caracteres digitados no filtro
            }

            @Override
            // após a mudança
            public void afterTextChanged(Editable s) {

            }
        });
        /***
         * Método de classe anônima para tratar toques longos em um elemento da lista
         */
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view; // layout do item clicado
                TextView idClicado = (TextView) layout.findViewById(R.id.idAniversariante); // id do item clicado no layout (oculto)
                Intent intencao = new Intent(AniversariantesMain.this, AniversarianteAlteraExclui.class); // intenção para a atividade de Alteração/Exclusão
                intencao.putExtra("idA", Integer.valueOf(idClicado.getText().toString())); // passa o id do aniversariante como parâmetro
                startActivity(intencao); // começa a atividade
                return true; // indica que o toque longo foi tratado (evita a execução do onListItemClick)
            }
        });

        // encontra os botões
        addAniversariante = (Button) findViewById(R.id.btnAdd);
        home = (Button) findViewById(R.id.btnHome);
        // adiciona listener de onClick aos botões
        addAniversariante.setOnClickListener(this);
        home.setOnClickListener(this);

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
        LinearLayout layout = (LinearLayout) v;
        TextView idClicado = (TextView) layout.findViewById(R.id.idAniversariante);
        Intent intencao = new Intent(AniversariantesMain.this, AniversarianteDetalhes.class);
        intencao.putExtra("idA", Integer.valueOf(idClicado.getText().toString()));
        startActivity(intencao);

    }
    /***
     * Método que atualiza a lista de aniversariantes
     * @param contexto para a instância de DAO
     */
    public void atualizarLista(Context contexto) {
        AniversarianteDAO aDao = new AniversarianteDAO(contexto); // nova instância de DAO

        try {
            Cursor c = aDao.listar(); // resultado da consulta
            String[] colunas = {EnumColunas.ID.getNome(), EnumColunas.NOME.getNome(),
                    EnumColunas.DIA_ANIVERSARIO.getNome(),EnumColunas.MES_ANIVERSARIO.getNome() ,EnumColunas.EMAIL.getNome()}; // nomes das colunas
            int[] ids = {R.id.idAniversariante, R.id.nomeAniversariante, R.id.diaAniversario, // ids das colunas no ListView
                    R.id.mesAniversario, R.id.emailAniversariante};
            // Utilizar uma Lista com Mapas de chave e valor, para possibilitar o uso do filtro
            List<Map<String, String>> lista = new ArrayList<>(); // lista de mapa de string
            while (c.moveToNext()) { // enquanto houver resultados
                Map<String, String> temp = new HashMap<>(); // novo mapa de string para armazenar os resultados

                // associar no mapa os nomes das chaves com seus valores respectivos
                temp.put(EnumColunas.ID.getNome(), String.valueOf(c.getInt(c.getColumnIndex(EnumColunas.ID.getNome())))); //associa o id
                temp.put(EnumColunas.NOME.getNome(), c.getString(c.getColumnIndex(EnumColunas.NOME.getNome()))); // associa o nome

                FormataNumero formata = new FormataNumero(); // para formatar os valores de dia e mes

                Integer valor = c.getInt(c.getColumnIndex(EnumColunas.DIA_ANIVERSARIO.getNome())); // pega o valor do dia
                String format = formata.formatarNumero(valor); // formata o dia
                temp.put(EnumColunas.DIA_ANIVERSARIO.getNome(), format); // associa o dia

                valor = c.getInt(c.getColumnIndex(EnumColunas.MES_ANIVERSARIO.getNome())); // pega o valor do mes
                format = formata.formatarNumero(valor); // formata o mes
                temp.put(EnumColunas.MES_ANIVERSARIO.getNome(), format); // associa o mes

                temp.put(EnumColunas.EMAIL.getNome(), c.getString(c.getColumnIndex(EnumColunas.EMAIL.getNome()))); // associa o e-mail

                lista.add(temp); // adiciona o elemento a lista
            }

            adaptador = new SimpleAdapter(contexto, lista, R.layout.list_row_aniversariante,
                    colunas, ids); // adaptador da lista de mapa de string para o ListView.
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
        if (v.getId() == addAniversariante.getId()) // selecionou adicionar aniversariantes?
            intencao = new Intent(this, AniversarianteCadastro.class); // intenção para o cadastro de aniversariantes
        if(v.getId() == home.getId()) // selecionou home?
            intencao = new Intent(this, MenuPrincipal.class); // intencão para o menu principal
        if (intencao != null) // existe intenção?
            startActivity(intencao); // começa a atividade
    }

}
