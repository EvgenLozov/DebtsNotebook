package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lozov.debtsnotebook.network.request.CreateDebtRequest;
import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;


public class MainActivity extends AppCompatActivity implements DebtCreationDialog.DebtCreationListener{

    UserLocalStore userLocalStore;

    ViewPager viewPager;
    MainPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore = new UserLocalStore(this);

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
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
        finish();
        startActivity(getIntent());
    }
}
