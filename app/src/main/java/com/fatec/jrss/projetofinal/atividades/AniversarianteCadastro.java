package com.fatec.jrss.projetofinal.atividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.dominio.Aniversariante;
import com.fatec.jrss.projetofinal.util.ValidarCampos;
import com.fatec.jrss.projetofinal.util.VerificaCamposVazios;

import java.sql.SQLException;

/***
 *  Atividade onde o usuario pode cadastrar um novo aniversariante. Somente o campo e-mail
 *  pode ficar em branco, e somente valores válidos em dia e mês são aceitos.
 *  Botão Home = Menu Principal
 *  Botão Cancelar = Cancela o cadastro, voltando à tela anterior
 */

public class AniversarianteCadastro extends Activity implements View.OnClickListener {
    private EditText nome, email, dia, mes; // campos de texto
    private Button cadastrar, home, cancela; // botões
    private int tamanhoCampos = 2; // tamanho máximo dos campos da data


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariante_cadastro);

        // encontrar os EditTexts
        nome = (EditText) findViewById(R.id.txtCadNome);
        email = (EditText) findViewById(R.id.txtCadEmail);
        dia = (EditText) findViewById(R.id.txtCadDia);
        mes = (EditText) findViewById(R.id.txtCadMes);
        // aplicar o tamanho máximo do campo
        dia.setFilters(new InputFilter[] {new InputFilter.LengthFilter(tamanhoCampos)});
        mes.setFilters(new InputFilter[] {new InputFilter.LengthFilter(tamanhoCampos)});
        // encontrar os Buttons
        cadastrar = (Button) findViewById(R.id.btnCadastrar);
        home = (Button) findViewById(R.id.btnHome);
        cancela = (Button) findViewById(R.id.btnCancela);
        // configurar os botões para escutar os clicks
        cadastrar.setOnClickListener(this);
        home.setOnClickListener(this);
        cancela.setOnClickListener(this);


    }

    /***
     * Sobrescrita do método onClick para tratar os clicks nos botões
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == cadastrar.getId()) { // clickou em cadastrar?
            String nomeTexto = nome.getText().toString(); // pega a string do nome
            String diaTexto = dia.getText().toString(); // pega a string do dia
            String mesTexto = mes.getText().toString(); // pega a string do texto
            // verificar se  nome é vazio
            VerificaCamposVazios vcv = new VerificaCamposVazios(); // instância da verificação
            if(!vcv.verificarNome(nomeTexto, this)) // nome vazio?
                return; //interrompe cadastro

            // verificar se data é vazia
            if (!vcv.verificaDataVazia(diaTexto, mesTexto, this)) // dia ou mês estão vazios?
                return; // interrompe cadastro
            int diaV = Integer.valueOf(diaTexto); // converte dia para int
            int mesV = Integer.valueOf(mesTexto); // converte mês para int

            // verificar se data é válida
            ValidarCampos vc = new ValidarCampos(); // instância da validação
            if (!vc.validarData(diaV, mesV, this)) // dia ou mês inválidos?
                return; // interrompe cadastro

            Aniversariante aniv = new Aniversariante(); // nova instância de Aniversariante
            aniv.setNome(nomeTexto); // preenche o nome
            aniv.setDiaAniversario(diaV); // preenche o dia
            aniv.setMesAniversario(mesV); // preenche o mês
            aniv.setEmail(email.getText().toString()); // preenche o e-mail

            AniversarianteDAO aDao = new AniversarianteDAO(this); // nova instância de DAO

            try {
                if (aDao.salvar(aniv)) // sucesso ao salvar?
                    Toast.makeText(this, R.string.salvoSucesso, Toast.LENGTH_SHORT).show(); //mensagem de sucesso
                else
                    Toast.makeText(this, R.string.salvoFalha, Toast.LENGTH_SHORT).show(); // mensagem de erro
                Intent intencao = new Intent(this, AniversariantesMain.class); // intencão para o menu principal de aniversariantes
                startActivity(intencao); // inicia a atividade

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        Intent intent = null; // objeto de intenção
        if (v.getId() == home.getId()) // selecionou o botão home?
            intent = new Intent(this, MenuPrincipal.class); // intenção para o menu principal
        if(v.getId() == cancela.getId()) // selecionou o botão cancelar?
            intent = new Intent(this, AniversariantesMain.class); // intenção para o menu principal de aniversariantes
        if (intent != null) // existe intenção?
            startActivity(intent); // começar atividade
    }

}
