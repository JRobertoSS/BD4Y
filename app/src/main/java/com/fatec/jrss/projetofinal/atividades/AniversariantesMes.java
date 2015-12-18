package com.fatec.jrss.projetofinal.atividades;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.dao.AniversariantePresenteDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.util.FormataNumero;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Atividade onde aparecem todos os aniversariantes do mês. Caso a opção Aniversariantes Atrasados esteja desmarcada,
 * somente os aniversários  que ainda não chegaram são mostrados.
 * Botão Home = Menu Principal
 */
public class AniversariantesMes extends ListActivity implements View.OnClickListener {
    private EditText filtrar; // EditText para filtrar resultados na lista
    private SimpleAdapter adaptador; // adaptador para preencher a lista
    private Calendar calendar = Calendar.getInstance(); // pega a data atual do sistema
    private Button home; // botão home, para voltar ao menu principal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes_mes);

        home = (Button) findViewById(R.id.btnHome); // encontra o botão home
        home.setOnClickListener(this); // adiciona o listener para o método onClick

        atualizarLista(this); // atualiza a lista

        getListView().setTextFilterEnabled(true); // habilita o filtro na lista

        filtrar = (EditText) findViewById(R.id.filtrar); // encontra o filtro
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
            // aoós a mudança
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
                LinearLayout layout = (LinearLayout) view;
                TextView idClicado = (TextView) layout.findViewById(R.id.idAniversariante);
                Intent intencao = new Intent(AniversariantesMes.this, AniversarianteDetalhes.class);
                intencao.putExtra("idA", Integer.valueOf(idClicado.getText().toString()));
                startActivity(intencao);
                return true;
            }
        });

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
        LinearLayout layout = (LinearLayout) v; // layout clicado
        TextView idAniversariante = (TextView) v.findViewById(R.id.idAniversariante); // id do item clicado no layout

        AniversariantePresenteDAO apDao = new AniversariantePresenteDAO(this); // nova instância de DAO
        Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
        aniv.setId(Integer.valueOf(idAniversariante.getText().toString())); // preenche o id do aniversariante
        String presentes = "Nenhum presente associado."; // para mostrar a lista de presentes do aniversariante selecionado
        try {
            Cursor c = apDao.presentesDoAniversariante(aniv); // resultados
            if (c.getCount() > 0) // se houver resultados
                presentes = "Presentes:"; // inicializa a mensagem
            while (c.moveToNext()) { // enquanto houver resultados
                // montar a mensagem
                presentes += "\nNome: ";
                presentes += c.getString(c.getColumnIndex(EnumColunas.NOME.getNome()));
                presentes += " Preço: ";
                presentes += String.valueOf(c.getDouble(c.getColumnIndex(EnumColunas.PRECO.getNome())));
            }
            Toast.makeText(this, presentes, Toast.LENGTH_LONG).show(); // mostrar a mensagem
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /***
     * Método que atualiza a lista de aniversariantes
     * @param contexto para a instância de DAO
     */
    public void atualizarLista(Context contexto) {
        AniversarianteDAO aDao = new AniversarianteDAO(contexto); // instância de DAO
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); // pegar as configurações

        try {
            Cursor c = aDao.listar(); // resultado da listagem
            String[] colunas = {EnumColunas.ID.getNome(), EnumColunas.NOME.getNome(),
                    EnumColunas.DIA_ANIVERSARIO.getNome(), EnumColunas.MES_ANIVERSARIO.getNome(), EnumColunas.EMAIL.getNome()}; // nomes das colunas
            int[] ids = {R.id.idAniversariante, R.id.nomeAniversariante, R.id.diaAniversario,
                    R.id.mesAniversario, R.id.emailAniversariante}; // ids das colunas na lista
            List<Map<String, String>> lista = new ArrayList<>(); // mapa de String para relacionar as chaves das colunas com seus valores
            while (c.moveToNext()) { // equanto houver resultados
                Integer dia = c.getInt(c.getColumnIndex(EnumColunas.DIA_ANIVERSARIO.getNome())); // pega o dia do aniversário
                Integer mes = c.getInt(c.getColumnIndex(EnumColunas.MES_ANIVERSARIO.getNome())); // pega o mês do aniversário
                Integer diaSistema = calendar.get(Calendar.DAY_OF_MONTH); // pega o dia do sistema
                Integer mesSistema = (calendar.get(Calendar.MONTH) + 1); // pega o mês do sistema
                if (mes == mesSistema) { // é aniversariante do mês?
                    Map<String, String> temp = new HashMap<>(); // mapa de strings para salvar as chaves e valores
                    temp.put(EnumColunas.ID.getNome(), String.valueOf(c.getInt(c.getColumnIndex(EnumColunas.ID.getNome())))); // associa a chave ao id

                    FormataNumero formata = new FormataNumero(); // para formatar os valores de dia e mês

                    String format = formata.formatarNumero(dia); // formata o dia
                    temp.put(EnumColunas.DIA_ANIVERSARIO.getNome(), format); // associa a chave ao dia

                    format = formata.formatarNumero(mes); // formata o mes
                    temp.put(EnumColunas.MES_ANIVERSARIO.getNome(), format); // associa a chave ao mes

                    temp.put(EnumColunas.EMAIL.getNome(), c.getString(c.getColumnIndex(EnumColunas.EMAIL.getNome()))); // associa a chave ao e-mail

                    if (diaSistema <= dia) { // aniversário ainda não passou?
                        // associa a chave ao nome
                        temp.put(EnumColunas.NOME.getNome(), c.getString(c.getColumnIndex(EnumColunas.NOME.getNome())));
                        lista.add(temp); // adiciona a lista
                    } else if(SP.getBoolean("atrasados", false)){ // foi configurado para mostrar Aniversários Atrasados?
                        // associa a chave ao nome, indicado que o aniversário está atrasado
                        temp.put(EnumColunas.NOME.getNome(), c.getString(c.getColumnIndex(EnumColunas.NOME.getNome())) + " (Atrasado!) ");
                        lista.add(temp); // adiciona a lista
                    }
                }
            }

            adaptador = new SimpleAdapter(contexto, lista, R.layout.list_row_aniversariante,
                    colunas, ids); // adaptador para o ListView
            setListAdapter(adaptador); // preenche o ListView
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
        if (v.getId() == home.getId()) // selecionou o botão home?
            intencao = new Intent(this, MenuPrincipal.class); // intenção para o menu principal
        if (intencao != null) // existe intenção?
            startActivity(intencao); // começa a atividade
    }
}