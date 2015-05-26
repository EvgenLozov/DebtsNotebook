package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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

    ListView lvMyDebtors;
    UserLocalStore userLocalStore;

    UsersAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userLocalStore = new UserLocalStore(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debtors, container, false);
        lvMyDebtors = (ListView) view.findViewById(R.id.lvMyDebtors);
        adapter = new UsersAdapter(getActivity(), new ArrayList<User>());
        lvMyDebtors.setAdapter(adapter);

        ProgressDialog progressDialog = Util.getProgressDialog(getActivity());
        String userId = userLocalStore.getLoggedInUser().getId();

        new GetDebtorsRequest(progressDialog, new ResourcesCallback<User>() {
            @Override
            public void done(List<User> debtors) {
                adapter.clear();
                adapter.addAll(debtors);
                adapter.notifyDataSetChanged();
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
