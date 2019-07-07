package com.clineli.framework.coco.bean;

/**
 * 请求体
 *
 * @author ckli01
 * @date 2019-04-23
 */
public class CocoRequestEntity<T> extends CocoEntity<T> {


    /**
     * 操作码
     */
    private Integer opeId;


    public Integer getOpeId() {
        return opeId;
    }

    public void setOpeId(Integer opeId) {
        this.opeId = opeId;
    }
}

    
    
  