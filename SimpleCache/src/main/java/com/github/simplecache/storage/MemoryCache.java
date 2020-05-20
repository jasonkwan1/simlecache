package com.github.simplecache.storage;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache<K, V> implements Cache<K, V> {

    public ConcurrentHashMap<K, V> cacheImpl = new ConcurrentHashMap<>();


    @Override
    public DeleteResponse delete(K key) throws Exception {
        return cacheImpl.remove(key) == null ? DeleteResponse.DELETED : DeleteResponse.NOT_FOUND;
    }

    @Override
    public StoreResponse add(K key, V val) throws Exception {
        return cacheImpl.putIfAbsent(key, val) == null ? StoreResponse.STORED : StoreResponse.NOT_STORED;
    }

    @Override
    public StoreResponse set(K key, V val) throws Exception {
        return cacheImpl.putIfAbsent(key, val) == null ? StoreResponse.STORED : StoreResponse.NOT_STORED;
    }

    @Override
    public StoreResponse replace(K key, V newVal) throws Exception {
        return cacheImpl.replace(key, newVal) == null ? StoreResponse.STORED : StoreResponse.NOT_STORED;
    }

    @Override
    public byte[] get(K key) throws Exception {
        return cacheImpl.get(key) == null ? "(nil)".getBytes() : (byte[]) cacheImpl.get(key) ;
    }
}
