package com.example.lozov.debtsnotebook.network;

import android.app.ProgressDialog;

import com.example.lozov.debtsnotebook.GetResourcesCallback;
import com.example.lozov.debtsnotebook.User;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by lozov on 22.05.15.
 */
public class GetLendersRequest extends GetResourcesRequest<User> {

    private String userId;

    public GetLendersRequest(ProgressDialog progressDialog, GetResourcesCallback<User> callback, String userId) {
        super(progressDialog, callback);
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
