package com.example.user.termproject;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by user on 2017-11-21.
 */

public class TownItemView extends LinearLayout {

    TextView txtName, txtCity, txtActivity;

    public TownItemView(Context context) {
        super(context);

        init(context);
    }

    public TownItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.town_item, this, true);

        txtName = (TextView) findViewById(R.id.txtName);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtActivity = (TextView) findViewById(R.id.txtActivity);

    }

    public void setName(String name) {
        txtName.setText(name);
    }

    public void setCity(String state, String city) {
        if(state.equals(city))
            txtCity.setText(state);
        else
            txtCity.setText(state + " " + city);
    }

    public void setActivity(String activity) {
        txtActivity.setText(activity);
    }
}
