package com.clineli.framework.coco;

import com.clineli.framework.coco.channel.CocoChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端 测试类
 *
 * @author ckli01
 * @date 2019-04-19
 */
public class ServerApplication {

    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new CocoChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)    // 设置接受数据的缓存大小
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)    // 设置保持连接
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
            ;

            ChannelFuture channelFuture = serverBootstrap.bind(10010).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }


}

    
    
  