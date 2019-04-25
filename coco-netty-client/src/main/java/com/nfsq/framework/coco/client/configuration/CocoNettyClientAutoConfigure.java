package com.nfsq.framework.coco.client.configuration;

import com.nfsq.framework.coco.client.core.impl.CocoNettyClientImpl;
import com.nfsq.framework.coco.client.operation.deal.ClientHeartbeatOperation;
import com.nfsq.framework.coco.client.operation.deal.MessageOperation;
import com.nfsq.framework.coco.client.operation.deal.ReConnectOperation;
import com.nfsq.framework.coco.client.properties.CocoNettyClientProperties;
import com.nfsq.framework.coco.operation.Operation;
import com.nfsq.framework.coco.operation.adapter.CocoOperationAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化注入bean
 *
 * @author ckli01
 * @date 2018/8/29
 */
@Configuration
public class CocoNettyClientAutoConfigure {


    @Bean
    @ConditionalOnMissingBean(value = ClientHeartbeatOperation.class)
    public Operation clientHeartbeatOperation() {
        return new ClientHeartbeatOperation();
    }

    @Bean
    @ConditionalOnMissingBean(value = MessageOperation.class)
    public Operation messageOperation() {
        return new MessageOperation();
    }


    @Bean
    @ConditionalOnMissingBean(value = MessageOperation.class)
    public Operation reConnectOperation() {
        return new ReConnectOperation();
    }

    @Bean
    @ConditionalOnBean(value = {ApplicationContext.class, Operation.class})
    public CocoOperationAdapter cocoOperationAdapter(ApplicationContext applicationContext) {
        return new CocoOperationAdapter(applicationContext.getBeansOfType(Operation.class));
    }


    @Bean
    @ConfigurationProperties(prefix = "coco.netty.client")
    @ConditionalOnMissingBean(value = CocoNettyClientProperties.class)
    public CocoNettyClientProperties cocoNettyServerProperties() {
        return new CocoNettyClientProperties();
    }

    @Bean
    @ConditionalOnBean(value = CocoNettyClientProperties.class)
    public CocoNettyClientImpl cocoNettyServer(CocoNettyClientProperties cocoNettyClientProperties) {
        return new CocoNettyClientImpl(cocoNettyClientProperties);
    }


}
