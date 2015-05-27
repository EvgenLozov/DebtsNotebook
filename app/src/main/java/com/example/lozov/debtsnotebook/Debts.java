package com.example.lozov.debtsnotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lozov.debtsnotebook.network.request.GetDebtsRequest;
import com.example.lozov.debtsnotebook.network.callback.ResourcesCallback;

import java.util.ArrayList;
import java.util.List;


public class Debts extends AppCompatActivity implements EditDebtDialog.OnDebtEditedListener{

    public static final String LENDER_ID = "com.example.lozov.debtsnotebook.LENDER_ID";
    public static final String DEBTOR_ID = "com.example.lozov.debtsnotebook.DEBTOR_ID";
    public static final String TITLE = "title";

    ListView lvDebts;
    DebtsAdapter debtsAdapter;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debts);
        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        setTitle(title);

        userLocalStore = new UserLocalStore(this);

        lvDebts = (ListView) findViewById(R.id.lvDebts);

        debtsAdapter = new DebtsAdapter(this, new ArrayList<Debt>());
        lvDebts.setAdapter(debtsAdapter);

        registerForContextMenu(lvDebts);

        populateAdapter();
    }

    private void populateAdapter() {
        Intent intent = getIntent();
        String debtorId = intent.getStringExtra(DEBTOR_ID);
        String lenderId = intent.getStringExtra(LENDER_ID);

        ProgressDialog progressDialog = Util.getProgressDialog(this);

        new GetDebtsRequest(progressDialog,
                new ResourcesCallback<Debt>() {
                    @Override
                    public void done(List<Debt> debts) {
                        debtsAdapter.clear();

                        lvDebts.setAdapter(null);
                        lvDebts.addHeaderView(getHeaderView(getTotalDebt(debts)), "Total", false);

                        lvDebts.setAdapter(debtsAdapter);

                        debtsAdapter.addAll(debts);
                        debtsAdapter.notifyDataSetChanged();
                    }
                }, debtorId, lenderId).execute();
    }

    private int getTotalDebt(List<Debt> debts) {
        Integer result = 0;
        for (Debt debt : debts) {
            result += debt.getAmountOfMoney();
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View getHeaderView(int totalAmount){
        View headerView = getLayoutInflater().inflate(R.layout.debts_header, null);
        TextView tvTotalAmountOfMoney = (TextView) headerView.findViewById(R.id.tvTotalAmountOfMoney);
        tvTotalAmountOfMoney.setText(String.valueOf(totalAmount));

        return headerView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.debt_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.editDebt:
                Debt debt = (Debt) lvDebts.getItemAtPosition(info.position);
                EditDebtDialog.newInstance(debt).show(getSupportFragmentManager(), "borrow");
                return true;
            case R.id.paidUp:
                String paidText = "Paid-Up debt : " + ((Debt)lvDebts.getItemAtPosition(info.position)).getDesc();
                Toast.makeText(this, paidText, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onDebtEdited(Debt debt) {
        Toast.makeText(getApplicationContext(),
                "Debt is edited - desc: " + debt.getDesc() + ", amount: " + debt.getAmountOfMoney(),
                Toast.LENGTH_LONG).show();
    }
}
