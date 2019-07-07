package com.clineli.framework.coco.server.configuration;

import com.clineli.framework.coco.operation.Operation;
import com.clineli.framework.coco.operation.adapter.CocoOperationAdapter;
import com.clineli.framework.coco.server.core.impl.CocoNettyServerImpl;
import com.clineli.framework.coco.server.operation.deal.MessageOperation;
import com.clineli.framework.coco.server.operation.deal.ServerHeartbeatOperation;
import com.clineli.framework.coco.server.properties.CocoNettyServerProperties;
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
public class CocoNettyServerAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(value = ServerHeartbeatOperation.class)
    public Operation serverHeartbeatOperation() {
        return new ServerHeartbeatOperation();
    }

    @Bean
    @ConditionalOnMissingBean(value = MessageOperation.class)
    public Operation messageOperation() {
        return new MessageOperation();
    }


    @Bean
    @ConditionalOnBean(value = {ApplicationContext.class, Operation.class})
    public CocoOperationAdapter cocoOperationAdapter(ApplicationContext applicationContext) {
        return new CocoOperationAdapter(applicationContext.getBeansOfType(Operation.class));
    }


    @Bean
    @ConfigurationProperties(prefix = "coco.netty.server")
    @ConditionalOnMissingBean(value = CocoNettyServerProperties.class)
    public CocoNettyServerProperties cocoNettyServerProperties() {
        return new CocoNettyServerProperties();
    }

    @Bean
    @ConditionalOnBean(value = CocoNettyServerProperties.class)
    public CocoNettyServerImpl cocoNettyServer(CocoNettyServerProperties cocoNettyServerProperties) {
        return new CocoNettyServerImpl(cocoNettyServerProperties);
    }


}
