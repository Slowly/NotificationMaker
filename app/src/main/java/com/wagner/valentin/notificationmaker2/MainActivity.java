package com.wagner.valentin.notificationmaker2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wagner.valentin.notificationmaker2.list.ListAdapter;
import com.wagner.valentin.notificationmaker2.list.ListDivider;
import com.wagner.valentin.notificationmaker2.notifications.NotificationBroadcastReceiver;
import com.wagner.valentin.notificationmaker2.notifications.NotificationFactory;
import com.wagner.valentin.notificationmaker2.notifications.Storage;

public class MainActivity extends BaseActivity{

    private ListAdapter adapter;

    private MainPagerAdapter mainPagerAdapter;

    private NotificationBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets up the notifications and their storage
        NotificationFactory.setup(getApplicationContext());
        Storage.setup(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the AddActivity
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        //create the tabs at the top
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(mainPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //setup the notification broadcast receiver
        //receiver = new NotificationBroadcastReceiver();
        //unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(receiver); //todo is this neccessary?
    }
}
