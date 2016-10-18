package com.wagner.valentin.notificationmaker2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wagner.valentin.notificationmaker2.fragments.NotificationsTabBase;

/**
 * Created by Valentin on 27.09.2016.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override
    public Fragment getItem(int position){
        NotificationsTabBase fragment = new NotificationsTabBase();
        Bundle bundle = new Bundle();
        switch (position){
            case 0:
                bundle.putSerializable("MODE", NotificationsTabBase.MODES.ACTIVE);
                break;
            case 1:
                bundle.putSerializable("MODE", NotificationsTabBase.MODES.INACTIVE);
                break;
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Active";
            case 1:
                return "Inactive";
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return 2;
    }
}
