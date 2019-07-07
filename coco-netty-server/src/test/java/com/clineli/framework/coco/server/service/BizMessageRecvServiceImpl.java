package com.clineli.framework.coco.server.service;

import com.clineli.framework.coco.bean.CocoEntity;
import com.clineli.framework.coco.bean.CocoResponseEntity;
import com.clineli.framework.coco.example.TestA;
import com.clineli.framework.coco.example.TestB;
import com.clineli.framework.coco.server.operation.biz.BizMessageRecvService;
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

    
    
  