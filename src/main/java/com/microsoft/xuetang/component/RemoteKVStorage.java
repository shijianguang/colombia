package com.microsoft.xuetang.component;

import com.microsoft.xuetang.component.impl.ElasticRemoteKVStorage;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
public abstract class RemoteKVStorage<Value> {
    public Value get(String id) {
        return get(null, null, id);
    }
    public Value get(String db, String id) {
        return get(db, null, id);
    }
    public abstract Value get(String db, String namespace, String id);

    /**
     *
     * @param idList
     * @return null Value means the key is not exist in remote KV storage
     */
    public Map<String, Value> get(List<String> idList) {
        return get(null, null, idList);
    }
    public Map<String, Value> get(String db, List<String> idList) {
        return get(db, null, idList);
    }
    public abstract Map<String, Value> get(String db, String namespace, List<String> idList);
    public static synchronized RemoteKVStorage getRemoteKVStorage() {
        return new ElasticRemoteKVStorage();
    }
}
