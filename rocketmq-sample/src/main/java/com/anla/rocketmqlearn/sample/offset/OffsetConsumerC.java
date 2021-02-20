package com.anla.rocketmqlearn.sample.offset;

import com.anla.rocketmqlearn.sample.config.SampleConstant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * 广播示例 消费者
 * @author luoan
 * @version 1.0
 * @date 2020/4/19 21:27
 **/
public class OffsetConsumerC {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("offset_group_a");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));

        //set to broadcast mode
        // consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        final AtomicInteger i = new AtomicInteger();
        consumer.subscribe("offset_topic", MessageSelector.bySql("a =3"));
        consumer.setNamesrvAddr(SampleConstant.NAMESPACE_ADDR);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
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

        consumer.start();
        System.out.printf("Broadcast Consumer Started.%n");
    }
}
