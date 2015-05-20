package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yevhen on 2015-05-16.
 */
public class ServerRequest {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
//    public static final String SERVER_ADDRESS = "http://stark-peak-7912.herokuapp.com";
    public static final String SERVER_ADDRESS = "http://10.0.2.2:8080";
    private static final String LOGIN_URL = "/login";

    ProgressDialog progressDialog;

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait..");
    }

    public void storeUserDataInBackground(User user, GetUsersCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUsersCallback userCallback){
        progressDialog.show();
        new FetchUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchBorrowers(User user, GetUsersCallback callback){
        progressDialog.show();
        new FetchBorrowersAsyncTask(callback).execute(user.getId());
    }

    public void fetchDebtors(User user, GetUsersCallback callback){
        progressDialog.show();
        new FetchDebtorsAsyncTask(callback).execute(user.getId());
    }

    public void fetchUsers(GetUsersCallback callback){
        progressDialog.show();
        new FetchUsersAsyncTask(callback).execute();
    }

    public void createDebt(Debt debt){
        progressDialog.show();
        new CreateDebtAsyncTask(debt).execute();
    }

    public void fetchDebts(User loggedInUser, String borrowerId, GetDebtsCallback callback) {
        progressDialog.show();
        new FetchDebtsAsyncTask(loggedInUser, borrowerId, callback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void>{
        User user;
        GetUsersCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUsersCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpPost httpPost = new HttpPost(SERVER_ADDRESS + "/user");

            JSONObject dataToSend = new JSONObject();
            try {
                dataToSend.put("email", user.getEmail());
                dataToSend.put("username", user.getUsername());
                dataToSend.put("password", user.getPassword());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpPost.addHeader("Content-Type", "application/json");

            try {
                httpPost.setEntity(new StringEntity(dataToSend.toString()));
                httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User>{
        User user;
        GetUsersCallback userCallback;

        public FetchUserDataAsyncTask(User user, GetUsersCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... voids) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpPost httpPost = new HttpPost(SERVER_ADDRESS + LOGIN_URL);

            List<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.getUsername()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));

            User returnedUser = null;

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);

                returnedUser = new User(jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(Collections.singletonList(returnedUser));
            super.onPostExecute(returnedUser);
        }
    }

    public class FetchBorrowersAsyncTask extends AsyncTask<String, Void, List<User>>{

        GetUsersCallback callback;

        public FetchBorrowersAsyncTask(GetUsersCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<User> doInBackground(String... params) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "/user/" + params[0] + "/borrowers");

            List<User> returnedUsers = new ArrayList<>();
            try{
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);

                JSONArray jsonArray = new JSONArray(responseString);

                returnedUsers = User.fromJson(jsonArray);

            } catch (Exception e){
                e.printStackTrace();
            }

            return returnedUsers;
        }

        @Override
        protected void onPostExecute(List<User> returnedUsers) {
            progressDialog.dismiss();
            callback.done(returnedUsers);
            super.onPostExecute(returnedUsers);
        }
    }

    public class FetchUsersAsyncTask extends AsyncTask<Void, Void, List<User>>{

        GetUsersCallback callback;

        public FetchUsersAsyncTask(GetUsersCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "/user");

            List<User> returnedUsers = new ArrayList<>();
            try{
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);

                JSONArray jsonArray = new JSONArray(responseString);

                returnedUsers = User.fromJson(jsonArray);

            } catch (Exception e){
                e.printStackTrace();
            }

            return returnedUsers;
        }

        @Override
        protected void onPostExecute(List<User> returnedUsers) {
            progressDialog.dismiss();
            callback.done(returnedUsers);
            super.onPostExecute(returnedUsers);
        }
    }

    public class CreateDebtAsyncTask extends AsyncTask<Void, Void, Void>{

        Debt debt;

        public CreateDebtAsyncTask(Debt debt) {
            this.debt = debt;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpPost httpPost = new HttpPost(SERVER_ADDRESS + "/user/" + debt.getDebtorId() + "/debt");

            JSONObject dataToSend = new JSONObject();
            try {
                dataToSend.put("debtorId", debt.getDebtorId());
                dataToSend.put("borrowerId", debt.getBorrowerId());
                dataToSend.put("amountOfMoney", debt.getAmountOfMoney());
                dataToSend.put("desc", debt.getDesc());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpPost.addHeader("Content-Type", "application/json");

            try {
                httpPost.setEntity(new StringEntity(dataToSend.toString()));
                httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    public class FetchDebtsAsyncTask extends AsyncTask<Void, Void, List<Debt>>{
        User user;
        String borrowerId;
        GetDebtsCallback callback;

        public FetchDebtsAsyncTask(User user, String borrowerId, GetDebtsCallback callback) {
            this.user = user;
            this.borrowerId = borrowerId;
            this.callback = callback;
        }

        @Override
        protected List<Debt> doInBackground(Void... params) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "/user/" + user.getId() +
                                                            "/debt?" + "borrowerId=" + borrowerId);

            List<Debt> returnedDebts = new ArrayList<>();
            try{
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);

                JSONArray jsonArray = new JSONArray(responseString);

                returnedDebts = Debt.fromJson(jsonArray);

            } catch (Exception e){
                e.printStackTrace();
            }

            return returnedDebts;
        }

        @Override
        protected void onPostExecute(List<Debt> debts) {
            progressDialog.dismiss();
            callback.done(debts);
            super.onPostExecute(debts);
        }
    }

    private class FetchDebtorsAsyncTask extends AsyncTask<String, Void, List<User>>{

        GetUsersCallback callback;

        public FetchDebtorsAsyncTask(GetUsersCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<User> doInBackground(String... params) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "/user/" + params[0] + "/debtors");

            List<User> returnedUsers = new ArrayList<>();
            try{
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);

                JSONArray jsonArray = new JSONArray(responseString);

                returnedUsers = User.fromJson(jsonArray);

            } catch (Exception e){
                e.printStackTrace();
            }

            return returnedUsers;
        }

        @Override
        protected void onPostExecute(List<User> returnedUsers) {
            progressDialog.dismiss();
            callback.done(returnedUsers);
            super.onPostExecute(returnedUsers);
        }
    }
}
