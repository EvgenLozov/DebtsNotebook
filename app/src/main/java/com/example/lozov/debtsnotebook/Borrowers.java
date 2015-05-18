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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Borrowers extends ActionBarActivity {

    public static final String BORROWER_ID = "com.example.lozov.debtsnotebook.BORROWER_ID";
    List<User> borrowersList = new ArrayList<>();
    BorrowersAdapter adapter;

    ListView lvMyBorrowers;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowers);
        userLocalStore = new UserLocalStore(this);

        lvMyBorrowers = (ListView) findViewById(R.id.lvMyBorrowers);

        adapter = new BorrowersAdapter(this, new ArrayList<User>());
        lvMyBorrowers.setAdapter(adapter);

        new ServerRequest(this).fetchBorrowers(userLocalStore.getLoggedInUser(), new GetUsersCallback() {
            @Override
            public void done(List<User> borrowers) {
                borrowersList.addAll(borrowers);
                adapter.addAll(borrowers);
                adapter.notifyDataSetChanged();
            }
        });

        lvMyBorrowers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Borrowers.this, Debts.class);
                intent.putExtra(BORROWER_ID, borrowersList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_borrowers, menu);
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
