package com.example.lozov.debtsnotebook;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by lozov on 20.05.15.
 */
public class DebtsAdapter extends ArrayAdapter<Debt> {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private List<Debt> debts;

    public DebtsAdapter(Context context, List<Debt> objects) {
        super(context, 0, objects);
        this.debts = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Debt debt = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.debt_item, parent, false);
        }

        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
        tvDesc.setText(debt.getDesc());

        TextView tvAmountOfMoney = (TextView) convertView.findViewById(R.id.tvAmountOfMoney);
        tvAmountOfMoney.setText(String.valueOf(debt.getAmountOfMoney()));

        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        if (debt.getDate() != null)
            tvDate.setText(DATE_FORMAT.format(debt.getDate()));
        else
            tvDate.setText("N/A");

        return convertView;
    }

    private static class DebtViewHolder{
        TextView tvDesc;
        TextView tvAmountOfMoney;
    }
}
