package com.anla.rocketmqlearn.sample.offset;

import com.anla.rocketmqlearn.sample.config.SampleConstant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * 广播示例 消费者
 * @author luoan
 * @version 1.0
 * @date 2020/4/19 21:27
 **/
public class OffsetConsumerA {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("offset_group_a");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //set to broadcast mode
        // consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        final AtomicInteger i = new AtomicInteger();
        consumer.subscribe("offset_topic", MessageSelector.bySql("a =1"));
        consumer.setNamesrvAddr(SampleConstant.NAMESPACE_ADDR);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                i.incrementAndGet();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        new Thread(){
            @Override
            public void run() {
                DefaultMQPushConsumer consumerOrder = new DefaultMQPushConsumer("offset_group_a1");

                consumerOrder.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

                //set to broadcast mode
                // consumer.setMessageModel(MessageModel.BROADCASTING);
                consumerOrder.setMessageModel(MessageModel.CLUSTERING);
                final AtomicInteger iOrder = new AtomicInteger();
                try {
                    consumerOrder.subscribe("offset_topic", MessageSelector.bySql("a =1"));
                } catch (MQClientException e) {
                    e.printStackTrace();
                }
                consumerOrder.setNamesrvAddr(SampleConstant.NAMESPACE_ADDR);
                consumerOrder.registerMessageListener(new MessageListenerOrderly() {
                    @Override
                    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                        System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                        iOrder.incrementAndGet();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ConsumeOrderlyStatus.SUCCESS;
                    }
                });

                try {
                    consumerOrder.start();
                } catch (MQClientException e) {
                    e.printStackTrace();
                }
                System.out.printf("orderly Broadcast Consumer Started.%n");
            }
        }.start();

        consumer.start();



    }
}
