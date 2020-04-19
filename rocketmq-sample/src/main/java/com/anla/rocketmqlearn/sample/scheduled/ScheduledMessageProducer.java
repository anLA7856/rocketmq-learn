package com.anla.rocketmqlearn.sample.scheduled;

import com.anla.rocketmqlearn.sample.config.SampleConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * 预定的消息与正常消息不同，因为它们将在稍后提供的时间内发送。
 * @author luoan
 * @version 1.0
 * @date 2020/4/19 21:36
 **/
public class ScheduledMessageProducer {
    public static void main(String[] args) throws Exception {
        // Instantiate a producer to send scheduled messages
        DefaultMQProducer producer = new DefaultMQProducer("ScheduledMessageExampleConsumer");
        producer.setNamesrvAddr(SampleConstant.NAMESPACE_ADDR);
        // Launch producer
        producer.start();
        int totalMessagesToSend = 100;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("ScheduledMessageTestTopic", ("Hello scheduled message " + i).getBytes());
            // This message will be delivered to consumer 10 seconds later.
            message.setDelayTimeLevel(3);
            // Send the message
            System.out.println(message);
            producer.send(message);
        }

        // Shutdown producer after use.
        producer.shutdown();
    }
}
