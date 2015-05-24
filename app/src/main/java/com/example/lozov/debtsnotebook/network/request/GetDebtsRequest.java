package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;

import com.example.lozov.debtsnotebook.Debt;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Yevhen on 2015-05-24.
 */
public class GetDebtsRequest extends ResourcesRequest<Debt> {

    private String lenderId;
    private String debtorId;

    public GetDebtsRequest(ProgressDialog progressDialog, ResourcesCallback<Debt> callback,
                           String debtorId, String lenderId) {
        super(progressDialog, callback);
        this.debtorId = debtorId;
        this.lenderId = lenderId;
    }

    @Override
    protected String url() {
        return AppController.SERVER_ADDRESS + "/user/" + debtorId +
                "/debt?" + "lenderId=" + lenderId;
    }

    @Override
    protected List<Debt> parseResponse(JSONArray response) {
        return Debt.fromJson(response);
    }
}
