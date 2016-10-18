package com.wagner.valentin.notificationmaker2.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wagner.valentin.notificationmaker2.list.ListAdapter;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Valentin on 23.09.2016.
 */

public class Storage {

    private static ArrayList<Notification> notifications;

    private static SharedPreferences sharedPreferences;

    private static ArrayList<Notification> activeNotifications = new ArrayList<>();
    private static ArrayList<Notification> inactiveNotifications = new ArrayList<>();

    /**
     * Get set by the 2 different Fragments. TODO REWORK|Very unsafe
     */
    private static ListAdapter adapterActive, adapterInactive;

    public static void setup(Context context){

        sharedPreferences = context.getSharedPreferences("notifications", Context.MODE_PRIVATE);

        //if setup was already called, just return
        if(notifications != null){
            return;
        }

        load();

        //create the currently active notifications again
        for(Notification notification : activeNotifications){
            NotificationFactory.create(notification.id, notification.title, notification.subTitle, notification.color);
        }
    }

    /**
     * Adds a notification and saves it afterwards
     * @param notification
     */
    public static void add(Notification notification){
        //add it to the right place of the notificationsarray
        activeNotifications.add(0,notification);
        save();
    }

    public static Notification findById(ArrayList<Notification> where, int id){
        for(Notification notification : where){
            if(notification.id == id){
                return notification;
            }
        }
        return null;
    }

    /**
     * Used for debugging purposes
     */
    public static void deleteAllFromSharedPref(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }



    /**
     * Swaps a notification to the other tab.
     * So if the notification switches to being inactive it gets removed from the active tab and moved to the inactive tab
     * @param notification
     */
    public static void swap(Notification notification){
        //find out from which arraylist the notification should be added to and which one removed from
        ArrayList<Notification> addTo = activeNotifications.contains(notification)? inactiveNotifications : activeNotifications;
        ArrayList<Notification> removeFrom = activeNotifications.contains(notification)? activeNotifications : inactiveNotifications;

        int oldIndex = removeFrom.indexOf(notification);

        addTo.add(0,notification);
        removeFrom.remove(notification);
        //notify the adapters
        //todo rework spaghetti code, Storage should not notify the adapters
        if(adapterActive != null && adapterInactive != null){

            //if we add to the active adapter
            if(adapterActive.data == addTo){
                //we have to notitify that we added an item at the beginning
                adapterActive.notifyItemInserted(0);
                //and that also means we removed something from the inactive adapter
                adapterInactive.notifyItemRemoved(oldIndex);
            }else{
                //adapters are switched
                adapterInactive.notifyItemInserted(0);
                adapterActive.notifyItemRemoved(oldIndex);
            }

        }
    }

    /**
     * Removes a notification.
     * @param notification
     */
    public static void remove(Notification notification){
        if(activeNotifications.contains(notification)){
            activeNotifications.remove(notification);
        }else{
            inactiveNotifications.remove(notification);
        }
    }


    /**
     * Saves all the Notifications to the SharedPreferencesStorage
     */
    public static void save(){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString("active", new Gson().toJson(activeNotifications));
            editor.putString("inactive", new Gson().toJson(inactiveNotifications));
        }catch (Exception e){
            e.printStackTrace();
        }

        editor.apply();
    }

    /**
     * Loads the Notifications from the SharedPreferencesStorage
     */
    private static void load(){
        Gson gson = new Gson();
        String defaultVal = gson.toJson(new ArrayList<Notification>());
        Type typeToken = new TypeToken<ArrayList<Notification>>(){}.getType();

        try{
            activeNotifications = gson.fromJson(sharedPreferences.getString("active", defaultVal), typeToken);
            inactiveNotifications = gson.fromJson(sharedPreferences.getString("inactive", defaultVal), typeToken);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Notification> getActiveNotifications(){
        return activeNotifications;
    }

    public static ArrayList<Notification> getInactiveNotifications(){
        return inactiveNotifications;
    }

    public static void setAdapterActive(ListAdapter adapterActive) {
        Storage.adapterActive = adapterActive;
    }

    public static void setAdapterInactive(ListAdapter adapterInactive) {
        Storage.adapterInactive = adapterInactive;
    }

    public static ListAdapter getAdapterActive() {
        return adapterActive;
    }

    public static ListAdapter getAdapterInactive() {
        return adapterInactive;
    }

    /**
     * Holds the Data of a single Notification.
     */
    public static class Notification{

        public int id;
        public String title;
        public String subTitle;
        public int color;
        public boolean active;

        public Notification(int id, String title, String subTitle, int color, boolean active){
            this.id = id;
            this.title = title;
            this.subTitle = subTitle;
            this.color = color;
            this.active = active;
        }

    }


}
