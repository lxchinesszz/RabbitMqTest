package rabbittest.rabbit;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rabbittest.consume.PayMentConsumeImpl;

import java.util.HashMap;
import java.util.Map;


/**
 * @Package: pterosaur.account.config.rabbit
 * @Description: 绑定mq
 * @author: liuxin
 * @date: 17/4/25 上午11:08
 */
//@Configuration
//@EnableAutoConfiguration
//@ConfigurationProperties(prefix = "spring.rabbitmq")
public class TopicRabbitConfig {


    /**
     * 配置链接信息，记得必须配置
     * 当发现有死信，就将发送到改死亡交换机中
     *
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("127.0.0.1", 5672);
        connectionFactory.setUsername("liuxin");
        connectionFactory.setPassword("950914lx");
        connectionFactory.setVirtualHost("az");
        connectionFactory.setPublisherConfirms(true); // 必须要设置回调

        Channel channel = connectionFactory.createConnection().createChannel(false);
        channel.confirmSelect();

        //String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("internal",true);
        //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        //设置AE交换机
        channel.exchangeDeclare("alter", "fanout", false, false, false, arguments);
        channel.queueDeclare("alter_message", false, false, false, null);
        channel.queueBind("alter_message", "alter", "");

        //声明死信交换机并绑定
        channel.exchangeDeclare("dead_letter_exchange", "direct", false, false, null);
        channel.queueDeclare("dead", false, false, false, null);
        channel.queueBind("dead", "dead_letter_exchange", "task_queue.fail");


        arguments = new HashMap<>();
        arguments.put("alternate-exchange", "alter");//指定AE交换机
        channel.exchangeDeclare("test", "direct", false, false, arguments);
        //声明接受正式的队列，不需要参数
        channel.queueDeclare("hello", false, false, false, null);
        channel.queueBind("hello", "test", "hello");

        arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "dead_letter_exchange");
        arguments.put("x-dead-letter-routing-key", "task_queue.fail");
        arguments.put("x-message-ttl",6000);//6s没有被处理，就死了
        //设置测试死信队列的task_queue，推送该队列里面，被拒绝会到dead_letter_exchange，并最终到dead，routeKey，task_queue.fail 为并设置死信队列参数
        channel.queueDeclare("task_queue", false, false, false, arguments);
        channel.queueBind("task_queue", "test", "task_queue");

        return connectionFactory;
    }


    /**
     * 接受消息的监听，这个监听客户交易流水的消息
     * 针对消费者配置
     *
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer1(ConnectionFactory connectionFactory, PayMentConsumeImpl transactionConsume) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.addQueueNames("hello");
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(8);
        container.setConcurrentConsumers(4);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认,当设置了此模式，必须返回ACK，否则会进入死信队列
        container.setMessageListener(transactionConsume);
        container.setPrefetchCount(1000);
        return container;
    }


}

