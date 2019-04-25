package com.nfsq.framework.coco.bean;

/**
 * 返回体
 *
 * @author ckli01
 * @date 2019-04-23
 */
public class CocoResponseEntity<T> extends CocoEntity<T> {


    /**
     * 请求状态
     */
    private boolean success;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

    
    
  