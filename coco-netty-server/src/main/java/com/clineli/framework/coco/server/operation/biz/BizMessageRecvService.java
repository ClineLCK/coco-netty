package com.clineli.framework.coco.server.operation.biz;


import com.clineli.framework.coco.bean.CocoEntity;
import com.clineli.framework.coco.bean.CocoResponseEntity;

/**
 * 业务消息处理
 *
 * @author ckli01
 * @date 2019-04-22
 */
public interface BizMessageRecvService<T,K> {

    /**
     * 业务信息操作
     *
     * @param cocoEntity
     * @return
     */
    CocoResponseEntity<T> receive(CocoEntity<K> cocoEntity);
}

    
    
  