package com.anla.rocketmqlearn.sample.filter;

import java.io.File;
import java.io.IOException;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.MixAll;

/**
 *
 * 好像已经过时了
 * @author luoan
 * @version 1.0
 * @date 2020/6/20 13:29
 **/
public class FilterConsumer {

    public static void main(String[] args) throws IOException, MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("anla_FilterConsumer_test");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource("MessageFilterDemoImpl.java").getFile());
        String filterCode = MixAll.file2String(file);
        consumer.subscribe("TopicTest","com.anla.rocketmqlearn.sample.filter.MessageFilterDemoImpl", filterCode);
    }
}
