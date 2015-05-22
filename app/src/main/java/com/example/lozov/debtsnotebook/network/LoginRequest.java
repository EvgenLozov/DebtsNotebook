package com.example.lozov.debtsnotebook.network;

import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lozov.debtsnotebook.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lozov on 22.05.15.
 */
public class LoginRequest extends GetResourceRequest<User> {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String LOGIN_URL = "/login";

    private String username;
    private String password;

    public LoginRequest(ProgressDialog progressDialog, GetResourceCallback<User> callback,
                        String username, String password) {
        super(progressDialog, callback);
        this.username = username;
        this.password = password;
    }

    @Override
    protected JsonObjectRequest getRequest() {
        String url = AppController.SERVER_ADDRESS + LOGIN_URL;

        JSONObject params = new JSONObject();
        try {
            params.put(PARAM_USERNAME, username);
            params.put(PARAM_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        new User(response);

                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });

        return request;
    }
}
