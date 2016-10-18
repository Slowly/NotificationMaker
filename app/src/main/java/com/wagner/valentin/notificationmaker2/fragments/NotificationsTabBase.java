package com.wagner.valentin.notificationmaker2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wagner.valentin.notificationmaker2.MainActivity;
import com.wagner.valentin.notificationmaker2.R;
import com.wagner.valentin.notificationmaker2.list.ListAdapter;
import com.wagner.valentin.notificationmaker2.list.ListDivider;
import com.wagner.valentin.notificationmaker2.notifications.Storage;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

/**
 * Created by Valentin on 27.09.2016.
 */

public class NotificationsTabBase extends Fragment {

    private ListAdapter adapter;
    private View view;

    public static enum MODES{
        ACTIVE,
        INACTIVE
    }

    private MODES mode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        mode = (MODES) getArguments().get("MODE");

        setupListAdapter();
        //create the tabs at the top


        return view;
    }


    private void setupListAdapter(){
        //setup the list
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.content_main_list);

        //set the divider between the items
        recyclerView.addItemDecoration(new ListDivider(getContext()));

        //set the animation
        if(this.mode == MODES.ACTIVE)
            recyclerView.setItemAnimator(new SlideInRightAnimator());
        else
            recyclerView.setItemAnimator(new SlideInLeftAnimator());

        ArrayList<Storage.Notification> data = this.mode == MODES.ACTIVE? Storage.getActiveNotifications() : Storage.getInactiveNotifications();
        this.adapter = new ListAdapter((MainActivity) getActivity(), data);

        //set the adapter dependency
        if(this.mode == MODES.ACTIVE)
            Storage.setAdapterActive(this.adapter);
        else
            Storage.setAdapterInactive(this.adapter);

        recyclerView.setAdapter(this.adapter);
    }
}
