package com.anla.rocketmqlearn.sample.order;

import com.anla.rocketmqlearn.sample.config.SampleConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 有序消息的消费者
 * @author luoan
 * @version 1.0
 * @date 2020/4/19 21:17
 **/
public class OrderedConsumer {
    public static void main(String[] args) throws Exception {
        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("OrderedConsumer_group_name");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("OrderedProducerTopicTest", "TagA");
        consumer.setNamesrvAddr(SampleConstant.NAMESPACE_ADDR);
        consumer.registerMessageListener(new MessageListenerOrderly() {

            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
                                                       ConsumeOrderlyContext context) {
                context.setAutoCommit(false);  // 永远不进入dlq
                // 如果失败，每隔1s重试一次
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
