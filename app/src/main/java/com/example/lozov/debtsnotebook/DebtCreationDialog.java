package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lozov.debtsnotebook.network.request.GetUsersRequest;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lozov on 19.05.15.
 */
public class DebtCreationDialog extends DialogFragment {

    private static final String USER_ID_ARG = "userId";
    private static final String DEBT_TYPE_ARG = "debtType";

    public interface DebtCreationListener{
        void onDebtCreated(Debt debt);
    }

    UsersAdapter adapter;

    String userId;
    Debt.Type debtType;

    private DebtCreationListener debtCreationListener;


    static DebtCreationDialog newInstance(String userId, Debt.Type debtType) {
        DebtCreationDialog f = new DebtCreationDialog();

        Bundle args = new Bundle();
        args.putString(USER_ID_ARG, userId);
        args.putString(DEBT_TYPE_ARG, debtType.toString());
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString(USER_ID_ARG);
        debtType = Debt.Type.valueOf(getArguments().getString(DEBT_TYPE_ARG));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setDialogTitle();

        final View view = inflater.inflate(R.layout.debt_creation_dialog, container);

        Spinner spinner = (Spinner) view.findViewById(R.id.sUser);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new UsersAdapter(getActivity(), new ArrayList<User>());

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        ProgressDialog progressDialog = Util.getProgressDialog(getActivity());

        new GetUsersRequest(progressDialog,
                            new ResourcesCallback<User>() {
                                @Override
                                public void done(List<User> users) {
                                    Util.removeUserFromList(userId, users);

                                    adapter.clear();
                                    adapter.addAll(users);
                                    adapter.notifyDataSetChanged();
                                }
                          }).execute();

        debtCreationListener = (DebtCreationListener) getActivity();

        Button button = (Button)view.findViewById(R.id.bCreateDebt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText etDesc = (EditText) view.findViewById(R.id.etDebtDesc);
                String desc = etDesc.getText().toString();

                EditText etAmountOfMoney = (EditText) view.findViewById(R.id.etAmountOfMoney);
                Integer amountOfMoney = Integer.valueOf(etAmountOfMoney.getText().toString());

                Spinner sUser = (Spinner) view.findViewById(R.id.sUser);
                String lender = ((User) sUser.getSelectedItem()).getId();

                Debt debt = new Debt();
                debt.setAmountOfMoney(amountOfMoney);
                debt.setDesc(desc);
                debt.setDate(new Date());
                debt.setStatus(Debt.Status.OPEN);

                switch (debtType){
                    case BORROWED:
                        debt.setLenderId(lender);
                        debt.setDebtorId(userId);
                        break;
                    case LOANED:
                        debt.setDebtorId(lender);
                        debt.setLenderId(userId);
                }

                debtCreationListener.onDebtCreated(debt);
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void setDialogTitle() {
        switch (debtType){
            case BORROWED:
                getDialog().setTitle("Borrow");
                break;
            case LOANED:
                getDialog().setTitle("Lend");
                break;
        }
    }
}
