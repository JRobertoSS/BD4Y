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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.dao.IDAO;
import com.fatec.jrss.projetofinal.dao.PresenteDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.dominio.Presente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/***
 * Atividade da Tela principal dos presentes. Exibe uma lista com todos os presentes
 * cadastrados, e é possível utilizar o campo filtrar para a lista. Um toque simples em um dos presentes
 * abre a tela de detalhes deste presente, enquanto um toque longo possibilita alterar ou excluir.
 * Botão Home = Volta ao Menu Princpal
 * Botão Add Presente = Possibilita cadastrar um novo presente.
 */
public class PresentesMain extends ListActivity implements View.OnClickListener{
    private SimpleAdapter adaptador; // adaptador da lista
    private EditText filtrar; // campo para filtrar a lista
    private Button addPresente, home; // botões

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentes_main);

        atualizarLista(this); // atualizar a lista

        getListView().setTextFilterEnabled(true); // habilita a filtragem da lista

        filtrar = (EditText) findViewById(R.id.filtrar); // encontra a visão do filtro
        /***
         * Método de classe anônima para filtrar os resultados da lista a medida que o usuário digita no filtro
         */
        filtrar.addTextChangedListener(new TextWatcher() {
            // antes da mudança
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            // durante a mudança
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s); // filtra os resultados da lista de acordo com a sequência de caracteres digitados no filtro
            }
            // após a mudança
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /***
         * Método de classe anônima para tratar toques longos em um elemento da lista
         */
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view; // layout do item selecionado
                TextView idClicado = (TextView) layout.findViewById(R.id.idPresente); // id no layout do item selecionado
                Intent intencao = new Intent(PresentesMain.this, PresenteAlteraExclui.class); // intenção para a tela de Alteração/Exclusão de presentes
                intencao.putExtra("idP", Integer.valueOf(idClicado.getText().toString())); // passa o ID do presente como parâmetro
                startActivity(intencao); // começa a ativdidade
                return true; // indica que o toque longo foi tratado (evita a execução do onListItemClick)
            }
        });
        // encontra os botões
        addPresente = (Button) findViewById(R.id.btnAdd);
        home = (Button) findViewById(R.id.btnHome);
        // adiciona o listener de onClick para os botões
        addPresente.setOnClickListener(this);
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
        LinearLayout layout = (LinearLayout) v; // layout do item selecionado
        TextView idClicado = (TextView) layout.findViewById(R.id.idPresente); // id do item no layout selecionado
        Intent intencao = new Intent(PresentesMain.this, PresenteDetalhes.class); // intenção para os Detalhes do presente selecionado
        intencao.putExtra("idP", Integer.valueOf(idClicado.getText().toString())); // passa o id do presente como parâmetro
        startActivity(intencao); // começa a atividade

    }
    /***
     * Método que atualiza a lista de presentes
     * @param contexto para a instância de DAO
     */
    public void atualizarLista(Context contexto) {
        PresenteDAO pDao = new PresenteDAO(contexto); // instâancia de DAO

        try {
            Cursor c = pDao.listar(); // resultado da listagem de presentes
            String[] colunas = {EnumColunas.ID.getNome(), EnumColunas.NOME.getNome(), EnumColunas.PRECO.getNome()}; // nome das colunas
            int[] ids = {R.id.idPresente, R.id.nomePresente, R.id.precoPresente}; // ids das colunas
            // Utilizar uma Lista com Mapas de chave e valor, para possibilitar o uso do filtro
            List<Map<String, String>> lista = new ArrayList<>(); // Lista de Mapas de String
            while(c.moveToNext()){ // enquanto houver resultado
                Map<String, String> temp = new HashMap<>(); // novo mapa de string para armazenar os resultados

                // associar nos mapas os nomes das chaves com seus valores respectivos
                temp.put(EnumColunas.ID.getNome(),
                        String.valueOf(c.getInt(c.getColumnIndex(EnumColunas.ID.getNome())))); // associa o ID
                temp.put(EnumColunas.NOME.getNome(), c.getString(c.getColumnIndex(EnumColunas.NOME.getNome()))); // associa o nome
                temp.put(EnumColunas.PRECO.getNome(),
                        String.valueOf(c.getDouble(c.getColumnIndex(EnumColunas.PRECO.getNome())))); // associa o preço
                lista.add(temp); // adiciona o mapa a lista
            }
            adaptador = new SimpleAdapter(contexto, lista, R.layout.list_row_presente,
                    colunas, ids); // adaptador para a lista
            setListAdapter(adaptador); // inicia a lista
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
        if (v.getId() == addPresente.getId()) // selecionou adicionar um presente?
            intencao = new Intent(this, PresenteCadastro.class); // intenção para o cadastro de presentes
        if(v.getId() == home.getId()) // selecionou home?
            intencao = new Intent(this, MenuPrincipal.class); // intenção para o menu principal
        if (intencao != null) // existe intenção?
            startActivity(intencao); // começar atividade
    }

}
