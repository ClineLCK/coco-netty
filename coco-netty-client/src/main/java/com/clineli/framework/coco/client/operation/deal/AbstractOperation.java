package com.clineli.framework.coco.client.operation.deal;

import com.clineli.framework.coco.bean.Proto;
import com.clineli.framework.coco.client.sync.DefaultFuture;
import com.clineli.framework.coco.operation.Operation;
import io.netty.channel.Channel;

/**
 * 操作类抽象方法
 *
 * @author ckli01
 * @date 2019-04-19
 */
public abstract class AbstractOperation implements Operation {






    @Override
    public void action(Channel ch, Proto proto) throws Exception {
        DefaultFuture.recive(proto);
    }
}
