package com.anla.rocketmqlearn.springboot.sample.consumer.config;

import com.anla.rocketmqlearn.springboot.sample.consumer.domain.User;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

/**
 * The consumer that replying Object
 */
@Service
@RocketMQMessageListener(topic = "${demo.rocketmq.objectRequestTopic}", consumerGroup = "${demo.rocketmq.objectRequestConsumer}", selectorExpression = "${demo.rocketmq.tag}")
public class ObjectConsumerWithReplyUser implements RocketMQReplyListener<User, User> {

    @Override
    public User onMessage(User user) {
        System.out.printf("------- ObjectConsumerWithReplyUser received: %s \n", user);
        User replyUser = new User();
        replyUser.setUserAge((byte) 10);
        replyUser.setUserName("replyUserName");
        return replyUser;
    }
}
