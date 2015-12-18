package com.fatec.jrss.projetofinal.servicos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Serviço parou", "=(");
        context.startService(new Intent(context, ServicoNotificacao.class));
    }
}
