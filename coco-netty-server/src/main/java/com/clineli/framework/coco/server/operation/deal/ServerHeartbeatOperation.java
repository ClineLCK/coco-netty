package com.clineli.framework.coco.server.operation.deal;

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
public class ServerHeartbeatOperation extends AbstractOperation {


    // todo 如果有几万个TCP 长连接 维护几万个心跳 考虑优化
    private ConcurrentHashMap<CocoConnection, Integer> map = new ConcurrentHashMap<>();

    @Override
    public int op() {
        return Constants.OP_HEARTBEAT;
    }

    @Override
    public void action(Channel ch, Proto proto) throws Exception {
        CocoConnection cocoConnection=getCocoConnection(ch);
        if (null == proto) {
            // 客户端未进行ping心跳发送的次数等于3,断开此连接
            Integer aI = map.computeIfAbsent(cocoConnection, k -> 0);
            map.put(cocoConnection, ++aI);
            log.info("CocoNettyServer HeartbeatOperation client connect no ping times: {} ", aI);
            if (aI >= 3) {
                ch.disconnect();
                log.info("CocoNettyServer HeartbeatOperation client connect timeout for ip: {} -> close connect", cocoConnection.getIp());
                map.remove(cocoConnection);
            }
        } else {
            proto.setOperation(Constants.OP_HEARTBEAT);
            proto.setBody(null);
            ch.writeAndFlush(proto);
            log.info("CocoNettyServer HeartbeatOperation client response heartBeat");
            map.put(cocoConnection, 0);
        }
    }


}
