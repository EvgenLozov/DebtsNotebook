package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore = new UserLocalStore(this);
        progressDialog = DialogUtil.getProgressDialog(MainActivity.this);

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                showCreateDialog(Debt.Type.BORROWED);
                break;

            case R.id.action_lend:
                showCreateDialog(Debt.Type.LOANED);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void showCreateDialog(Debt.Type debtType) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DebtCreationDialog frag = DebtCreationDialog.newInstance(userLocalStore.getLoggedInUser().getId(), debtType);
        frag.show(ft, "createDebt");
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
        DialogUtil.showProgressDialog(progressDialog);
        new CreateDebtRequest(new ResourceCallback<Debt>() {
            @Override
            public void onSuccess(Debt resource) {
                //todo validate response
                DialogUtil.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onError() {
                DialogUtil.dismissProgressDialog(progressDialog);
            }
        }, debt).execute();
        finish();
        startActivity(getIntent());
    }
}
