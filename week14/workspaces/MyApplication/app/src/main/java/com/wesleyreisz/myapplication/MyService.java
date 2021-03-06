package com.wesleyreisz.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by wesleyreisz on 11/16/14.
 */
public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TEST","Started Service");

        Bundle bundle = intent.getExtras();
        String message = (String)bundle.get(MainActivity.MESSAGE);

        new Thread(new CounterRunnable(message)).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("TEST","Stopped Service");
        super.onDestroy();
    }

    private class CounterRunnable implements Runnable{
        String mMessage;
        public CounterRunnable(String message){
            this.mMessage = message;
        }
        @Override
        public void run() {
            int count = 0;

            //get access to the system service that manages notifications
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            while(count<=5){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("TEST"," Message: " + ++count);
            }

            //build up your notification. btw, this is a builder pattern
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext());


            PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    new Intent(getApplicationContext(), MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
                );


            mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
            mBuilder.setContentTitle(mMessage + " Notification " + count);
            mBuilder.setContentText(mMessage + ": " + count);
            mBuilder.setContentIntent(resultPendingIntent);

            //send it
            notificationManager.notify(
                count,
                mBuilder.build()
            );
        }
    }
}
