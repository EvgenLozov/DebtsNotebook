package com.example.lozov.debtsnotebook.network;

import android.app.DownloadManager;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lozov.debtsnotebook.GetResourcesCallback;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by lozov on 22.05.15.
 */
public abstract class GetResourcesRequest<T> {
    private static final String TAG = "getResourcesRequest";

    private ProgressDialog progressDialog;
    private GetResourcesCallback<T> callback;

    private JsonArrayRequest request;

    public GetResourcesRequest(ProgressDialog progressDialog, GetResourcesCallback<T> callback) {
        this.progressDialog = progressDialog;
        this.callback = callback;
    }

    public void execute(){
        progressDialog.show();
        request = new JsonArrayRequest(
                method(),
                url(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.done(parseResponse(response));
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    protected int method(){
        return Request.Method.GET;
    }

    protected abstract String url();
    protected abstract List<T> parseResponse(JSONArray response);
}
