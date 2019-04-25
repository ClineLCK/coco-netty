package com.nfsq.framework.coco.client.operation.deal;

import com.nfsq.framework.coco.bean.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 业务消息返回操作
 *
 * @author ckli01
 * @date 2019-04-19
 */
@Service
@Slf4j
public class MessageOperation extends AbstractOperation {


    @Override
    public int op() {
        return Constants.OP_MESSAGE_REPLY;
    }


}
