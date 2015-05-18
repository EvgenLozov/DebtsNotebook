package com.example.lozov.debtsnotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lozov on 18.05.15.
 */
public class BorrowersAdapter extends ArrayAdapter<User> {

    public BorrowersAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        // Return the completed view to render on screen
        return convertView;
    }
}
