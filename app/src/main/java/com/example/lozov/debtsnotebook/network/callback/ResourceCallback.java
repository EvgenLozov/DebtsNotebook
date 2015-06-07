package com.example.lozov.debtsnotebook.network.callback;

/**
 * Created by lozov on 22.05.15.
 */
public interface ResourceCallback<T> {
    void onSuccess(T resource);
    void onError();
}
