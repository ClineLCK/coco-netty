package com.nfsq.framework.coco.bean;

import lombok.EqualsAndHashCode;

/**
 * 连接 端口 ip 信息
 *
 * @author ckli01
 * @date 2019-04-24
 */
@EqualsAndHashCode
public class CocoConnection {


    private Integer port;

    private String ip;


    public CocoConnection(String ip, Integer port) {
        this.port = port;
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

    
    
  