package com.example.lozov.debtsnotebook.network.request;

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

    private ResourcesCallback<T> callback;

    private JsonArrayRequest request;

    public ResourcesRequest(ResourcesCallback<T> callback) {
        this.callback = callback;
    }

    public void execute(){
        request = new JsonArrayRequest(
                method(),
                url(),
                body(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(parseResponse(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(getTag(), "Error: " + error.getMessage());
                callback.onError();

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
