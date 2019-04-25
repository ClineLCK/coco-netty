package com.nfsq.framework.coco.bean;

/**
 * 请求 返回 基类
 *
 * @author ckli01
 * @date 2019-04-23
 */
public class CocoEntity<T> {


    /**
     * 请求体
     */
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

    
    
  