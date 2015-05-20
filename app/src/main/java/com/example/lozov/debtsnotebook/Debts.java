package com.example.lozov.debtsnotebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Debts extends ActionBarActivity {

    ListView lvDebts;
    DebtsAdapter debtsAdapter;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debts);
        userLocalStore = new UserLocalStore(this);

        lvDebts = (ListView) findViewById(R.id.lvDebts);

        debtsAdapter = new DebtsAdapter(this, new ArrayList<Debt>());
        lvDebts.setAdapter(debtsAdapter);

        populateAdapter();
    }

    private void populateAdapter() {
        Intent intent = getIntent();
        String borrowerId = intent.getStringExtra(Borrowers.BORROWER_ID);

        new ServerRequest(this).fetchDebts(userLocalStore.getLoggedInUser(), borrowerId, new GetDebtsCallback() {
            @Override
            public void done(List<Debt> debts) {
                debtsAdapter.clear();

                lvDebts.setAdapter(null);
                lvDebts.addHeaderView(getHeaderView(getTotalDebt(debts)), "Total", false);

                lvDebts.setAdapter(debtsAdapter);

                debtsAdapter.addAll(debts);
                debtsAdapter.notifyDataSetChanged();
            }
        });
    }

    private int getTotalDebt(List<Debt> debts) {
        Integer result = 0;
        for (Debt debt : debts) {
            result += debt.getAmountOfMoney();
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View getHeaderView(int totalAmount){
        View headerView = getLayoutInflater().inflate(R.layout.debts_header, null);
        TextView tvTotalAmountOfMoney = (TextView) headerView.findViewById(R.id.tvTotalAmountOfMoney);
        tvTotalAmountOfMoney.setText(String.valueOf(totalAmount));

        return headerView;
    }
}
