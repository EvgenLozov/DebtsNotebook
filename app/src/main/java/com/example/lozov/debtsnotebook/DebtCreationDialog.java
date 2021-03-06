package com.example.lozov.debtsnotebook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lozov.debtsnotebook.network.request.GetUsersRequest;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import java.util.ArrayList;
import java.util.Date;
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

    ProgressDialog progressDialog;

    SpinnerUserAdapter adapter;

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
        if (getArguments() != null){
            userId = getArguments().getString(USER_ID_ARG);
            debtType = Debt.Type.valueOf(getArguments().getString(DEBT_TYPE_ARG));
        }
        setStyle(android.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light);

        progressDialog = DialogUtil.getProgressDialog(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.debt_creation_dialog, container);

        TextView tvDialogTitle = (TextView) view.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText(getDialogTitle());

        Spinner spinner = (Spinner) view.findViewById(R.id.sUser);
        adapter = new SpinnerUserAdapter(getActivity(), new ArrayList<User>());

        spinner.setAdapter(adapter);

        DialogUtil.showProgressDialog(progressDialog);
        new GetUsersRequest(new ResourcesCallback<User>() {
                                @Override
                                public void onSuccess(List<User> users) {
                                    DialogUtil.dismissProgressDialog(progressDialog);

                                    DialogUtil.removeUserFromList(userId, users);
                                    adapter.clear();
                                    adapter.addAll(users);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError() {
                                    DialogUtil.dismissProgressDialog(progressDialog);
                                }
        }).execute();

        debtCreationListener = (DebtCreationListener) getActivity();

        Button button = (Button)view.findViewById(R.id.bCreateDebt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText etDesc = (EditText) view.findViewById(R.id.etDebtDesc);
                String desc = etDesc.getText().toString();
                if (TextUtils.isEmpty(desc)){
                    etDesc.setError("Invalid value");
                    etDesc.requestFocus();
                    return;
                }

                EditText etAmountOfMoney = (EditText) view.findViewById(R.id.etAmountOfMoney);
                String amountOfMoney = etAmountOfMoney.getText().toString();
                if (!Debt.isAmountOfMoneyValid(amountOfMoney)){
                    etAmountOfMoney.setError("Invalid value");
                    etAmountOfMoney.requestFocus();
                    return;
                }

                Spinner sUser = (Spinner) view.findViewById(R.id.sUser);
                String lender = ((User) sUser.getSelectedItem()).getId();

                Debt debt = new Debt();
                debt.setAmountOfMoney(Integer.valueOf(amountOfMoney));
                debt.setDesc(desc);
                debt.setStatus(Debt.Status.OPEN);

                Date date = new Date();
                debt.setCreatedAt(date);
                debt.setLastModified(date);

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

    private String getDialogTitle() {
        switch (debtType){
            case BORROWED:
                return "Borrow money";
            case LOANED:
                return "Lend money";
        }
        return "Create debt";
    }
}
