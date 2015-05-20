package com.example.lozov.debtsnotebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Debtors extends ActionBarActivity {

    public static final String BORROWER_ID = "com.example.lozov.debtsnotebook.BORROWER_ID";

    ListView lvMyDebtors;
    UserLocalStore userLocalStore;

    List<User> debtorsList = new ArrayList<>();
    UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtors);
        userLocalStore = new UserLocalStore(this);

        lvMyDebtors = (ListView) findViewById(R.id.lvMyDebtors);
        adapter = new UsersAdapter(this, new ArrayList<User>());
        lvMyDebtors.setAdapter(adapter);

        new ServerRequest(this).fetchDebtors(userLocalStore.getLoggedInUser(), new GetUsersCallback() {
            @Override
            public void done(List<User> borrowers) {
                debtorsList.clear();
                adapter.clear();

                debtorsList.addAll(borrowers);
                adapter.addAll(borrowers);
                adapter.notifyDataSetChanged();
            }
        });

        lvMyDebtors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Debtors.this, Debts.class);
                intent.putExtra(BORROWER_ID, userLocalStore.getLoggedInUser().getId());
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
