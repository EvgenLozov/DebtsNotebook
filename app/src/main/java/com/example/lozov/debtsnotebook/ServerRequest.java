package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yevhen on 2015-05-16.
 */
public class ServerRequest {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://stark-peak-7912.herokuapp.com";

    ProgressDialog progressDialog;

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait..");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new FetchUserDataAsyncTask(user, userCallback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void>{
        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback) {
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
        GetUserCallback userCallback;

        public FetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... voids) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpPost httpPost = new HttpPost(SERVER_ADDRESS + "/login");

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

                if (jsonObject.length() > 0){
                    String email = jsonObject.getString("email");

                    returnedUser = new User(email, user.getUsername(), user.getPassword());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
