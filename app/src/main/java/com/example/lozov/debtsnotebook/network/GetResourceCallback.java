package com.example.lozov.debtsnotebook.network;

/**
 * Created by lozov on 22.05.15.
 */
public interface GetResourceCallback<T> {
    void done(T resource);
}
