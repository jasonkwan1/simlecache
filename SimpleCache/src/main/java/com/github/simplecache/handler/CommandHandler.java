package com.github.simplecache.handler;

import com.github.simplecache.storage.Cache;
import com.github.simplecache.Command;
import com.github.simplecache.ResponseMsg;
import com.github.simplecache.SessionStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CommandHandler  extends ChannelInboundHandlerAdapter {

    private SessionStatus status;

    private Cache cache;

    public CommandHandler(SessionStatus status, Cache cache){
        this.status = status;
        this.cache = cache;
    }

    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) throws Exception {
        Command command = null;
        try{
            command = (Command)msg;
        } catch (Exception e){
            return;
        }

        Command.CmdType cmdType = command.cmdType;

        if(cmdType == Command.CmdType.ADD){
            handleAdd(ctx, command);
        } else if(cmdType == Command.CmdType.GET){
            handleGet(ctx, command);
        }
    }

    private void handleAdd(ChannelHandlerContext ctx, Command command) throws Exception {
        Cache.StoreResponse ret = cache.add(command.getKey(), command.getData());

        ctx.fireChannelRead(new ResponseMsg(command).withResponse(ret));
    }

    private void handleGet(ChannelHandlerContext ctx, Command command) throws Exception{
        byte[] bytes = cache.get(command.getKey());
        command.setData(bytes);
        ctx.fireChannelRead(new ResponseMsg(command));
    }
}
