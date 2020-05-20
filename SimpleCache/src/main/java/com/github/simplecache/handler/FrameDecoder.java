package com.github.simplecache.handler;

import com.github.simplecache.SessionStatus;
/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class FrameDecoder extends ByteToMessageDecoder {

    private SessionStatus status;

    public FrameDecoder(SessionStatus status) {
        this.status = status;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        int minFrameLength = Integer.MAX_VALUE;
        ByteBuf foundDelimiter = null;

        String crlf = "\r\n";
        byte[] bytes = crlf.getBytes(CharsetUtil.UTF_8);
        foundDelimiter = Unpooled.wrappedBuffer(bytes);

        if (status.state == SessionStatus.State.WAITING_FOR_DATA) {
            // check the size
            if(byteBuf.readableBytes() < status.bytesNeeded + foundDelimiter.capacity()){
                channelHandlerContext.fireChannelRead(null);
            }

            // check the delimiter
            ByteBuf dest = byteBuf.slice(status.bytesNeeded + byteBuf.readerIndex(), 2);

            if (!dest.equals(foundDelimiter)) {

                status.ready();
                throw new RuntimeException("payload not terminated correctly");
            } else {
                status.processingMultiline();

                ByteBuf ret = byteBuf.slice(byteBuf.readerIndex(), status.bytesNeeded);
                byteBuf.skipBytes(status.bytesNeeded + foundDelimiter.capacity());

                channelHandlerContext.fireChannelRead(ret);
            }

        } else {
            int frameLength = byteBuf.bytesBefore(byteBuf.readerIndex(), byteBuf.readableBytes(), (byte) '\r') < 0
                    ? byteBuf.bytesBefore(byteBuf.readerIndex(), byteBuf.readableBytes(), (byte) '\n')
                    : byteBuf.bytesBefore(byteBuf.readerIndex(), byteBuf.readableBytes(), (byte) '\r');

            if (frameLength >= 0 && frameLength < minFrameLength && byteBuf.readableBytes() >= frameLength + 2) {
                minFrameLength = frameLength;
            }

            if (frameLength == minFrameLength) {
                int minDelimiterLength = foundDelimiter.capacity();
                ByteBuf frame = byteBuf.slice(byteBuf.readerIndex(), minFrameLength);
                byteBuf.skipBytes(minFrameLength + minDelimiterLength);
                status.processing();
                channelHandlerContext.fireChannelRead(frame);
            }
        }
    }
}
