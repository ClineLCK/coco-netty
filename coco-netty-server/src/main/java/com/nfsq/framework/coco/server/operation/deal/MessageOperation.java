package com.nfsq.framework.coco.server.operation.deal;

import com.nfsq.framework.coco.bean.*;
import com.nfsq.framework.coco.server.operation.biz.BizMessageRecvService;
import com.nfsq.framework.coco.utils.ProtostuffUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务消息操作
 *
 * @author ckli01
 * @date 2019-04-19
 */
@Slf4j
@Service
public class MessageOperation extends AbstractOperation {


    @Autowired
    private BizMessageRecvService bizMessageRecvService;

    @Override
    public int op() {
        return Constants.OP_MESSAGE;
    }

    @Override
    public void action(Channel ch, Proto proto) throws Exception {
        CocoResponseEntity result;
        try {
            // receive a message
            result = bizMessageRecvService.receive(ProtostuffUtil.deserializer(proto.getBody(),CocoEntity.class));
        } catch (Exception e) {
            log.error("MessageOperation receive msg error for: {}", e.getMessage(), e);
            result = new CocoResponseEntity();
            result.setMessage(e.getMessage());
        }
        // write message reply
        proto.setOperation(Constants.OP_MESSAGE_REPLY);
        proto.setBody(ProtostuffUtil.serializer(result));
        ch.writeAndFlush(proto);
    }


}
