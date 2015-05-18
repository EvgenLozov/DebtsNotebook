package com.example.lozov.debtsnotebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Borrowers extends ActionBarActivity {

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

        new ServerRequest(this).fetchBorrowers(userLocalStore.getLoggedInUser(), new GetBorrowersCallback() {
            @Override
            public void done(List<User> borrowers) {
                adapter.addAll(borrowers);
                adapter.notifyDataSetChanged();
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
