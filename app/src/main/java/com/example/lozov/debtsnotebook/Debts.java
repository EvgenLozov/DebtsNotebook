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

import com.example.lozov.debtsnotebook.network.callback.ResourceCallback;
import com.example.lozov.debtsnotebook.network.request.EditDebtRequest;
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
    List<Debt> debtsList;

    UserLocalStore userLocalStore;

    private String debtorId;
    private String lenderId;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debts);
        Intent intent = getIntent();
        debtorId = intent.getStringExtra(DEBTOR_ID);
        lenderId = intent.getStringExtra(LENDER_ID);

        title = intent.getStringExtra(TITLE);
        setTitle(title);

        userLocalStore = new UserLocalStore(this);

        debtsList = new ArrayList<>();
        debtsAdapter = new DebtsAdapter(this, new ArrayList<Debt>());

        lvDebts = (ListView) findViewById(R.id.lvDebts);
        lvDebts.setAdapter(debtsAdapter);

        registerForContextMenu(lvDebts);

        populateAdapter();
    }

    private void populateAdapter() {
        ProgressDialog progressDialog = Util.getProgressDialog(Debts.this);

        new GetDebtsRequest(progressDialog,
                new ResourcesCallback<Debt>() {
                    @Override
                    public void done(List<Debt> debts) {
                        debtsAdapter.clear();
                        debtsAdapter.clear();

                        debtsList.addAll(debts);
                        debtsAdapter.addAll(debts);
                        debtsAdapter.sort(new Debt.ByStatusCompataror());
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
        getMenuInflater().inflate(R.menu.menu_debts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        ProgressDialog progressDialog = Util.getProgressDialog(Debts.this);
        new EditDebtRequest(progressDialog,
                new ResourceCallback<Debt>() {
                    @Override
                    public void done(Debt debt) {
                        for (Debt debt1 : debtsList) {
                            if(debt1.getId().equals(debt.getId())){
                                debt1.setDesc(debt.getDesc());
                                debt1.setAmountOfMoney(debt.getAmountOfMoney());
                                debt1.setStatus(debt.getStatus());
                                break;
                            }
                        }
                        debtsAdapter.notifyDataSetChanged();
                    }
                }, debt).execute();
    }
}
