package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;

import com.android.volley.Request;
import com.example.lozov.debtsnotebook.User;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yevhen on 2015-05-24.
 */
public class RegisterUserRequest extends ResourceRequest<User> {

    private User user;

    public RegisterUserRequest(ProgressDialog progressDialog, ResourceCallback<User> callback, User user) {
        super(progressDialog, callback);
        this.user = user;
    }

    @Override
    protected String url() {
        return AppController.SERVER_ADDRESS + "/user";
    }

    @Override
    protected int method() {
        return Request.Method.POST;
    }

    @Override
    protected JSONObject body() {
        JSONObject dataToSend = new JSONObject();
        try {
            dataToSend.put("email", user.getEmail());
            dataToSend.put("username", user.getUsername());
            dataToSend.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataToSend;
    }

    @Override
    protected User parseResponse(JSONObject response) {
        return new User(response);
    }
}
