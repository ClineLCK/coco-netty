package com.clineli.framework.coco.client.operation.deal;

import com.clineli.framework.coco.bean.CocoConnection;
import com.clineli.framework.coco.bean.Constants;
import com.clineli.framework.coco.bean.Proto;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 心跳
 *
 * @author ckli01
 * @date 2019-04-19
 */
@Service
@Slf4j
public class ClientHeartbeatOperation extends AbstractOperation {


    // todo 如果有几万个TCP 长连接 维护几万个心跳 考虑优化
    private ConcurrentHashMap<CocoConnection, Integer> map = new ConcurrentHashMap<>();

    @Override
    public int op() {
        return Constants.OP_HEARTBEAT;
    }

    @Override
    public void action(Channel ch, Proto proto) throws Exception {
        CocoConnection cocoConnection = getCocoConnection(ch);

        if (null == proto) {
            // 服务端未进行心跳响应的次数小于3,则进行发送心跳，否则则断开连接
            Integer aI = map.computeIfAbsent(cocoConnection, k -> 0);
            map.put(cocoConnection, ++aI);
            if (aI <= 3) {
                proto = new Proto();
                proto.setOperation(Constants.OP_HEARTBEAT);
                // 发送心跳
                ch.writeAndFlush(proto);
                log.info("CocoNettyClient HeartbeatOperation times: {} heartBeat", aI);
            } else {
                // 重连机制
                map.put(cocoConnection, 0);
                ch.close();
            }
        } else {
            map.put(cocoConnection, 0);
            log.info("CocoNettyClient HeartbeatOperation receive response heartBeat");
        }


    }


}
