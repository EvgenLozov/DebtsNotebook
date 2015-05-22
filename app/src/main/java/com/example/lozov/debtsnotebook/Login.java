package com.example.lozov.debtsnotebook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lozov.debtsnotebook.network.GetResourceCallback;
import com.example.lozov.debtsnotebook.network.LoginRequest;

import java.util.List;


public class Login extends ActionBarActivity implements View.OnClickListener{

    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);

        tvRegisterLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);

                authenticate(username, password);

                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(String username, String password) {
        ProgressDialog progressDialog = Util.getProgressDialog(this);

        new LoginRequest(
                progressDialog,
                new GetResourceCallback<User>() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser == null){
                            showErrorMessage();
                        } else {
                            logUserIn(returnedUser);
                        }
                    }
                },
                username,
                password).execute();
    }

    private void logUserIn(User user) {
        userLocalStore.storeUserData(user);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));
    }

    private void showErrorMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
        alertDialogBuilder.setMessage("Incorrect username or password");
        alertDialogBuilder.setPositiveButton("Ok", null);
        alertDialogBuilder.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
