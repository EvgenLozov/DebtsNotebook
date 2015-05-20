package com.example.lozov.debtsnotebook;

import java.util.List;

/**
 * Created by lozov on 20.05.15.
 */
public interface GetDebtsCallback {
    void done(List<Debt> debts);
}
