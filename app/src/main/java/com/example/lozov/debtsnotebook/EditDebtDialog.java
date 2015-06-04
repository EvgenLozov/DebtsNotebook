package com.example.lozov.debtsnotebook;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class EditDebtDialog extends DialogFragment {
    public static final String ARG_DEBT_OBJECT = "debtObject";

    private Debt debt;

    private EditText etAmountOfMoney;
    private EditText etDebtDesc;

    private OnDebtEditedListener mListener;

    public static EditDebtDialog newInstance(Debt debt) {
        EditDebtDialog fragment = new EditDebtDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEBT_OBJECT, debt);
        fragment.setArguments(args);
        return fragment;
    }

    public EditDebtDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            debt = (Debt) getArguments().getSerializable(ARG_DEBT_OBJECT);
        }
        setStyle(android.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_debt_dialog, container);

        etDebtDesc = (EditText) view.findViewById(R.id.etDebtDesc);
        etDebtDesc.setText(debt.getDesc());

        etAmountOfMoney = (EditText) view.findViewById(R.id.etAmountOfMoney);
        etAmountOfMoney.setText(debt.getAmountOfMoney().toString());

        Button bSaveDebt = (Button) view.findViewById(R.id.bSaveDebt);
        bSaveDebt.setOnClickListener(new OnSaveDebtListener());

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDebtEditedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDebtEditedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDebtEditedListener {
        void onDebtEdited(Debt debt);
    }

    private class OnSaveDebtListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String newDesc = etDebtDesc.getText().toString();
            String newAmountOfMoney = etAmountOfMoney.getText().toString();

            debt.setDesc(newDesc);
            debt.setAmountOfMoney(Integer.valueOf(newAmountOfMoney));

            mListener.onDebtEdited(debt);
            getDialog().dismiss();
        }
    }
}
