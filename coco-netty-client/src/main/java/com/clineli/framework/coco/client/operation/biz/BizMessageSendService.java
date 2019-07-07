package com.clineli.framework.coco.client.operation.biz;


/**
 * 业务消息处理
 *
 * @author ckli01
 * @date 2019-04-22
 */
public interface BizMessageSendService {

    /**
     * 业务信息发送操作
     *
     * @param bytes
     * @return
     */
    Object send(byte[] bytes);
}

    
    
  