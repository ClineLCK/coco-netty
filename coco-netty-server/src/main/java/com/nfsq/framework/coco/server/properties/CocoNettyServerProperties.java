package com.nfsq.framework.coco.server.properties;

/**
 * 服务 相关配置
 *
 * @author ckli01
 * @date 2019-04-22
 */
public class CocoNettyServerProperties {


    /**
     * 监听端口
     */
    private Integer port = 10010;

    /**
     * 工作线程数
     */
    private Integer workGroupNum = 4;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWorkGroupNum() {
        return workGroupNum;
    }

    public void setWorkGroupNum(Integer workGroupNum) {
        this.workGroupNum = workGroupNum;
    }
}

    
    
  