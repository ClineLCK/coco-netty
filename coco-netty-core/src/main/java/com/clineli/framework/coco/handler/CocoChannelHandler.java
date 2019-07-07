package com.clineli.framework.coco.handler;

import com.clineli.framework.coco.bean.Constants;
import com.clineli.framework.coco.bean.Proto;
import com.clineli.framework.coco.operation.Operation;
import com.clineli.framework.coco.operation.adapter.CocoOperationAdapter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理channel
 *
 * @author ckli01
 * @date 2018/8/9
 */
@Slf4j
@ChannelHandler.Sharable
public class CocoChannelHandler extends SimpleChannelInboundHandler<Proto> {

    private static CocoChannelHandler cocoChannelHandler;

    /**
     * 获取实例
     *
     * @return
     */
    public static CocoChannelHandler getInstance() {
        if (cocoChannelHandler != null) {
            return cocoChannelHandler;
        } else {
            synchronized (CocoChannelHandler.class) {
                if (cocoChannelHandler == null) {
                    cocoChannelHandler = new CocoChannelHandler();
                }
                return cocoChannelHandler;
            }
        }
    }


    /**
     * 读取消息
     *
     * @param ctx 通道上下文
     * @param msg 协议
     * @throws Exception 异常
     */
    protected void channelRead0(ChannelHandlerContext ctx, Proto msg) throws Exception {
        // 根据获取 操作码 选取对应处理类
        Operation op = CocoOperationAdapter.find(msg.getOperation());
        if (op != null) {
            log.debug("CocoNetty receive msg operation: {}", op.op());
            op.action(ctx.channel(), msg);
        } else {
            log.warn("Not found operation opId: {}", msg.getOperation());
        }
    }

    /**
     * 客户端连接
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端与服务端连接开启");
    }

    /**
     * 客户端关闭
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        log.info("客户端与服务端连接关闭...");
        Operation op = CocoOperationAdapter.find(Constants.OP_RECONNECT);
        if (op != null) {
            op.action(ctx.channel(), null);
        }
    }


    /**
     * 异常消息
     *
     * @param ctx   通道上下文
     * @param cause 线程
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常消息: {}", cause.getMessage(), cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //  连接超时处理
                Operation op = CocoOperationAdapter.find(Constants.OP_HEARTBEAT);
                if (op != null) {
                    op.action(ctx.channel(), null);
                }
            } else if (event.state() == IdleState.WRITER_IDLE
            ) {
                // 写超时
                log.debug("CocoNetty CocoChannelHandler WRITER_IDLE -> 写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
                // 总时间超时
                log.debug("CocoNetty CocoChannelHandler ALL_IDLE -> 总时间超时");
            }
        }
    }


}
