package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;

import com.example.lozov.debtsnotebook.User;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Yevhen on 2015-05-23.
 */
public class GetUsersRequest extends ResourcesRequest<User> {

    public GetUsersRequest(ProgressDialog progressDialog, ResourcesCallback<User> callback) {
        super(progressDialog, callback);
    }

    @Override
    protected String url() {
        return AppController.SERVER_ADDRESS + "/user";
    }

    @Override
    protected List<User> parseResponse(JSONArray response) {
        return User.fromJson(response);
    }
}
