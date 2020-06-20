package com.anla.rocketmqlearn.sample.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

import static com.anla.rocketmqlearn.sample.config.SampleConstant.NAMESPACE_ADDR;

/**
 *
 *
 * 消息发送默认根据主题的路由信息（主题消息队列）进行负载均衡，负载均衡机制为轮
 * 询策略。例如现在有这样一个场景，订单的状态变更消息发送到特定主题，为了避免消息
 * 消费者同时消费同一订单的不同状态的变更消息，我们应该使用顺序消息。为了提高消息
 * 消费的并发度，如果我们能根据某种负载算法，相同订单的不同消息能统一发到同一个消
 * 息消费队列上，则可以避免引人分布式锁， RocketMQ 在消息发送时提供了消息队列选择器
 * MessageQueueSe lee tor 。
 *
 * 有序发送消息示例，生产者
 * @author luoan
 * @version 1.0
 * @date 2020/4/19 21:04
 **/
public class OrderedProducer {
    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("OrderedProducer_group_name");
        producer.setNamesrvAddr(NAMESPACE_ADDR);
        //Launch the instance.
        producer.start();
        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 100; i++) {
            int orderId = i % 10;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("OrderedProducerTopicTest", tags[i % tags.length], "KEY" + i,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            System.out.printf("%s%n", sendResult);
        }
        //server shutdown
        producer.shutdown();
    }
}
