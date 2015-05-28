package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lozov on 22.05.15.
 */
public abstract class ResourcesRequest<T> {
    private static final String DEFAULT_TAG = "getResourcesRequest";

    private ProgressDialog progressDialog;
    private ResourcesCallback<T> callback;

    private JsonArrayRequest request;

    public ResourcesRequest(ProgressDialog progressDialog, ResourcesCallback<T> callback) {
        this.progressDialog = progressDialog;
        this.callback = callback;
    }

    public void execute(){
//        progressDialog.show();
        request = new JsonArrayRequest(
                method(),
                url(),
                body(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.done(parseResponse(response));
//                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(getTag(), "Error: " + error.getMessage());
//                progressDialog.dismiss();
                Context context = progressDialog.getContext();
                Toast.makeText(context, "Unable to load data!", Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(request, getTag());
    }

    protected String getTag() {
        return DEFAULT_TAG;
    }

    protected JSONObject body() {
        return null;
    }

    protected int method(){
        return Request.Method.GET;
    }

    protected abstract String url();
    protected abstract List<T> parseResponse(JSONArray response);
}
