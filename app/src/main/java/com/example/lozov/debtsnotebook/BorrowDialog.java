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
import java.util.Iterator;
import java.util.List;

/**
 * Created by lozov on 19.05.15.
 */
public class BorrowDialog extends DialogFragment {

    private static final String USER_ID_ARG = "userId";
    private static final String DEBT_TYPE_ARG = "debtType";

    public interface DebtCreationListener{
        void onDebtCreated(Debt debt);
    }

    List<User> userList = new ArrayList<>();
    UsersAdapter adapter;

    String userId;
    Debt.Type debtType;

    private DebtCreationListener debtCreationListener;


    static BorrowDialog newInstance(String userId, Debt.Type debtType) {
        BorrowDialog f = new BorrowDialog();

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

        final View view = inflater.inflate(R.layout.borrow_dialog, container);

        Spinner spinner = (Spinner) view.findViewById(R.id.sBorrower);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new UsersAdapter(getActivity(), new ArrayList<User>());

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        new ServerRequest(getActivity()).fetchUsers(new GetUsersCallback() {
            @Override
            public void done(List<User> users) {
                userList.clear();

                excludeLoggedInUser(users);
                userList.addAll(users);

                adapter.clear();
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
                String borrower = ((User) sBorrower.getSelectedItem()).getId();

                Debt debt = new Debt();
                debt.setAmountOfMoney(amountOfMoney);
                debt.setDesc(desc);

                switch (debtType){
                    case BORROWED:
                        debt.setBorrowerId(borrower);
                        debt.setDebtorId(userId);
                        break;
                    case LOANED:
                        debt.setDebtorId(borrower);
                        debt.setBorrowerId(userId);
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

    private void excludeLoggedInUser(List<User> users) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()){
            User user = iterator.next();
            if (user.getId().equals(userId)){
                iterator.remove();
                return;
            }
        }
    }
}
