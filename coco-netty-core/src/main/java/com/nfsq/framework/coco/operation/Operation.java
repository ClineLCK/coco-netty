package com.nfsq.framework.coco.operation;

import com.nfsq.framework.coco.bean.CocoConnection;
import com.nfsq.framework.coco.bean.Proto;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * 操作接口类
 *
 * @author ckli01
 * @date 2019-04-19
 */
public interface Operation {


    /**
     * 操作码
     *
     * @return
     */
    int op();

    /**
     * 处理
     *
     * @param ch
     * @param proto
     * @throws Exception
     */
    void action(Channel ch, Proto proto) throws Exception;


    default CocoConnection getCocoConnection(Channel channel) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        return new CocoConnection(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
    }


}
