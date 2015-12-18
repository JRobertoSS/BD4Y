package com.fatec.jrss.projetofinal;

import android.app.Application;

import com.fatec.jrss.projetofinal.db.DBHelper;

/**
 * Classe que gerencia a instância do singleton da conexão do banco de dados, abrindo-a
 * ao iniciar o programa e fechando-a ao finalizar.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBHelper.getInstance(this).close();
    }
}
