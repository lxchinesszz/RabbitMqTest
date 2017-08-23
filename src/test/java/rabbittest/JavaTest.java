package rabbittest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Package: rabbittest
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/8/15 上午9:40
 */
public class JavaTest {
    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();
        date.setTime(1502249906811L);

        simpleDateFormat.format(date);

        System.out.println(simpleDateFormat.format(date));

    }
}
