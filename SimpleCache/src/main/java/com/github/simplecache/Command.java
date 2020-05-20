package com.github.simplecache;

public class Command {

    public static enum CmdType {
        GET, GETS, APPEND, PREPEND, DELETE, DECR,
        INCR, REPLACE, ADD, SET, CAS, STATS, VERSION,
        QUIT, FLUSH_ALL
    }

    public Command (CmdType cmdType){
        this.cmdType = cmdType;
    }

    public CmdType cmdType;

    private String key;

    private byte[] data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
