package com.clineli.framework.coco.server.core;

/**
 * 服务 接口
 *
 * @author ckli01
 * @date 2019-04-22
 */
public interface CocoNettyServer {


    void start() throws Exception;

    void restart() throws Exception;

    void shutdown();

}
