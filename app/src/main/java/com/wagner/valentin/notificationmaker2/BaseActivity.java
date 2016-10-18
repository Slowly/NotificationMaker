package com.wagner.valentin.notificationmaker2;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wagner.valentin.notificationmaker2.notifications.NotificationFactory;
import com.wagner.valentin.notificationmaker2.notifications.Storage;

/**
 * Created by Valentin on 21.09.2016.
 * The base activity. Handles the creation and clicks of the menu.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_credits){
            Toast.makeText(this, "Developed by Valentin Wagner", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.action_delete_active){

            new AlertDialog.Builder((this))
                    .setTitle("Delete")
                    .setMessage("Delete all active notifications?")
                    .setIcon(R.mipmap.ic_delete_black_24dp)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationFactory.removeAll(Storage.getAdapterActive().data);
                            Storage.getAdapterActive().data.clear();
                            Storage.getAdapterActive().notifyDataSetChanged();
                            Storage.save();
                            Toast.makeText(BaseActivity.this, "Deleted all active notifications", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null).show();

            return true;
        }else if(id == R.id.action_delete_inactive){

            new AlertDialog.Builder((this))
                    .setTitle("Delete")
                    .setMessage("Delete all inactive notifications?")
                    .setIcon(R.mipmap.ic_delete_black_24dp)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Storage.getAdapterInactive().data.clear();
                            Storage.getAdapterInactive().notifyDataSetChanged();
                            Storage.save();
                            Toast.makeText(BaseActivity.this, "Deleted all inactive notifications", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null).show();

        }

        return super.onOptionsItemSelected(item);
    }

}
