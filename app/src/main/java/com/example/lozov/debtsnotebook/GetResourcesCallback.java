package com.example.lozov.debtsnotebook;

import java.util.List;

/**
 * Created by lozov on 22.05.15.
 */
public interface GetResourcesCallback<T> {
    void done(List<T> resources);
}
