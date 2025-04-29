package com.shangan.trade.coupon.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageReceiver {

    /**
     * topics = {"test-topic"} 可同时接收多个topic
     * @param message
     */
    @KafkaListener(topics = {"test-topic"})
    public void consumeMessage(String message) {
        log.info("接收到消息 message:{}", message);
    }
}
