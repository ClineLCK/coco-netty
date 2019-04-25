package com.nfsq.framework.coco.client.properties;

/**
 * 服务 相关配置
 *
 * @author ckli01
 * @date 2019-04-22
 */
public class CocoNettyClientProperties {


    /**
     * 连接 ip 端口
     *  ; 隔离
     */
    private String ipPort = "10.213.4.176:10010";
//    private String ipPort = "127.0.0.1:10010";

    /**
     * 超时时间
     */
    private Integer timeout = 5;


    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}

    
    
  