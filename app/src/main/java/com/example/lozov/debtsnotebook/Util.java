package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.Iterator;
import java.util.List;

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

    public static void removeUserFromList(String userToRemoveId, List<User> users) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()){
            User user = iterator.next();
            if (user.getId().equals(userToRemoveId)){
                iterator.remove();
                return;
            }
        }
    }
}
