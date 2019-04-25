package com.nfsq.framework.coco.codec;

import com.nfsq.framework.coco.bean.Proto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * TCP 协议 加解密
 *
 * @author ckli01
 * @date 2019-04-19
 */
@ChannelHandler.Sharable
@Slf4j
public class TcpProtoCodec extends MessageToMessageCodec<ByteBuf, Proto> {


    private static TcpProtoCodec tcpProtoCodec;


    /**
     * 获取实例
     *
     * @return
     */
    public static TcpProtoCodec getInstance() {
        if (tcpProtoCodec != null) {
            return tcpProtoCodec;
        } else {
            synchronized (TcpProtoCodec.class) {
                if (tcpProtoCodec == null) {
                    tcpProtoCodec = new TcpProtoCodec();
                }
                return tcpProtoCodec;
            }
        }
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Proto proto, List<Object> list) throws Exception {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        if (proto.getBody() != null) {
            byteBuf.writeInt(Proto.HEADER_LENGTH + proto.getBody().length);
            byteBuf.writeShort(Proto.HEADER_LENGTH);
            byteBuf.writeShort(Proto.VERSION);
            byteBuf.writeInt(proto.getOperation());
            byteBuf.writeLong(proto.getSeqId());
            byteBuf.writeBytes(proto.getBody());
        } else {
            byteBuf.writeInt(Proto.HEADER_LENGTH);
            byteBuf.writeShort(Proto.HEADER_LENGTH);
            byteBuf.writeShort(Proto.VERSION);
            byteBuf.writeInt(proto.getOperation());
            byteBuf.writeLong(proto.getSeqId());
        }
        byteBuf.writeBytes(new byte[]{'\r','\n','\n'});

        list.add(byteBuf);
        log.debug("encode: " + proto);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Proto proto = new Proto();
        proto.setPacketLen(byteBuf.readInt());
        proto.setHeaderLen(byteBuf.readShort());
        proto.setVersion(byteBuf.readShort());
        proto.setOperation(byteBuf.readInt());
        proto.setSeqId(byteBuf.readLong());
        if (proto.getPacketLen() > proto.getHeaderLen()) {
            byte[] bytes = new byte[proto.getPacketLen() - proto.getHeaderLen()];
            byteBuf.readBytes(bytes);
            proto.setBody(bytes);
        }
        list.add(proto);
        log.debug("decode: " + proto);
    }
}
