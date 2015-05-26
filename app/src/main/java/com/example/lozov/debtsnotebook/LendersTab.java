package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;
import com.example.lozov.debtsnotebook.network.request.GetLendersRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lozov on 26.05.15.
 */
public class LendersTab extends Fragment {
    UsersAdapter adapter;

    ListView lvMyLenders;
    UserLocalStore userLocalStore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userLocalStore = new UserLocalStore(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lenders, container, false);
        lvMyLenders = (ListView) view.findViewById(R.id.lvMyLenders);

        adapter = new UsersAdapter(getActivity(), new ArrayList<User>());
        lvMyLenders.setAdapter(adapter);

        ProgressDialog progressDialog = Util.getProgressDialog(getActivity());

        new GetLendersRequest(progressDialog, new ResourcesCallback<User>() {
            @Override
            public void done(List<User> lenders) {
                adapter.clear();
                adapter.addAll(lenders);
                adapter.notifyDataSetChanged();
            }
        }, userLocalStore.getLoggedInUser().getId()).execute();

        lvMyLenders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), Debts.class);
                intent.putExtra(Debts.LENDER_ID, ((User) lvMyLenders.getItemAtPosition(position)).getId());
                intent.putExtra(Debts.DEBTOR_ID, userLocalStore.getLoggedInUser().getId());
                intent.putExtra(Debts.TITLE, "Debts to " + ((User) lvMyLenders.getItemAtPosition(position)).getUsername());
                startActivity(intent);
            }
        });
        return view;
    }
}
