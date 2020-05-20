package com.github.simplecache;

import com.github.simplecache.storage.MemoryCache;

public class bootstrap {

    public void main(String args[]) throws InterruptedException {
        new SimpleCacheServer(12000, new MemoryCache<String, String>()).star();
    }
}
