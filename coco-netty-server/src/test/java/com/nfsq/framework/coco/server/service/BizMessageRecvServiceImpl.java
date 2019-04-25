package com.nfsq.framework.coco.server.service;

import com.nfsq.framework.coco.bean.CocoEntity;
import com.nfsq.framework.coco.bean.CocoResponseEntity;
import com.nfsq.framework.coco.example.TestA;
import com.nfsq.framework.coco.example.TestB;
import com.nfsq.framework.coco.server.operation.biz.BizMessageRecvService;
import org.springframework.stereotype.Service;

/**
 * @author ckli01
 * @date 2019-04-22
 */
@Service
public class BizMessageRecvServiceImpl implements BizMessageRecvService<TestB,TestA> {


    @Override
    public CocoResponseEntity<TestB> receive(CocoEntity<TestA> cocoEntity) {
        CocoResponseEntity<TestB> cocoResponseEntity = new CocoResponseEntity<>();


        TestB a = new TestB();
        a.setA(Integer.valueOf(cocoEntity.getData().getA().toString()));

        a.setB(cocoEntity.getData().getA().toString() + " im catch...");

        cocoResponseEntity.setSuccess(true);
        cocoResponseEntity.setData(a);

        return cocoResponseEntity;
    }
}

    
    
  