package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;

import com.android.volley.Request;
import com.example.lozov.debtsnotebook.User;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lozov on 22.05.15.
 */
public class LoginRequest extends ResourceRequest<User> {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String LOGIN_URL = "/login";

    private String username;
    private String password;

    public LoginRequest(ResourceCallback<User> callback,
                        String username, String password) {
        super(callback);
        this.username = username;
        this.password = password;
    }

    @Override
    protected String url() {
        return AppController.SERVER_ADDRESS + LOGIN_URL;
    }

    @Override
    protected int method() {
        return Request.Method.POST;
    }

    @Override
    protected JSONObject body() {
        JSONObject params = new JSONObject();
        try {
            params.put(PARAM_USERNAME, username);
            params.put(PARAM_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    @Override
    protected User parseResponse(JSONObject response) {
        return new User(response);
    }
}
