package com.paper.webscrapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String numTarea = intent.getStringExtra("com.paper.webscrapper.AssignmentsFragment.TAG_TAREA");

        NotificationHelper notificationHelper = new NotificationHelper(context);

        String texto = "";

        if( numTarea.equals("1") ){
            texto = " tarea pendiente.";
        }else {
            texto = " tareas pendientes.";
        }

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification()
                .setContentText("Usted tiene " + numTarea + texto);

        notificationHelper.getManager().notify(1, nb.build());
    }
}
