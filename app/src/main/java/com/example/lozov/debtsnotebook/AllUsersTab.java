package com.example.lozov.debtsnotebook;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;
import com.example.lozov.debtsnotebook.network.request.GetUsersRequest;

import java.util.ArrayList;
import java.util.List;


public class AllUsersTab extends Fragment {

    private ListView lvAllUsers;
    private UsersAdapter usersAdapter;

    private UserLocalStore userLocalStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        userLocalStore = new UserLocalStore(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_users_tab, container, false);
        lvAllUsers = (ListView) view.findViewById(R.id.lvAllUsers);
        usersAdapter = new UsersAdapter(getActivity(), new ArrayList<User>());
        lvAllUsers.setAdapter(usersAdapter);

        ProgressDialog progressDialog = Util.getProgressDialog(getActivity());

        new GetUsersRequest(progressDialog, new ResourcesCallback<User>() {
            @Override
            public void done(List<User> users) {
                usersAdapter.clear();

                Util.removeUserFromList(userLocalStore.getLoggedInUser().getId(), users);

                usersAdapter.addAll(users);
                usersAdapter.notifyDataSetChanged();
            }
        }).execute();

        return view;
    }


}
