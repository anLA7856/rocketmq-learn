package com.anla.rocketmqlearn.sample.broadcast;

import com.anla.rocketmqlearn.sample.config.SampleConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/** 广播示例
 * @author luoan
 * @version 1.0
 * @date 2020/4/19 21:26
 **/
public class BroadcastProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("BroadcastProducerProducerGroupName");
        producer.setNamesrvAddr(SampleConstant.NAMESPACE_ADDR);
        producer.start();

        for (int i = 0; i < 100; i++){
            Message msg = new Message("BroadcastProducerTopicTest",
                    "TagA",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        producer.shutdown();
    }
}
