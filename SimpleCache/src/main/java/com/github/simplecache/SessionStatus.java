package com.github.simplecache;

public class SessionStatus {

    public static enum State {
        WAITING_FOR_DATA,
        READY,
        PROCESSING,
        PROCESSING_MULTILINE,
    }

    // the state the session is in
    public State state;

    public Command command;

    // the size of the data we are waiting
    public int bytesNeeded;

    public SessionStatus ready(){
        this.state = State.READY;
        return this;
    }

    public SessionStatus processing() {
        this.state = State.PROCESSING;
        return this;
    }

    public SessionStatus processingMultiline() {
        this.state = State.PROCESSING_MULTILINE;
        return this;
    }

    public SessionStatus needMore(int size, Command command){
        this.command = command;
        this.bytesNeeded = size;
        this.state = State.WAITING_FOR_DATA;
        return this;
    }
}
