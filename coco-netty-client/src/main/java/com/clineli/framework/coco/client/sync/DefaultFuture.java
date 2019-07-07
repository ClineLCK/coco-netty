package com.clineli.framework.coco.client.sync;

import com.clineli.framework.coco.bean.Proto;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 管理请求和响应的关系
 * 主要是通过唯一的请求标识id
 *
 * @author ckli01
 * @date 2019-04-22
 */
@Slf4j
public class DefaultFuture {

    //请求id
    private long id;

    //请求id对应的响应结果
    private volatile Proto response;

    //存储响应结果和自身绑定在一起
    public final static Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>();

    //超时时间
    private long timeout;

    private final long start = System.currentTimeMillis();

    //获取锁
    private volatile Lock lock = new ReentrantLock();

    //线程通知条件
    private volatile Condition condition = lock.newCondition();

    public DefaultFuture(long seqId) {
        id = seqId;//获取对应的请求ID
        FUTURES.put(seqId, this);//存储当前的请求ID对应的上下文信息
    }


    /**
     * 根据请求id获取响应结果
     *
     * @param timeout
     * @return
     */
    public Proto get(long timeout) {
        long start = System.currentTimeMillis();
        lock.lock();//先锁
        while (!hasDone()) {
            try {
                condition.await(timeout, TimeUnit.SECONDS);
                long ti = (System.currentTimeMillis() - start) / 1000;
                if (ti >= timeout) {
                    break;
                }
            } catch (InterruptedException e) {
                log.error("CocoNettyClient DefaultFuture : {} ", e.getMessage(), e);
            } finally {
                lock.unlock();//释放锁
            }
        }
        return response;
    }

    /**
     * 存储服务器端的响应
     *
     * @param res
     */
    public static void recive(Proto res) {
        //找到res相对应的DefaultFuture
        DefaultFuture future = FUTURES.remove(res.getSeqId());
        if (future == null) {
            return;
        }
        Lock lock = future.getLock();
        lock.lock();
        try {
            //设置响应
            future.setResponse(res);
            Condition condition = future.getCondition();
            if (condition != null) {
                //通知
                condition.signal();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public void setResponse(Proto response) {
        this.response = response;
    }

    private boolean hasDone() {
        return response != null;
    }

    public long getId() {
        return id;
    }


    public Lock getLock() {
        return lock;
    }

    public Condition getCondition() {
        return condition;
    }

    public long getTimeout() {
        return timeout;
    }


    public long getStart() {
        return start;
    }


    /**
     * 处理请求超时的线程
     */
    static class FutureTimeOutThread extends Thread {

        @Override
        public void run() {

            while (true) {
                for (long futureId : FUTURES.keySet()) {
                    DefaultFuture f = FUTURES.get(futureId);
                    if (f == null) {
                        FUTURES.remove(futureId);//为空的话 代表请求结果已经处理完毕了
                        continue;
                    }
                    if (f.getTimeout() > 0) {
                        long ti = (System.currentTimeMillis() - f.getStart()) / 1000;
                        if (ti > f.getTimeout()) {
                            Proto res = new Proto();
                            res.setBody("请求超时！".getBytes());
                            res.setSeqId(f.getId());
                            DefaultFuture.recive(res);//存储服务端的响应结果信息
                        }
                    }


                }


            }
        }

    }

    /**
     * 设置为后台线程
     */
    static {
        FutureTimeOutThread timeOutThread = new FutureTimeOutThread();
        timeOutThread.setDaemon(true);
        timeOutThread.start();
    }


}
