package com.github.simplecache.handler;

import com.github.simplecache.storage.Cache;
import com.github.simplecache.Command;
import com.github.simplecache.Response;
import com.github.simplecache.ResponseMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ResponseHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ResponseMsg responseMsg = null;
        try {
            responseMsg = (ResponseMsg) msg;
        } catch (Exception e) {
            return;
        }


        if (responseMsg.command.cmdType == Command.CmdType.ADD) {
            ctx.channel().writeAndFlush(storeResponse(responseMsg.response));
        } else if (responseMsg.command.cmdType == Command.CmdType.GET){

            ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(responseMsg.command.getData()));
            ctx.channel().writeAndFlush(Response.CRLF.retain());
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println(cause);
    }

    private ByteBuf storeResponse(Cache.StoreResponse storeResponse) {
        switch (storeResponse) {
            case EXISTS:
                return Response.EXISTS.retain();
            case NOT_FOUND:
                return Response.NOT_FOUND.retain();
            case NOT_STORED:
                return Response.NOT_STORED.retain();
            case STORED:
                return Response.STORED.retain();

        }
        throw new RuntimeException("unknown store response from cache: " + storeResponse);
    }
}
