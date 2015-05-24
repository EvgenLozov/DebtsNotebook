package com.example.lozov.debtsnotebook.network.callback;

import java.util.List;

/**
 * Created by lozov on 22.05.15.
 */
public interface ResourcesCallback<T> {
    void done(List<T> resources);
}
