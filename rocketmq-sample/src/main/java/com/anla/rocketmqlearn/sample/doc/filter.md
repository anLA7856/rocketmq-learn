### 过滤器示例
在很多时候，tag是简单和有用的被设计用来选择消息。
比如：
```
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_EXAMPLE");
consumer.subscribe("TOPIC", "TAGA || TAGB || TAGC");
```
这个消费者会接受消息包括：TAGA，TAGB和TAGC。但是限制是一个消息只能拥有一个tag，有时候可能不能工作在复杂的场景，在这样的案例中，你能使用SQL表达式去过滤消息。

### 原理
SQL特性可以通过在发送消息时放入的属性进行一些计算。在RocketMQ定义的语法下，您可以实现一些有趣的逻辑。这是一个例子:
```
------------
| message  |
|----------|  a > 5 AND b = 'abc'
| a = 10   |  --------------------> Gotten
| b = 'abc'|
| c = true |
------------
------------
| message  |
|----------|   a > 5 AND b = 'abc'
| a = 1    |  --------------------> Missed
| b = 'abc'|
| c = true |
------------
```

### 语法
RocketMQ只定义了一些基本的语法来支持这个特性。你可以很轻松的扩展它。
1. 数字比较。像>,>=,<,<=,BETWEEN,=;
2. 字符比较。像=，<>，IN；
3. IS NULL or IS NOT NULL；
4. 逻辑 AND、OR、NOT；

### 定义类型
1. 数字，像123，3.1415；
2. 字符，像‘abc’，必须使用单引号；
3. NULL，特殊定义；
4. 布尔，TRUE和FALSE；

### 使用约束
只有push consumer能使用SQL92来选择消息，接口是：
`public void subscribe(final String topic, final MessageSelector messageSelector)`

### 生产者示例
你可以放置属性到消息中通过方法 `putUserProperty` 当发送的时候。
```
DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
producer.start();
Message msg = new Message("TopicTest",
    tag,
    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
);
// Set some properties.
msg.putUserProperty("a", String.valueOf(i));
SendResult sendResult = producer.send(msg);
producer.shutdown();
```

### 消费者示例
使用 MessageSelector.bySql 通过SQL92来选择消息，当消费的时候。
```
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_4");

// only subsribe messages have property a, also a >=0 and a <= 3
consumer.subscribe("TopicTest", MessageSelector.bySql("a between 0 and 3");

consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
consumer.start();
```
