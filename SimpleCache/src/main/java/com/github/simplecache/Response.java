package com.github.simplecache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class Response {

    public static final ByteBuf CRLF = Unpooled.wrappedBuffer("\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf VALUE = Unpooled.wrappedBuffer("VALUE ".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf EXISTS = Unpooled.wrappedBuffer("EXISTS\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf NOT_FOUND = Unpooled.wrappedBuffer("NOT_FOUND\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf NOT_STORED = Unpooled.wrappedBuffer("NOT_STORED\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf STORED = Unpooled.wrappedBuffer("STORED\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf DELETED = Unpooled.wrappedBuffer("DELETED\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf END = Unpooled.wrappedBuffer("END\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf OK = Unpooled.wrappedBuffer("OK\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf ERROR = Unpooled.wrappedBuffer("ERROR\r\n".getBytes(CharsetUtil.UTF_8));
    public static final ByteBuf CLIENT_ERROR = Unpooled.wrappedBuffer("CLIENT_ERROR".getBytes(CharsetUtil.UTF_8));

}
