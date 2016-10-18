package com.wagner.valentin.notificationmaker2.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wagner.valentin.notificationmaker2.R;

/**
 * Created by Valentin on 27.09.2016.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public NotificationBroadcastReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();

        String actionDone = context.getString(R.string.notification_action_done);
        String actionDelete = context.getString(R.string.notification_action_delete);

        int id = intent.getIntExtra("notification_id", 0);

        Log.v("NotificationReceiver", "onReceive was actually called!");
        Log.e("NotificationReceiver", "onReceive was actually called!");

        //done action button was pressed
        if(action.equals(actionDone)){

            Storage.getAdapterActive(); //todo implement

        }else if(action.equals(actionDelete)){
            //delete action button was pressed
            Storage.Notification notification = Storage.findById(Storage.getActiveNotifications(), id);
            int index = Storage.getActiveNotifications().indexOf(notification);
            Storage.getAdapterActive().data.remove(index);
            Storage.getAdapterActive().notifyItemRemoved(index);
        }

    }

}
