package com.example.lozov.debtsnotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lozov on 19.05.15.
 */
public class UserSpinnerAdapter extends ArrayAdapter<User> {

    public UserSpinnerAdapter(Context context, List<User> objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvUsername);
        // Populate the data into the template view using the data object
        tvName.setText(user.getUsername());
        tvName.setTag(user.getId());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_spinner_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvUsername);
        // Populate the data into the template view using the data object
        tvName.setText(user.getUsername());
        tvName.setTag(user.getId());
        // Return the completed view to render on screen
        return convertView;
    }
}