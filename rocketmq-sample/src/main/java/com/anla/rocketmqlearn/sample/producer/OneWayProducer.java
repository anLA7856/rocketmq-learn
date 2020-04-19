package com.anla.rocketmqlearn.sample.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import static com.anla.rocketmqlearn.sample.config.SampleConstant.NAMESPACE_ADDR;

/**
 * @author luoan
 * @version 1.0
 * @date 2020/4/19 21:00
 **/
public class OneWayProducer {
    public static void main(String[] args) throws Exception{
        //实例化一个生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("OneWayProducerGroup");
        producer.setNamesrvAddr(NAMESPACE_ADDR);
        //运行这个实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            //创建一个消息实例，指定topic，tag和消息体
            Message msg = new Message("OneWayTopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            System.out.println(msg);
            //调用发送去投递到一个代理
            producer.sendOneway(msg);

        }
        //不在使用的生产者关闭
        producer.shutdown();
    }
}
