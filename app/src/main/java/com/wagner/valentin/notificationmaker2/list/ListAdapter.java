package com.wagner.valentin.notificationmaker2.list;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wagner.valentin.notificationmaker2.MainActivity;
import com.wagner.valentin.notificationmaker2.R;
import com.wagner.valentin.notificationmaker2.notifications.Storage;

import java.util.ArrayList;

/**
 * Created by Valentin on 24.09.2016.
 */

public class ListAdapter extends RecyclerView.Adapter<ListItem>{

    private MainActivity context;

    public ArrayList<Storage.Notification> data;

    public ListAdapter(MainActivity context, ArrayList<Storage.Notification> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        //set the contextmenulistener


        return new ListItem(view, this);
    }

    @Override
    public void onBindViewHolder(ListItem holder, int position) {
        holder.notification = this.data.get(position);
        holder.title.setText(holder.notification.title);
        holder.subTitle.setText(holder.notification.subTitle);
        holder.imageView.setColorFilter(holder.notification.color, PorterDuff.Mode.MULTIPLY);

        holder.setupButtons();
    }

    @Override
    public int getItemCount(){
        return this.data.size();
    }
}
