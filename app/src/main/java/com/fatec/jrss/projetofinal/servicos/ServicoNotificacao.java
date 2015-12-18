package com.fatec.jrss.projetofinal.servicos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fatec.jrss.projetofinal.R;
import com.fatec.jrss.projetofinal.atividades.AniversariantesMain;
import com.fatec.jrss.projetofinal.atividades.AniversariantesMes;
import com.fatec.jrss.projetofinal.dao.AniversarianteDAO;
import com.fatec.jrss.projetofinal.db.Colunas.EnumColunas;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/***
 * Classe do serviço de notificações. Utiliza de um timer pré configurado para disparar as notificações (capturando as
 * configurações do configuracoes.xml
 */
public class ServicoNotificacao extends Service {
    private Timer mTimer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Log.i("Log", "Serviço de notificação executado");
            notificar();
        }
    };

    public ServicoNotificacao() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 2000, 1 * 60000); // seta o tempo das notificações no padrao de 1 min (tempoNotificacao * 1 min em ms)
    }

    @Override
    public void onDestroy() {
        try {
            mTimer.cancel();
            timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.fatec.jrss.projetofinal");
        sendBroadcast(intent);
    }


    public void notificar() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); // pega as configurações
        // verificar se foi configurado para enviar notificações. Caso não, sair do método.
        boolean notificar = SP.getBoolean("notificacoes", false);
        if (!notificar)
            return;
        Calendar cal = Calendar.getInstance();
        AniversarianteDAO dao = new AniversarianteDAO(this);
        Cursor c;
        int dia, mes;
        boolean notificarAniversariantes = false;
        boolean notificarAniversariantesAtrasados =  false;
        String textoAniversario;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle(); // para adicionar os nomes dos aniversariantes à notificação
        inboxStyle.addLine("");
        try {
            c = dao.listar();
            while (c.moveToNext()) {
                // pegar o dia e mês do sistema, para ver se há aniversariantes para enviar a notificação
                dia = c.getInt(c.getColumnIndex(EnumColunas.DIA_ANIVERSARIO.getNome()));
                mes = c.getInt(c.getColumnIndex(EnumColunas.MES_ANIVERSARIO.getNome()));
                if ((mes == cal.get(Calendar.MONTH) + 1)) { // existem aniversariantes este mês?
                    if (cal.get(Calendar.DAY_OF_MONTH) <= dia) { // o aniversário não chegou ainda (não é atrasado)?
                        textoAniversario = c.getString(c.getColumnIndex(EnumColunas.NOME.getNome())) + " " + dia + "/" + mes;
                        inboxStyle.addLine(textoAniversario);
                        notificarAniversariantes = true;
                    } else if (SP.getBoolean("atrasados", false)){ // foi configurado para notificar aniversários atrasados?
                        textoAniversario = c.getString(c.getColumnIndex(EnumColunas.NOME.getNome())) + " (Atrasado!) " + dia + "/" + mes;
                        inboxStyle.addLine(textoAniversario);
                        notificarAniversariantesAtrasados = true;
                    }
                }
            }
            inboxStyle.addLine("");
            inboxStyle.addLine("Clique aqui para visualizá-los!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (notificarAniversariantes || notificarAniversariantesAtrasados) { // evita de enviar uma notificação em branco, caso haja aniversariantes atrasados, mas não foi configurado para mostrá-los
            // construir a notificação
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_bolo_notificacao); //seta ícone
            builder.setContentTitle("Aniversariantes do mês: "); //seta título
            builder.setStyle(inboxStyle); // seta todas mensagens adicionadas no loop anterior com o cursor
            Intent intencao = new Intent(this, AniversariantesMes.class); //seta a inteção pra abrir aniversariantes do mes ao clicar na notificação
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(AniversariantesMain.class);
            stackBuilder.addNextIntent(intencao);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, builder.build());
        }

    }
}
