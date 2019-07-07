package com.clineli.framework.coco.client.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.clineli.framework.coco.bean.*;
import com.clineli.framework.coco.channel.CocoChannelInitializer;
import com.clineli.framework.coco.client.core.CocoNettyClient;
import com.clineli.framework.coco.client.properties.CocoNettyClientProperties;
import com.clineli.framework.coco.client.sync.DefaultFuture;
import com.clineli.framework.coco.utils.ProtostuffUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 客户端启动类
 *
 * @author ckli01
 * @date 2019-04-22
 */
@Slf4j
public class CocoNettyClientImpl implements CocoNettyClient {


    private CocoNettyClientProperties cocoNettyClientProperties;

    private EventLoopGroup eventLoopGroup;

    private List<ChannelFuture> channelFutures;

    private AtomicInteger aI = new AtomicInteger(0);
    private AtomicInteger atSeqId = new AtomicInteger(0);


    private Bootstrap bootstrap;

    private ConcurrentHashMap<CocoConnection, ChannelFuture> map = new ConcurrentHashMap<>();

    public CocoNettyClientImpl(CocoNettyClientProperties cocoNettyClientProperties) {
        this.cocoNettyClientProperties = cocoNettyClientProperties;
        try {
            start();
        } catch (Exception e) {
            log.error("CocoNettyClient start failed  for : {}", e.getMessage(), e);
        }
    }


    @Override
    public void start() throws Exception {
        eventLoopGroup = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new CocoChannelInitializer())
                .option(ChannelOption.TCP_NODELAY, true);
        init(bootstrap);
    }

    @Override
    public void restart() throws Exception {
        shutdown();
        start();
    }

    @Override
    public void shutdown() {
        if (!CollectionUtils.isEmpty(channelFutures)) {
            channelFutures.forEach(t -> t.channel().close().syncUninterruptibly());
        }
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
        }
    }


    @Override
    public void reConnect(CocoConnection cocoConnection) {
        log.info("CocoNettyClient start reconnect channel ip: {},channel port: {}", cocoConnection.getIp(), cocoConnection.getPort());
        String ip = cocoConnection.getIp();
        int port = cocoConnection.getPort();
        ChannelFuture channelFuture = map.get(cocoConnection);
        if (channelFuture == null) {
            log.error("CocoNettyClient reconnect lost channel connect msg -> ip: {} port: {}", ip, port);
            return;
        }
        try {
            synchronized (CocoNettyClientImpl.class) {
                bootstrap.handler(new CocoChannelInitializer());
                channelFuture = bootstrap.connect(ip,port);
            }
            // 监听 连接 情况
            channelFuture.addListener((ChannelFutureListener) f -> {
                boolean succeed = f.isSuccess();
                if (!succeed) {
                    log.info("CocoNettyClient reconnect failed for ip: {} port: {}", ip, port);
                    f.channel().eventLoop().schedule(() -> {
                        reConnect(cocoConnection);
                    }, 5, TimeUnit.SECONDS);
                } else {
                    log.info("CocoNettyClient reconnect success for ip: {} port: {}", ip, port);
                }
            });
        } catch (Throwable t) {
            log.error("CocoNettyClient reconnect channel for ip: {} port: {} failed, msg: {} ", ip, port, t.getMessage(), t);
        }
    }

    @Override
    public <T, K> CocoResponseEntity<T> send(CocoRequestEntity<K> entity) throws Exception {
        ChannelFuture channelFuture = getChannelFuture();
        if (channelFuture == null) {
            log.error("CocoNettyClient send msg failed for channel all is null...");
            throw new Exception("channelFuture cant be null");
        }
        Proto proto = new Proto();
        long seqId =atSeqId.incrementAndGet();
        proto.setOperation(entity.getOpeId());
        proto.setSeqId(seqId);
        proto.setVersion((short) 1);
        // 设置请求体外包装
        CocoEntity<K> cocoEntity = new CocoEntity<>();
        cocoEntity.setData(entity.getData());
        proto.setBody(ProtostuffUtil.serializer(cocoEntity));
        log.info("seqId: {} , msg: {}", seqId, JSONObject.toJSONString(proto));

        // 发送请求
        channelFuture.channel().writeAndFlush(proto);
        // 等待请求返回
        DefaultFuture defaultFuture = new DefaultFuture(seqId);
        Proto response = defaultFuture.get(cocoNettyClientProperties.getTimeout());

        if (response.getBody() != null) {
            return ProtostuffUtil.deserializer(response.getBody(), CocoResponseEntity.class);
        } else {
            return new CocoResponseEntity<>();
        }
    }


    /**
     * 构建 channels
     *
     * @param bootstrap
     */
    private void init(Bootstrap bootstrap) {
        String ipPortstr = cocoNettyClientProperties.getIpPort();
        if (!StringUtils.isEmpty(ipPortstr)) {
            channelFutures = new ArrayList<>();
            String[] ipPorts = ipPortstr.split(";");
            for (int i = 0; i < ipPorts.length; i++) {
                String ipPort = ipPorts[i];
                String[] ipAndPort = ipPort.split(":");
                CocoConnection cocoConnection = new CocoConnection(ipAndPort[0], Integer.valueOf(ipAndPort[1]));
                ChannelFuture channelFuture = null;
                try {
                    channelFuture = bootstrap.connect(cocoConnection.getIp(), cocoConnection.getPort()).sync();
                } catch (Exception e) {
                    log.error("CocoNettyClient Exception : {}", e.getMessage(), e);
                    continue;
                }
                map.put(cocoConnection, channelFuture);
                channelFutures.add(channelFuture);
            }
        } else {
            log.error("CocoNettyClient start failed for ipPort cant be null...  ");
        }
    }


    private ChannelFuture getChannelFuture() {
        if (!CollectionUtils.isEmpty(channelFutures)) {
            int i = aI.incrementAndGet();
            return channelFutures.get(i % channelFutures.size());

        }
        return null;
    }

}

    
    
  