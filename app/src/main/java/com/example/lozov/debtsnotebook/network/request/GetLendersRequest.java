package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;

import com.example.lozov.debtsnotebook.User;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by lozov on 22.05.15.
 */
public class GetLendersRequest extends ResourcesRequest<User> {

    private String userId;

    public GetLendersRequest(ResourcesCallback<User> callback, String userId) {
        super(callback);
        this.userId = userId;
    }

    @Override
    protected String url() {
        return AppController.SERVER_ADDRESS + "/user/" + userId + "/lenders";
    }

    @Override
    protected List<User> parseResponse(JSONArray response) {
        return User.fromJson(response);
    }
}
