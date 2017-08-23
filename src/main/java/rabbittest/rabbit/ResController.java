package rabbittest.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Package: rabbittest
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/8/18 下午2:47
 */
@Slf4j
@RestController
public class ResController {

    //
    @RequestMapping(value = "/log",method = RequestMethod.GET)
    public String test(String name, HttpServletRequest request){
        String remoteAddr = request.getRemoteAddr();
        System.out.println(remoteAddr);
//        log.info("clientId:[{}],message:[{}]",remoteAddr,name);
        log.error(name);
//        log.trace("clientId:[{}],message:[{}]",remoteAddr,name);
//        log.debug("clientId:[{}],message:[{}]",remoteAddr,name);
        return name;
    }
}
