package com.example.lozov.debtsnotebook.network;

import android.app.ProgressDialog;

import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by lozov on 22.05.15.
 */
public abstract class GetResourceRequest<T> {
    protected static final String TAG = "getResourceRequest";

    protected ProgressDialog progressDialog;
    protected GetResourceCallback<T> callback;

    public GetResourceRequest(ProgressDialog progressDialog, GetResourceCallback<T> callback) {
        this.progressDialog = progressDialog;
        this.callback = callback;
    }

    public void execute(){
        progressDialog.show();
        AppController.getInstance().addToRequestQueue(getRequest(), TAG);
    }

    protected abstract JsonObjectRequest getRequest();
}
