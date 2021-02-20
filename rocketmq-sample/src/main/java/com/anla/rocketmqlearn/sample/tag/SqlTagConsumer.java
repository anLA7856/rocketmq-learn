package com.anla.rocketmqlearn.sample.tag;

import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 *
 *
 * ：基于SQL92 表达式消息过滤， 其实是对悄息的属性运用SQL 过滤表达式进行
 * 条件匹配，所以消息发送时应该调用 putUserProperty 方法设置消息属性。
 * @author luoan
 * @version 1.0
 * @date 2020/6/20 13:25
 **/
public class SqlTagConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_4");

        // only subsribe messages have property a, also a >=0 and a <= 3
        consumer.subscribe("TopicTest", MessageSelector.bySql("a between 0 and 3"));

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
