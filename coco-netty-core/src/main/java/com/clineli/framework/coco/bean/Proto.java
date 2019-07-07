package com.clineli.framework.coco.bean;

import java.io.Serializable;

/**
 * 协议类
 *
 * @author ckli01
 * @date 2019-04-19
 */
public class Proto implements Serializable {


    /**
     * 默认消息头长度 16
     */
    public static final short HEADER_LENGTH = 16;
    /**
     * 默认消息版本
     */
    public static final short VERSION = 1;

    /**
     * 包长度
     */
    private int packetLen;
    /**
     * 消息头长度
     */
    private short headerLen;
    /**
     * 版本
     */
    private short version;
    /**
     * 操作码
     */
    private int operation;
    /**
     * 请求唯一Id
     */
    private long seqId;

    /**
     * 消息体
     */
    private byte[] body;

    public int getPacketLen() {
        return packetLen;
    }

    public void setPacketLen(int packetLen) {
        this.packetLen = packetLen;
    }

    public short getHeaderLen() {
        return headerLen;
    }

    public void setHeaderLen(short headerLen) {
        this.headerLen = headerLen;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }


    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        String text;
        if (body == null) {
            text = "null";
        } else {
            text = new String(body);
        }
        return "Proto{" +
                "packetLen=" + packetLen +
                ", headerLen=" + headerLen +
                ", version=" + version +
                ", operation=" + operation +
                ", seqId=" + seqId +
                ", body=" + text +
                '}';
    }
}
