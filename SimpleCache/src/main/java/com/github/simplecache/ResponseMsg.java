package com.github.simplecache;

import com.github.simplecache.storage.Cache;

public class ResponseMsg {

    public Command command;

    public Cache.StoreResponse response;

    public ResponseMsg(Command command){
        this.command = command;
    }

    public ResponseMsg withResponse(Cache.StoreResponse response){
        this.response = response;
        return this;
    }
}
