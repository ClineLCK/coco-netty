package com.clineli.framework.coco.operation.adapter;

import com.clineli.framework.coco.operation.Operation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 操作码 适配器
 *
 * @author ckli01
 * @date 2019-04-19
 */
public class CocoOperationAdapter {


    private static ConcurrentHashMap<Integer, Operation> opeMaps = new ConcurrentHashMap<>();

    /**
     * 初始化获取所有操作处理器
     *
     * @param beans
     */
    public CocoOperationAdapter(Map<String, Operation> beans) {
        if (null != beans && !beans.isEmpty()) {
            for (Operation op : beans.values()) {
                opeMaps.put(op.op(), op);
            }
        }
    }

    /**
     * 获取 具体操作码 对应 操作器
     *
     * @param op
     * @return
     */
    public static Operation find(int op) {
        return opeMaps.get(op);
    }

}

    
    
  