package com.fatec.jrss.projetofinal.atividades;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.fatec.jrss.projetofinal.dao.AniversariantePresenteDAO;
import com.fatec.jrss.projetofinal.dao.PresenteDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.dominio.Presente;

import java.sql.SQLException;

/***
 * Atividade onde aparece a lista de todos os presentes cadastrados, e o usuário pode realizar um toque
 * simples para visualizar em uma mensagem o nome e preço do produto, ou com um toque longo adicionar
 * este presente à lista do aniversariante em questão. Não é possível adicionar o mesmo presente 2x.*
 * Botão Home = Menu Principal
 * Botão Aniversariante = Volta aos detalhes do aniversariante
 */
public class AddPresenteDoAniversariante extends ListActivity implements View.OnClickListener {
    private int idPresente, idAniversariante; //ids para trabalhar com o banco de dados
    private SimpleCursorAdapter adaptador; // adaptador da consulta do banco de dados
    private Button home, aniversariante; // botões de navegação

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_presente_do_aniversariante);

        // encontar as visões respectivas
        home = (Button) findViewById(R.id.btnHome);
        aniversariante = (Button) findViewById(R.id.btnAniversarianteDetalhes);
        // habilitar listener de click para os botões
        home.setOnClickListener(this);
        aniversariante.setOnClickListener(this);
        // atualizar a lista de presentes
        atualizarLista(this);
        // receber os parâmetros da outra atividade
        Bundle extras = getIntent().getExtras();
        if (extras != null) // existem parâmetros?
            idAniversariante = extras.getInt("idA"); //pega o id do aniversariante

    }

    /***
     *  Método que atualiza o ListView com todos os presentes do banco de dados
     * @param contexto contexto para instanciar o DAO e realizar a consulta no banco
     */
    public void atualizarLista(Context contexto) {
        PresenteDAO aDao = new PresenteDAO(contexto); // nova instância do DAO para consulta

        try {
            Cursor c = aDao.listar(); // resultado da listagem de todos os presentes
            String[] colunas = {EnumColunas.ID.getNome(), EnumColunas.NOME.getNome(), EnumColunas.PRECO.getNome()}; // nome das colunas
            int[] ids = {R.id.idPresente, R.id.nomePresente, R.id.precoPresente}; // ids das visões de uma coluna na ListView
            adaptador = new SimpleCursorAdapter(contexto, R.layout.list_row_presente,
                    c, colunas, ids, 0); // nova instãncia de adaptador para a ListView
            setListAdapter(adaptador); // associa o adaptador à lista
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /***
         *  Método em uma classe anônima que trata um toque longo sobre um item da lista
         */
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view; // layout da linha selecionada
                TextView idClicado = (TextView) layout.findViewById(R.id.idPresente); // id do presente selecionado (oculto na lista)
                idPresente = Integer.valueOf(idClicado.getText().toString()); // conversão para int
                AniversariantePresenteDAO apDAO = new AniversariantePresenteDAO(AddPresenteDoAniversariante.this); // nova instância do DAO
                Aniversariante aniv = new Aniversariante(); // novo objeto de aniversariante
                Presente pres = new Presente(); // novo objeto de presente
                aniv.setId(idAniversariante); // associa o id do aniversariante (recebido da outra atividade)
                pres.setId(idPresente); // associa o id do presente selecionado na lista
                aniv.addPresente(pres); // associa o presente ao aniversariante

                try {
                    if (apDAO.salvar(aniv)) //sucesso ao salvar?
                        Toast.makeText(AddPresenteDoAniversariante.this, R.string.sucessoAddPresente, Toast.LENGTH_SHORT).show(); // mensagem de sucesso
                    else
                        Toast.makeText(AddPresenteDoAniversariante.this, R.string.falhaAddPresente, Toast.LENGTH_SHORT).show(); // mensagem de falha
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true; // indica que o toque longo foi tratato (não permite que seja executado o onListItemClick na sequência)
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
        LinearLayout layout = (LinearLayout) v; // Layout selecionado
        TextView idClicado = (TextView) layout.findViewById(R.id.idPresente); // Id do presente selecionado
        idPresente = Integer.parseInt(idClicado.getText().toString()); // pega o Id selecionado e converte para int
        Presente pres = new Presente(); // nova instância de presente
        pres.setId(idPresente); // associa o id selecionado ao objeto de presente
        PresenteDAO dao = new PresenteDAO(this); // nova instância de DAO
        try {
            Cursor c = dao.consultar(pres); // consulta o presente de acordo com o id previamente selecionado
            if (c.moveToNext()) { // há resultado?
                String nomePresente = c.getString(c.getColumnIndex(EnumColunas.NOME.getNome())); // pega o nome
                Double precoPresente = c.getDouble(c.getColumnIndex(EnumColunas.PRECO.getNome())); // pega o preco
                Toast.makeText(this, "Nome: " + nomePresente + "\nPreço: " + precoPresente, Toast.LENGTH_SHORT).show(); // mostra os detalhes
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
        Intent intencao = null; //objeto de intencão
        if (v.getId() == home.getId()) // tocou o botão home?
            intencao = new Intent(this, MenuPrincipal.class); // configura a intenção para o menu principal
        if (v.getId() == aniversariante.getId()) { // tocou o botão aniversariante?
            intencao = new Intent(this, AniversarianteDetalhes.class); // configura a intenção para os detalhes do aniversariante
            intencao.putExtra("idA", idAniversariante); // passa o parâmetro Id do aniversariante
        }
        if (intencao != null) // existe intenção?
            startActivity(intencao); // começa a próxima atividade
    }
}