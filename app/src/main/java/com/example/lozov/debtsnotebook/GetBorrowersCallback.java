package com.example.lozov.debtsnotebook;

import java.util.List;

/**
 * Created by lozov on 18.05.15.
 */
public interface GetBorrowersCallback {
    void done(List<User> borrowers);
}
