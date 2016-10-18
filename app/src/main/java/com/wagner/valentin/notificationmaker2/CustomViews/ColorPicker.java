package com.wagner.valentin.notificationmaker2.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wagner.valentin.notificationmaker2.R;

/**
 * Created by Valentin on 22.09.2016.
 */

public class ColorPicker extends LinearLayout {

    /**
     * The Values of the ImageViews.
     * Has to be of an even length.
     */
    private String[][] colors = {
            {"Light Blue", "#6599FF"},
            {"Red", "#FF0000"},
            {"Light Red", "#FA8072"},
            {"Orange", "#FF9900"},
            {"Yellow", "#FFDE00"},
            {"Green", "#89E894"}
    };

    private int defaultElement = 0;

    private int circleSize = 40;

    /**
     * Array to store all the views in
     * Has the exact same length as the colors Array.
     */
    private View[] allElements = new View[colors.length];

    public View currentSelectedElement = null;

    /**
     * The default backgrund for the clickable views.
     */
    private Drawable defaultBackground;

    public ColorPicker(Context context){
        super(context);
        init(context);
    }
    public ColorPicker(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }
    public ColorPicker(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.component_color_picker, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        LayoutInflater inflater = LayoutInflater.from(context);

        for(int index = 0; index < this.colors.length; index++){
            final String[] element = this.colors[index];

            LinearLayout root = chooseCorrectLayout(element, index);

            //create the view based on the layout file compontent_color_picker_circle
            View view = inflater.inflate(R.layout.component_color_picker_circle, root, false);

            //add the view to the elements array
            this.allElements[index] = view;

            ImageView imageView = (ImageView) view.findViewById(R.id.component_color_picker_circle_imageview);
            TextView textView = (TextView) view.findViewById(R.id.component_color_picker_circle_textview);

            textView.setText(element[0]);

            //change the color of the circle to the element color
            imageView.setColorFilter(Color.parseColor(element[1]), PorterDuff.Mode.MULTIPLY);

            view.setTag(R.string.color_picker_tag_id, Color.parseColor(element[1]));

            //sets up the click listener
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ColorPicker.this.setCurrentElement(v);
                }
            });


            root.addView(view);
        }

        //get the default background and then set the default element
        if(this.currentSelectedElement == null){
            this.defaultBackground = allElements[0].getBackground();
            this.setCurrentElement(allElements[this.defaultElement]);
        }
    }

    /**
     * Sets the current element and change its background color
     * @param newCurrentElement The new Current Element
     */
    private void setCurrentElement(View newCurrentElement){
        this.currentSelectedElement = newCurrentElement;

        //set the default backgrund for all views
        for(View view : this.allElements){
            //have to use a deprecated method since the newer one isnt avaible for the target api
            view.setBackgroundDrawable(this.defaultBackground);
        }

        this.currentSelectedElement.setBackgroundColor(Color.parseColor("#e6e6e6"));

    }

    /**
     * Returns the color of the currentSelectedElement
     * @return
     */
    public int getCurrentColor(){
        return (int) this.currentSelectedElement.getTag(R.string.color_picker_tag_id);
    }

    /**
     * Set the current element based on the colorCode stored in a Notification {@link com.wagner.valentin.notificationmaker2.notifications.Storage.Notification}
     * @param colorCode
     */
    public void setElementBasedOnColor(int colorCode){

        int index = 0;
        //go through all the colors
        for(String[] current : colors){
            int currentColorCode = Color.parseColor(current[1]);

            //see if the current element is the element that should be selected
            if(currentColorCode == colorCode){

                //the view that should be set
                View element = allElements[index];
                setCurrentElement(element);
            }
            index++;
        }

    }

    /**
     * Reset the Color picker
     */
    public void reset(){
        this.setCurrentElement(this.allElements[this.defaultElement]);
    }

    /**
     * Picks the correct Layout to add the Imageview to.
     * If the index of the element is > colors.length / 2 it chooses the second one.
     * @param element
     */
    private LinearLayout chooseCorrectLayout(String[] element, int index){

        if(index >= colors.length/2f){
            return (LinearLayout) findViewById(R.id.color_picker_layout_2);
        }else{
            return (LinearLayout) findViewById(R.id.color_picker_layout_1);
        }

    }

}
