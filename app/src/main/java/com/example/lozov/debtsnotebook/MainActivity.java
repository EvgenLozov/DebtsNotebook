package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.lozov.debtsnotebook.network.request.CreateDebtRequest;
import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, DebtCreationDialog.DebtCreationListener{

    Button bBorrow, bLend;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bBorrow = (Button) findViewById(R.id.bBorrow);
        bLend = (Button) findViewById(R.id.bLend);

        bBorrow.setOnClickListener(this);
        bLend.setOnClickListener(this);

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

            case R.id.action_lenders:
                startActivity(new Intent(this, Lenders.class));
                break;

            case R.id.action_debtors:
                startActivity(new Intent(this, Debtors.class));
                break;

            case R.id.action_borrow:
                DebtCreationDialog.newInstance(userLocalStore.getLoggedInUser().getId(), Debt.Type.BORROWED).show(getSupportFragmentManager(), "borrow");
                break;

            case R.id.action_lend:
                DebtCreationDialog.newInstance(userLocalStore.getLoggedInUser().getId(), Debt.Type.LOANED).show(getSupportFragmentManager(), "lend");
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
                DebtCreationDialog.newInstance(userLocalStore.getLoggedInUser().getId(), Debt.Type.BORROWED).show(getSupportFragmentManager(), "borrow");
                break;
            case R.id.bLend:
                DebtCreationDialog.newInstance(userLocalStore.getLoggedInUser().getId(), Debt.Type.LOANED).show(getSupportFragmentManager(), "lend");
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
        ProgressDialog progressDialog = Util.getProgressDialog(this);
        new CreateDebtRequest(progressDialog, new ResourceCallback<Debt>() {
            @Override
            public void done(Debt resource) {
                //todo validate response
            }
        }, debt).execute();
    }
}
