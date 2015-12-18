package com.fatec.jrss.projetofinal.atividades;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.fatec.jrss.projetofinal.R;

/***
 * Atividade das configurações. Detalhes destas no próprio R.xml.configuracoes
 */
public class Configuracoes extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configuracoes);
    }
}