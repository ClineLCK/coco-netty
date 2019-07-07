package com.clineli.framework.coco;

import com.clineli.framework.coco.bean.Constants;
import com.clineli.framework.coco.bean.Proto;
import com.clineli.framework.coco.channel.CocoChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.*;

/**
 * 客户端测试类
 *
 * @author ckli01
 * @date 2019-04-19
 */
public class ClientApplication {


    public static void main(String[] args) throws InterruptedException {


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new CocoChannelInitializer())
                    .option(ChannelOption.TCP_NODELAY, true);

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 10010).sync();


            // 心跳包
            executor.schedule(() -> {
                Proto proto1 = new Proto();
                proto1.setVersion((short) 1);
                proto1.setOperation(Constants.OP_HEARTBEAT);
                proto1.setBody("ajsdisjdiasjd".getBytes());
                channelFuture.channel().writeAndFlush(proto1);
            }, 1, TimeUnit.SECONDS);


            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
            executor.shutdown();
        }
    }


}

    
    
  