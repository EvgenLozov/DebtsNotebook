package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lozov.debtsnotebook.network.request.GetDebtorsRequest;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import java.util.ArrayList;
import java.util.List;


public class Debtors extends ActionBarActivity {
    ListView lvMyDebtors;
    UserLocalStore userLocalStore;

    UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtors);
        userLocalStore = new UserLocalStore(this);

        lvMyDebtors = (ListView) findViewById(R.id.lvMyDebtors);
        adapter = new UsersAdapter(this, new ArrayList<User>());
        lvMyDebtors.setAdapter(adapter);

        ProgressDialog progressDialog = Util.getProgressDialog(this);
        String userId = userLocalStore.getLoggedInUser().getId();

        new GetDebtorsRequest(progressDialog, new ResourcesCallback<User>() {
            @Override
            public void done(List<User> debtors) {
                adapter.clear();
                adapter.addAll(debtors);
                adapter.notifyDataSetChanged();
            }
        }, userId).execute();

        lvMyDebtors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Debtors.this, Debts.class);
                intent.putExtra(Debts.LENDER_ID, userLocalStore.getLoggedInUser().getId());
                intent.putExtra(Debts.DEBTOR_ID, ((User) lvMyDebtors.getItemAtPosition(position)).getId());
                intent.putExtra(Debts.TITLE, "Debts of " + ((User) lvMyDebtors.getItemAtPosition(position)).getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debtors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                return true;
            case R.id.action_lenders:
                startActivity(new Intent(this, Lenders.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
