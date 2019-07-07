package com.clineli.framework.coco.client.bean;

import com.clineli.framework.coco.bean.CocoRequestEntity;
import com.clineli.framework.coco.bean.Constants;

/**
 * 心跳请求实体类
 *
 * @author ckli01
 * @date 2019-04-23
 */
public class CocoHeartBeatEntity extends CocoRequestEntity {


    @Override
    public Integer getOpeId() {
        return Constants.OP_HEARTBEAT;
    }
}

    
    
  