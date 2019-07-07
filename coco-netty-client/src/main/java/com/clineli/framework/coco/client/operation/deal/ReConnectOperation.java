package com.clineli.framework.coco.client.operation.deal;

import com.clineli.framework.coco.bean.CocoConnection;
import com.clineli.framework.coco.bean.Constants;
import com.clineli.framework.coco.bean.Proto;
import com.clineli.framework.coco.client.core.CocoNettyClient;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 重连
 *
 * @author ckli01
 * @date 2019-04-25
 */
@Service
@Slf4j
public class ReConnectOperation extends AbstractOperation {

    @Autowired
    private CocoNettyClient cocoNettyClient;

    @Override
    public int op() {
        return Constants.OP_RECONNECT;
    }

    @Override
    public void action(Channel ch, Proto proto) throws Exception {
        CocoConnection cocoConnection = getCocoConnection(ch);
        log.info("CocoNettyClient HeartbeatOperation timeout -> will do client restart ");
        cocoNettyClient.reConnect(cocoConnection);
    }


}

    
    
  