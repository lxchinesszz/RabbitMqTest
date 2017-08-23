package rabbittest.rabbit;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @Package: pterosaur.account.config.rabbit
 * @Description: ${todo}
 * @author: liuxin
 * @date: 17/4/26 下午7:29
 */
@Component
public class HellRev implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println(new String (message.getBody(),"utf-8"));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
