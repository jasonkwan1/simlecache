package com.github.simplecache.handler;

import com.github.simplecache.Command;
import com.github.simplecache.Response;
import com.github.simplecache.SessionStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CommandDecoder extends ChannelInboundHandlerAdapter {

    private SessionStatus status;

    public CommandDecoder(SessionStatus status) {
        this.status = status;
    }

    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        Charset USASCII = Charset.forName("US-ASCII");

        try{
            if (status.state == SessionStatus.State.PROCESSING) {
                List<String> pieces = new ArrayList<>();

                int pos = in.bytesBefore((byte) ' ');
                do {
                    if (pos != -1) {
                        pieces.add(in.toString(in.readerIndex(), pos, USASCII));
                        in.skipBytes(pos + 1);
                    }
                } while ((pos = in.bytesBefore((byte) ' ')) != -1);
                pieces.add(in.toString(USASCII));
                processLine(pieces, ctx);
            } else if (status.state == SessionStatus.State.PROCESSING_MULTILINE) {
                ByteBuf slice = in.copy();
                in.skipBytes(in.readableBytes());

                String data = slice.toString(slice.readerIndex(), slice.readableBytes(), USASCII);

                status.command.setData(data.getBytes());
                ctx.fireChannelRead(status.command);
            }
        } finally {
                // Now indicate that we need more for this command by changing the
                // session status's state.
                // This instructs the frame decoder to start collecting data for us.
                // Note, we don't do this if we're waiting for data.
                if (status.state != SessionStatus.State.WAITING_FOR_DATA) {
                    status.ready();
                }
        }

    }

    private void processLine(List<String> parts, ChannelHandlerContext ctx) {
        int numParts = parts.size();

        Command.CmdType cmdType;
        try {
            cmdType = Command.CmdType.valueOf(parts.get(0).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("unknown command: " + parts.get(0).toLowerCase());
        }

        Command cmd = new Command(cmdType);

        if (cmdType == Command.CmdType.ADD || cmdType == Command.CmdType.SET || cmdType == Command.CmdType.REPLACE) {

            if (numParts < 3) {
                throw new RuntimeException("invalid command length");
            }

            int size = Integer.parseInt(parts.get(2));
            String key = parts.get(1);
            cmd.setKey(key);
            status.needMore(size, cmd);
        } else if(cmdType == Command.CmdType.GET){
            if(numParts < 2){
                throw new RuntimeException("invalid command length");
            }

            cmd.setKey(parts.get(1));
            ctx.fireChannelRead(cmd);
        } else {
            ctx.channel().writeAndFlush(Response.ERROR.duplicate());
        }
    }
}
