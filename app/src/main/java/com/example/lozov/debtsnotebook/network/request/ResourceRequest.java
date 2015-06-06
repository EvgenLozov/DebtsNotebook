package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;

import org.json.JSONObject;

/**
 * Created by lozov on 22.05.15.
 */
public abstract class ResourceRequest<T> {
    protected static final String DEFAULT_TAG = "getResourceRequest";

    protected ProgressDialog progressDialog;
    protected ResourceCallback<T> callback;

    public ResourceRequest(ProgressDialog progressDialog, ResourceCallback<T> callback) {
        this.progressDialog = progressDialog;
        this.callback = callback;
    }

    public void execute(){
//        progressDialog.show();
        JsonObjectRequest request = new JsonObjectRequest(
                method(),
                url(),
                body(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        callback.done(parseResponse(response));

//                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(getTag(), "Error: " + error.getMessage());
//                progressDialog.dismiss();
            }
        });

        request.setRetryPolicy(AppController.DEFAULT_POLICY);
        AppController.getInstance().addToRequestQueue(request, getTag());
    }

    protected String getTag() {
        return DEFAULT_TAG;
    }


    protected JSONObject body() {
        return null;
    }

    protected int method() {
        return Request.Method.GET;
    }

    protected abstract String url();
    protected abstract T parseResponse(JSONObject response);
}
