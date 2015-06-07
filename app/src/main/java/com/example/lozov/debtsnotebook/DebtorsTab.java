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
import com.example.lozov.debtsnotebook.network.request.GetDebtorsRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lozov on 26.05.15.
 */
public class DebtorsTab extends Fragment {

    private static final int DEBTORS_FRAGMENT_GROUP_ID = 101;
    ListView lvMyDebtors;
    UserLocalStore userLocalStore;

    ProgressDialog progressDialog;
    UsersAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userLocalStore = new UserLocalStore(getActivity());
        progressDialog = DialogUtil.getProgressDialog(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debtors, container, false);
        lvMyDebtors = (ListView) view.findViewById(R.id.lvMyDebtors);
        adapter = new UsersAdapter(getActivity(), new ArrayList<User>());
        lvMyDebtors.setAdapter(adapter);

        String userId = userLocalStore.getLoggedInUser().getId();

        DialogUtil.showProgressDialog(progressDialog);
        new GetDebtorsRequest(new ResourcesCallback<User>() {
            @Override
            public void onSuccess(List<User> debtors) {
                DialogUtil.dismissProgressDialog(progressDialog);

                adapter.clear();
                adapter.addAll(debtors);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                DialogUtil.dismissProgressDialog(progressDialog);
            }
        }, userId).execute();

        lvMyDebtors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), Debts.class);
                intent.putExtra(Debts.LENDER_ID, userLocalStore.getLoggedInUser().getId());
                intent.putExtra(Debts.DEBTOR_ID, ((User) lvMyDebtors.getItemAtPosition(position)).getId());
                intent.putExtra(Debts.TITLE, "Debts of " + ((User) lvMyDebtors.getItemAtPosition(position)).getUsername());
                startActivity(intent);
            }
        });
        return view;
    }
}
