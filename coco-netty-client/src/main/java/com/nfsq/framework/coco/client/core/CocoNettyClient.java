package com.nfsq.framework.coco.client.core;


import com.nfsq.framework.coco.bean.CocoConnection;
import com.nfsq.framework.coco.bean.CocoRequestEntity;
import com.nfsq.framework.coco.bean.CocoResponseEntity;

/**
 * 服务 接口
 *
 * @author ckli01
 * @date 2019-04-22
 */
public interface CocoNettyClient {


    void start() throws Exception;

    void restart() throws Exception;

    void shutdown();

    void reConnect(CocoConnection cocoConnection) ;

    /**
     * 发送消息
     *
     * @param entity
     * @param <T>
     * @return
     * @throws Exception
     */
    <T, K> CocoResponseEntity<T> send(CocoRequestEntity<K> entity) throws Exception;
}
