package com.example.lozov.debtsnotebook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;
import com.example.lozov.debtsnotebook.network.request.LoginRequest;


public class Login extends ActionBarActivity implements View.OnClickListener{

    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;
    UserLocalStore userLocalStore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = DialogUtil.getProgressDialog(this);

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

                if (TextUtils.isEmpty(username)){
                    etUsername.setError("Invalid value");
                    etUsername.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Invalid value");
                    etPassword.requestFocus();
                    return;
                }
                authenticate(username, password);

                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(String username, String password) {
        DialogUtil.showProgressDialog(progressDialog);
        new LoginRequest(new ResourceCallback<User>() {
                    @Override
                    public void onSuccess(User returnedUser) {
                        DialogUtil.dismissProgressDialog(progressDialog);
                        if (returnedUser == null){
                            showErrorMessage();
                        } else {
                            logUserIn(returnedUser);
                        }
                    }

                    @Override
                    public void onError() {
                        DialogUtil.dismissProgressDialog(progressDialog);
                    }
            },username, password).execute();
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
