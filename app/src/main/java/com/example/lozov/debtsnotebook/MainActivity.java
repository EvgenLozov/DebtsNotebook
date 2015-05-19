package com.example.lozov.debtsnotebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, BorrowDialog.DebtCreationListener{

    Button bBorrow;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bBorrow = (Button) findViewById(R.id.bBorrow);

        bBorrow.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            case R.id.action_borrowers:
                startActivity(new Intent(this, Borrowers.class));
                break;

            case R.id.action_debtors:
                startActivity(new Intent(this, Debtors.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bBorrow:
                new BorrowDialog().show(getSupportFragmentManager(), "borrow");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!authenticate()){
            startActivity(new Intent(this, Login.class));
        }
    }

    private boolean authenticate() {
        return userLocalStore.isUserLoggedIn();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onDebtCreated(Debt debt) {
        String debtorId = userLocalStore.getLoggedInUser().getId();
        debt.setDebtorId(debtorId);

        new ServerRequest(this).createDebt(debt);
    }
}
