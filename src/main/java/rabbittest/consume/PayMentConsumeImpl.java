package rabbittest.consume;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @Package: rabbittest.consume
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/8/14 下午7:05
 */
@Component
public class PayMentConsumeImpl implements ChannelAwareMessageListener {



    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        MessageProperties messageProperties= message.getMessageProperties();




        Consumer defaultConsumer = channel.getDefaultConsumer();

        System.out.println(message.getMessageProperties().getConsumerTag());
        System.out.println(message.getMessageProperties().getAppId());
        System.out.println(message.getMessageProperties().getClusterId());
        System.out.println(message.getMessageProperties().getCorrelationIdString());
        System.out.println(message.getMessageProperties().getDeliveryTag());
        System.out.println(message.getMessageProperties().getHeaders());
        //对参数进行校验，参数不够直接返回
        String boby = new String(message.getBody(), "utf-8");
        System.out.println(String.format("活动流水:%s",boby));
        if (boby.contains("1")) {//只有等于1 的情况下回返回
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }else {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);//拒绝消息
        }
    }
}
