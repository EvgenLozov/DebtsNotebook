package com.example.lozov.debtsnotebook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lozov on 19.05.15.
 */
public class BorrowDialog extends DialogFragment {

    public interface DebtCreationListener{
        void onDebtCreated(Debt debt);
    }

    List<User> userList = new ArrayList<>();
    UsersAdapter adapter;

    private DebtCreationListener debtCreationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.borrow_dialog, container);

        Spinner spinner = (Spinner) view.findViewById(R.id.sBorrower);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new UsersAdapter(getActivity(), new ArrayList<User>());

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        new ServerRequest(getActivity()).fetchUsers(new GetUsersCallback() {
            @Override
            public void done(List<User> users) {
                userList.addAll(users);
                adapter.addAll(users);
                adapter.notifyDataSetChanged();
            }
        });

        debtCreationListener = (DebtCreationListener) getActivity();

        Button button = (Button)view.findViewById(R.id.bCreateDebt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText etDesc = (EditText) view.findViewById(R.id.etDebtDesc);
                String desc = etDesc.getText().toString();

                EditText etAmountOfMoney = (EditText) view.findViewById(R.id.etAmountOfMoney);
                Integer amountOfMoney = Integer.valueOf(etAmountOfMoney.getText().toString());

                Spinner sBorrower = (Spinner) view.findViewById(R.id.sBorrower);
                String borrower = userList.get(sBorrower.getSelectedItemPosition()).getId();

                Debt debt = new Debt();
                debt.setBorrowerId(borrower);
                debt.setAmountOfMoney(amountOfMoney);
                debt.setDesc(desc);

                debtCreationListener.onDebtCreated(debt);
                getDialog().dismiss();
            }
        });

        return view;
    }
}
