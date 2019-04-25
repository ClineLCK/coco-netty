package com.nfsq.framework.coco.channel;

import com.nfsq.framework.coco.codec.TcpProtoCodec;
import com.nfsq.framework.coco.handler.CocoChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 处理channel
 *
 * @author ckli01
 * @date 2018/8/9
 */
public class CocoChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ByteBuf delimiter = Unpooled.copiedBuffer("\r\n\n".getBytes());
        socketChannel.pipeline()
                .addFirst(new DelimiterBasedFrameDecoder(8192, delimiter))
                .addLast(new IdleStateHandler(5, 4, 0, TimeUnit.SECONDS))
                .addLast(TcpProtoCodec.getInstance())
                .addLast(CocoChannelHandler.getInstance());
    }
}
