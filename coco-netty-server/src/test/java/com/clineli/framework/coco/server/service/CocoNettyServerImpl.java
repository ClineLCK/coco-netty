//package com.clineli.framework.coco.server.service;
//
//import com.clineli.framework.coco.channel.CocoChannelInitializer;
//import com.clineli.framework.coco.server.core.CocoNettyServer;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
///**
// * @author ckli01
// * @date 2019-04-22
// */
//@Slf4j
//@Service
//public class CocoNettyServerImpl implements CocoNettyServer {
//
//
//    private EventLoopGroup bossGroup = new NioEventLoopGroup();
//    private EventLoopGroup workGroup = new NioEventLoopGroup();
//
//    private ChannelFuture channelFuture;
//
//    @Override
//    public void start() throws Exception {
//        try {
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.group(bossGroup, workGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new CocoChannelInitializer())
//                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)    // 设置接受数据的缓存大小
//                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)    // 设置保持连接
//                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
//            ;
//
//            channelFuture = serverBootstrap.bind(10010).sync();
//
//            channelFuture.channel().closeFuture().sync();
//        } finally {
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                @Override
//                public void run() {
//                    shutdown();
//                }
//            });
//        }
//    }
//
//    @Override
//    public void restart() throws Exception {
//        shutdown();
//        start();
//    }
//
//    @Override
//    public void shutdown() {
//        if (channelFuture != null) {
//            channelFuture.channel().close().syncUninterruptibly();
//        }
//        if (bossGroup != null) {
//            bossGroup.shutdownGracefully();
//        }
//        if (workGroup != null) {
//            workGroup.shutdownGracefully();
//        }
//    }
//}
//
//
//
//