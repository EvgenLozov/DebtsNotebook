package com.example.lozov.debtsnotebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lozov on 19.05.15.
 */
public class Debt implements Serializable{
    private String id;
    private String debtorId;
    private String lenderId;
    private Status status;
    private Integer amountOfMoney;
    private String desc;
    private Date date;

    public Debt() {
    }

    public Debt(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.debtorId = jsonObject.getString("debtorId");
            this.lenderId = jsonObject.getString("lenderId");
//            this.status = jsonObject.getString("status");
            this.amountOfMoney = jsonObject.getInt("amountOfMoney");
            this.desc = jsonObject.getString("desc");
//            this.date = jsonObject.get("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getLenderId() {
        return lenderId;
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Integer amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static List<Debt> fromJson(JSONArray jsonArray) {
        ArrayList<Debt> debts = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                debts.add(new Debt(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return debts;
    }

    public static enum Status{
        OPEN, CLOSED
    }

    public static enum Type{
        BORROWED, LOANED
    }
}
