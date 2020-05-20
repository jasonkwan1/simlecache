package com.github.simplecache.storage;

public interface Cache<K, V> {
    /**
     * Enum defining response statuses from set/add type commands
     */
    public enum StoreResponse {
        STORED, NOT_STORED, EXISTS, NOT_FOUND
    }

    /**
     * Enum defining responses statuses from removal commands
     */
    public enum DeleteResponse {
        DELETED, NOT_FOUND
    }
    /**
     * Handle the deletion of an item from the cache.
     *
     * @param key
     *            the key for the item
     * @return the message response
     */
    DeleteResponse delete(K key) throws Exception;
    /**
     * Add an element to the cache
     *
     * @param key
     *            the key to add
     * @param val
     *            the val to add
     * @return the store response code
     */
    StoreResponse add(K key, V val) throws Exception;
    /**
     * Set an element in the cache
     *
     * @param key
     *            the key to set
     * @param val
     *            the val to set
     */
    StoreResponse set(K key, V val) throws Exception;
    /**
     * Replace an element in the cache
     *
     * @param key
     *            the key to replace
     * @param newVal
     *            the newVal to set
     * @return the store response code
     */
    StoreResponse replace(K key, V newVal) throws Exception;
    /**
     * Get element from the cache
     *
     * @param key
     *            the key for the element to lookup
     * @return the element, or 'null' in case of cache miss.
     */
    byte[] get(K key) throws Exception;
}
