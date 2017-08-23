package rabbittest.rabbit;


import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * @Package: pterosaur.account.config.rabbit
 * @Description: 统一处理，异常，然后发送邮件通知
 * @author: liuxin
 * @date: 17/4/27 上午10:33
 */
@Component
public class ConsumerErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {

    }
}
