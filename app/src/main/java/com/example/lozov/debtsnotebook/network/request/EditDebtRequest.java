package com.example.lozov.debtsnotebook.network.request;

import android.app.ProgressDialog;

import com.android.volley.Request;
import com.example.lozov.debtsnotebook.Debt;
import com.example.lozov.debtsnotebook.network.AppController;
import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lozov on 28.05.15.
 */
public class EditDebtRequest extends ResourceRequest<Debt> {

    private Debt debt;

    public EditDebtRequest(ProgressDialog progressDialog, ResourceCallback<Debt> callback, Debt debt) {
        super(progressDialog, callback);
        this.debt = debt;
    }

    @Override
    protected String url() {
        return AppController.SERVER_ADDRESS + "/user/" + debt.getDebtorId() + "/debt/" + debt.getId();
    }

    @Override
    protected int method() {
        return Request.Method.PUT;
    }

    @Override
    protected JSONObject body() {
        JSONObject dataToSend = new JSONObject();
        try {
            dataToSend.put("id", debt.getId());
            dataToSend.put("debtorId", debt.getDebtorId());
            dataToSend.put("lenderId", debt.getLenderId());
            dataToSend.put("amountOfMoney", debt.getAmountOfMoney());
            dataToSend.put("desc", debt.getDesc());
            dataToSend.put("status", debt.getStatus());
//                dataToSend.put("date", debt.getDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataToSend;
    }

    @Override
    protected Debt parseResponse(JSONObject response) {
        return new Debt(response);
    }
}
