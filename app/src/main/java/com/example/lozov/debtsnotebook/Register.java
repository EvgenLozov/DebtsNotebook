package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lozov.debtsnotebook.network.request.RegisterUserRequest;
import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;


public class Register extends ActionBarActivity implements View.OnClickListener{

    Button bRegister;
    EditText etEmail, etUsername, etPassword;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister:
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User newUser = new User(email, username, password);

                register(newUser);

                break;
        }
    }

    private void register(User newUser) {
        ProgressDialog progressDialog = Util.getProgressDialog(this);
        new RegisterUserRequest(progressDialog,
                new ResourceCallback<User>() {
            @Override
            public void done(User user) {
                startActivity(new Intent(Register.this, Login.class));
            }
        }, newUser).execute();
    }
}
