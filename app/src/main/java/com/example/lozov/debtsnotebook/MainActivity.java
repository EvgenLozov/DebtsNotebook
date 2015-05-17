package com.example.lozov.debtsnotebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    Button bLogout;
    EditText etEmail, etUsername;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);

        bLogout = (Button) findViewById(R.id.bLogout);

        bLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate()){
            displayUserDetails();
        } else {
            startActivity(new Intent(this, Login.class));
        }
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());
    }

    private boolean authenticate() {
        return userLocalStore.isUserLoggedIn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}
