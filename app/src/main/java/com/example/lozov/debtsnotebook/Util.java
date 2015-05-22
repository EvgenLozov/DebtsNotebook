package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by lozov on 22.05.15.
 */
public class Util {
    public static ProgressDialog getProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait..");

        return progressDialog;

    }
}
