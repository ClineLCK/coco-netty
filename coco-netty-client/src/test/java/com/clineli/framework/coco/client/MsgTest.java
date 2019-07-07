package com.clineli.framework.coco.client;

import com.alibaba.fastjson.JSONObject;
import com.clineli.framework.coco.bean.CocoResponseEntity;
import com.clineli.framework.coco.client.bean.CocoBizRequestEntity;
import com.clineli.framework.coco.client.core.CocoNettyClient;
import com.clineli.framework.coco.example.TestA;
import com.clineli.framework.coco.example.TestB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * @author ckli01
 * @date 2019-04-22
 */
@Service
public class MsgTest {


    @Autowired
    private CocoNettyClient cocoNettyClient;


//    @PostConstruct
    public void send() {

        Executors.newSingleThreadExecutor().execute(() -> {

            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < 20000; i++) {
                try {

                    CocoBizRequestEntity<TestA> cocoRequestEntity = new CocoBizRequestEntity<>();

                    TestA a = new TestA();
                    a.setA(i);
                    a.setB(UUID.randomUUID().toString());

                    cocoRequestEntity.setData(a);

                    CocoResponseEntity<TestB> o = cocoNettyClient.send(cocoRequestEntity);
                    System.out.println(JSONObject.toJSONString(o));
                } catch (Exception e) {
                    e.printStackTrace();
//                Assert.fail();
                }


            }


            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });


    }
}

    
    
  