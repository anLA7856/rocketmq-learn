package com.anla.rocketmqlearn.sample.batch;

import com.anla.rocketmqlearn.sample.config.SampleConstant;
import java.util.ArrayList;
import java.util.List;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * @author luoan
 * @version 1.0
 * @date 2020/6/20 13:13
 **/
public class SimpleBatchProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("anla_BatchProducerGroupName");
        producer.setNamesrvAddr(SampleConstant.NAMESPACE_ADDR);
        producer.start();
        String topic = "anla_BatchProducerGroupName_topic";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "Tag", "OrderIDOOl ", "Hello world".getBytes()));
        messages.add(new Message(topic, "Tag", "OrderID002", "Hello world".getBytes()));
        messages.add(new Message(topic, "Tag", "Order 工D003", "Hello world".getBytes()));
        System.out.println(producer.send(messages));
        producer.shutdown();
    }
}
