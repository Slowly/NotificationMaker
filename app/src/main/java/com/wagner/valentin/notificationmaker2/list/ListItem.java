package com.wagner.valentin.notificationmaker2.list;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wagner.valentin.notificationmaker2.AddActivity;
import com.wagner.valentin.notificationmaker2.BaseActivity;
import com.wagner.valentin.notificationmaker2.MainActivity;
import com.wagner.valentin.notificationmaker2.R;
import com.wagner.valentin.notificationmaker2.notifications.NotificationFactory;
import com.wagner.valentin.notificationmaker2.notifications.Storage;

import java.util.ArrayList;

/**
 * Created by Valentin on 24.09.2016.
 */

public class ListItem extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

    public Storage.Notification notification;
    public ListAdapter adapter;
    public View view;
    public TextView title;
    public TextView subTitle;
    public ImageView imageView;
    public ImageView buttonAccept;

    //context menu ids
    private final int ID_DELETE = 1;
    private final int ID_EDIT = 2;
    private final int ID_SWITCH = 3;
    private final int ID_COPY = 4;

    public ListItem(View view, ListAdapter adapter) {
        super(view);
        this.adapter = adapter;
        this.view = view;
        this.title = (TextView) view.findViewById(R.id.list_item_title);
        this.subTitle = (TextView) view.findViewById(R.id.list_item_subtitle);
        this.imageView = (ImageView) view.findViewById(R.id.list_item_imageview);

        this.buttonAccept = (ImageView) view.findViewById(R.id.list_item_checkmark);

        this.buttonAccept.setOnClickListener(this);

        //setup the context menu listener
        view.setOnClickListener(this);
        view.setOnCreateContextMenuListener(this);
    }


    /**
     * Sets up the Buttons.
     * In an extra method because the data is not known in the constructor and it has to be refreshed for every reuse of the view
     */
    public void setupButtons(){
        //set the correct image on the button
        if(this.notification.active){
            this.buttonAccept.setImageResource(R.mipmap.ic_done_black_24dp);
            this.buttonAccept.setTag(R.string.list_item_button_tag, true);
        }else{
            this.buttonAccept.setImageResource(R.mipmap.ic_refresh_black_24dp);
            this.buttonAccept.setTag(R.string.list_item_button_tag, false);
        }
    }


    /**
     * Creates the menu items for the context menu
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem deleteItem = menu.add(Menu.NONE, ID_DELETE, Menu.NONE, "Delete");
        deleteItem.setOnMenuItemClickListener(this);

        MenuItem editItem = menu.add(Menu.NONE, ID_EDIT, Menu.NONE, "Edit");
        editItem.setOnMenuItemClickListener(this);

        MenuItem copyItem = menu.add(Menu.NONE, ID_COPY, Menu.NONE, "Copy");
        copyItem.setOnMenuItemClickListener(this);

        String switchItemTitle = "Switch to " + (this.notification.active? "inactive" : "active");
        MenuItem switchItem = menu.add(Menu.NONE, ID_SWITCH, Menu.NONE, switchItemTitle);
        switchItem.setOnMenuItemClickListener(this);
    }

    /**
     * Called when the user clicks on a single menu item.
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item){

        switch(item.getItemId()){
            case ID_DELETE:

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete")
                        .setMessage("Delete this notification?")
                        .setIcon(R.mipmap.ic_delete_black_24dp)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotificationFactory.remove(notification.id);

                                if(notification.active) {
                                    Storage.getActiveNotifications().remove(notification);
                                    Storage.getAdapterActive().notifyItemRemoved(getAdapterPosition());
                                }else{
                                    Storage.getInactiveNotifications().remove(notification);
                                    Storage.getAdapterInactive().notifyItemRemoved(getAdapterPosition());
                                }

                                Storage.save();
                                Toast.makeText(view.getContext(), "Notification was deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null).show();

                break;
            case ID_EDIT:

                //start the AddActivity with the notification_id as extra
                Intent intent = new Intent(view.getContext(), AddActivity.class);
                intent.putExtra("notification_id", notification.id);
                view.getContext().startActivity(intent);

                break;
            case ID_SWITCH:

                //switch the notification status
                if(notification.active){
                    switchToInactive();
                }else{
                    switchToActive();
                }

                break;
            case ID_COPY:

                //create a new notification
                Storage.Notification newNotification = new Storage.Notification(NotificationFactory.createId(), notification.title, notification.subTitle, notification.color, notification.active);

                //find out which arraylist to add it to
                ArrayList<Storage.Notification> addTo = null;
                if(notification.active)
                    addTo = Storage.getActiveNotifications();
                else
                    addTo = Storage.getInactiveNotifications();

                //add it below the current notification in the list
                int newIndex = getAdapterPosition() + 1;
                addTo.add(newIndex, newNotification);

                //if the notification we are copying is active, then we also make the new one active and create a notification for it
                if(notification.active){
                    NotificationFactory.create(newNotification.id, newNotification.title, newNotification.subTitle, newNotification.color);
                }

                //notify the adapter
                if(notification.active){
                    Storage.getAdapterActive().notifyItemInserted(newIndex);
                }else{
                    Storage.getAdapterInactive().notifyItemInserted(newIndex);
                }

                Storage.save();

                Toast.makeText(view.getContext(), "Copied the notification", Toast.LENGTH_SHORT).show();

                break;
        }

        return true;
    }

    /**
     * Called when the user clicks on the view or the done/reuse button
     * @param clickedElement
     */
    @Override
    public void onClick(View clickedElement) {
        if(clickedElement.getId() == this.buttonAccept.getId()){

            //the button has currently the checkmark icon
            //-> remove the notification
            if((boolean) clickedElement.getTag(R.string.list_item_button_tag)){

                switchToInactive();

            }else{
                //the button has the restart icon
                //add the notification again
                switchToActive();
            }

        }

    }



    /**
     * Switch the images, remove the notification, swap to the other tab
     */
    public void switchToInactive(){
        NotificationFactory.remove(notification.id);

        notification.active = false;
        Storage.swap(notification);
        Storage.save();
        adapter.notifyItemChanged(getAdapterPosition());

        this.buttonAccept.setImageResource(R.mipmap.ic_refresh_black_24dp);
        this.buttonAccept.setTag(R.string.list_item_button_tag, false);
    }

    /**
     * Switch the images, remove the notification, swap to the other tab
     */
    public void switchToActive(){
        NotificationFactory.create(notification.id, notification.title, notification.subTitle, notification.color);

        notification.active = true;
        Storage.swap(notification);
        Storage.save();
        adapter.notifyItemChanged(getAdapterPosition());

        this.buttonAccept.setImageResource(R.mipmap.ic_done_black_24dp);
        this.buttonAccept.setTag(R.string.list_item_button_tag, true);
    }


    @Override
    public String toString() {
        return "Test";
    }
}
