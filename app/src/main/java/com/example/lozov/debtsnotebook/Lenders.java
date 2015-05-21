package com.example.lozov.debtsnotebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Lenders extends ActionBarActivity {
    UsersAdapter adapter;

    ListView lvMyLenders;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lenders);
        userLocalStore = new UserLocalStore(this);

        lvMyLenders = (ListView) findViewById(R.id.lvMyLenders);

        adapter = new UsersAdapter(this, new ArrayList<User>());
        lvMyLenders.setAdapter(adapter);

        new ServerRequest(this).fetchLenders(userLocalStore.getLoggedInUser(), new GetUsersCallback() {
            @Override
            public void done(List<User> lenders) {
                adapter.clear();
                adapter.addAll(lenders);
                adapter.notifyDataSetChanged();
            }
        });

        lvMyLenders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Lenders.this, Debts.class);
                intent.putExtra(Debts.LENDER_ID, ((User) lvMyLenders.getItemAtPosition(position)).getId());
                intent.putExtra(Debts.DEBTOR_ID, userLocalStore.getLoggedInUser().getId());
                intent.putExtra(Debts.TITLE, "Debts to " + ((User) lvMyLenders.getItemAtPosition(position)).getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lenders, menu);
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
            case R.id.action_debtors:
                startActivity(new Intent(this, Debtors.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
