package com.wagner.valentin.notificationmaker2.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.NotificationCompat;

import com.wagner.valentin.notificationmaker2.MainActivity;
import com.wagner.valentin.notificationmaker2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Valentin on 23.09.2016.
 */

public class NotificationFactory {

    private static NotificationManager notificationManager;

    private static Context context;

    private static Drawable icon;

    public static void setup(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationFactory.context = context;
    }

    public static void create(int id, String title, String subTitle, int color){

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(NotificationFactory.context);

        //click pending intent
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //done pending intent
        Intent done = new Intent("com.valentin.wagner.notificationmaker2.notifications.NotificationBroadcastReceiver");
        done.setAction(context.getString(R.string.notification_action_done));
        done.putExtra("notification_id", id);
        PendingIntent pendingDone = PendingIntent.getBroadcast(context.getApplicationContext(), 123456, done, PendingIntent.FLAG_UPDATE_CURRENT);

        //delete pending intent
        Intent delete = new Intent("com.valentin.wagner.notificationmaker2.notifications.NotificationBroadcastReceiver");
        delete.setAction(context.getString(R.string.notification_action_done));
        delete.putExtra("notification_id", id);
        PendingIntent pendingDelete = PendingIntent.getBroadcast(context.getApplicationContext(), 123456, delete, PendingIntent.FLAG_UPDATE_CURRENT);


        //build the notification
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(subTitle)
                .setColor(color)
                .setContentIntent(pendingIntent)
         //       .addAction(R.mipmap.ic_delete_black_24dp, "Delete", pendingDelete)
         //       .addAction(R.mipmap.ic_done_black_24dp, "Done", pendingDone)
                .setTicker(title)
                .setOngoing(true);

        notificationManager.notify(id, notificationBuilder.build());
    }


    /**
     * Remove a notification.
     * @param id
     */
    public static void remove(int id){
        notificationManager.cancel(id);
    }

    /**
     * Remove all the notifications from the Arraylist with Storate.Notification objects
     * @param notifications
     */
    public static void removeAll(ArrayList<Storage.Notification> notifications){
        for(Storage.Notification notification : notifications){
            remove(notification.id);
        }
    }

    /**
     * Cancel all notifications
     */
    public static void removeAll(){
        notificationManager.cancelAll();
    }

    /**
     * creates an unique id to use when creating notifications
     * @return the Id
     */
    public static int createId(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.GERMANY).format(now));
        return id;
    }

}
