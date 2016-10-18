package com.wagner.valentin.notificationmaker2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wagner.valentin.notificationmaker2.CustomViews.ColorPicker;
import com.wagner.valentin.notificationmaker2.notifications.NotificationFactory;
import com.wagner.valentin.notificationmaker2.notifications.Storage;

import java.util.ArrayList;

public class AddActivity extends BaseActivity implements View.OnClickListener{

    private Button button;

    private ColorPicker colorPicker;

    private TextView title;

    private TextView subTitle;

    /**
     * Is the activity actually started to edit an existing notification?
     */
    private boolean editMode = false;

    /**
     * If the activity is in editMode, this is the notification that gets edited
     */
    private Storage.Notification notificationToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set the click listener
        this.button = (Button) findViewById(R.id.addactivity_create);
        this.button.setOnClickListener(this);

        this.title = (TextView) findViewById(R.id.edit_text_title);
        this.subTitle = (TextView) findViewById(R.id.edit_text_subtitle);
        this.colorPicker = (ColorPicker) findViewById(R.id.color_picker_id);

        //setup the edit mode
        Intent intent = getIntent();
        if(intent.hasExtra("notification_id")){
            //if the extra notification_id was passed with the intent, editMode is enabled
            int notificationId = intent.getExtras().getInt("notification_id");
            editMode = true;
            //searches in the active and inactive array for the notification
            Storage.Notification possibleNotificationActive = Storage.findById(Storage.getActiveNotifications(), notificationId);
            Storage.Notification possibleNotificationInactive = Storage.findById(Storage.getInactiveNotifications(), notificationId);
            //makes sure the actual notification is set
            if(possibleNotificationActive != null)
                notificationToEdit = possibleNotificationActive;
            else if(possibleNotificationInactive != null)
                notificationToEdit = possibleNotificationInactive;

            //set the notification values to the textfield and so on
            title.setText(notificationToEdit.title);
            subTitle.setText(notificationToEdit.subTitle);
            colorPicker.setElementBasedOnColor(notificationToEdit.color);

            //also change some other strings
            getSupportActionBar().setTitle(R.string.title_activity_addactivity_edit_mode);
            button.setText(R.string.add_activity_button_edit_mode);
        }
    }

    /**
     * Create Button Click Listener.
     * Creates a new Notification and saves it in the Storage.
     * @param v
     */
    @Override
    public void onClick(View v) {
        //if not in edit mode create a new notification
        if(!this.editMode){
            String title = this.title.getText().toString();
            String subTitle = this.subTitle.getText().toString();
            int color = this.colorPicker.getCurrentColor();
            if(title.equals("")){
                Toast.makeText(this, "Title needed", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = NotificationFactory.createId();
            NotificationFactory.create(id, title, subTitle, color);
            Storage.add(new Storage.Notification(id, title, subTitle, color, true));
        }else{
            //if in edit mode save the changes, update the actual notification and go back to the main activity
            notificationToEdit.title = this.title.getText().toString();
            notificationToEdit.subTitle = this.subTitle.getText().toString();
            notificationToEdit.color = this.colorPicker.getCurrentColor();

            //notify that the data changed
            Storage.getAdapterActive().notifyDataSetChanged();
            Storage.getAdapterInactive().notifyDataSetChanged();

            //save the changes
            Storage.save();

            //make sure the notification is up to date
            if(notificationToEdit.active)
                NotificationFactory.create(notificationToEdit.id, notificationToEdit.title, notificationToEdit.subTitle, notificationToEdit.color);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            //go back to the old activity
            finish();
        }
    }

    /**
     * Make sure the clear button is visible and setup its clicklistener
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem clearAction = menu.findItem(R.id.action_clear);
        clearAction.setVisible(true);

        clearAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //reset all the fields and the color picker
                AddActivity.this.title.setText("");
                AddActivity.this.subTitle.setText("");
                AddActivity.this.colorPicker.reset();

                //also set the focus to the title textfield
                AddActivity.this.title.requestFocus();

                return true;
            }
        });

        return true;
    }
}
