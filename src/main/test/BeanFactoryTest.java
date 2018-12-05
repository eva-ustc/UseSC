import org.junit.jupiter.api.Test;
import ustc.sse.action.UserAction;
import ustc.sse.dao.UserDao;
import ustc.sse.dao.orconfig.Configuration2;
import ustc.sse.dao.orconfig.MapperClass;
import ustc.sse.ioc.ApplicationContext;
import ustc.sse.service.UserService;
import utils.XmlUtils;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name PACKAGE_NAME
 * @date 2018/11/30 22:20
 * @description God Bless, No Bug!
 */
public class BeanFactoryTest {

    @Test
    public void testBean(){ // 测试手写的简单版IOC容器
        ApplicationContext context = new ApplicationContext("/applicationContext.xml");
        Object userDao = context.getBean("userDao");
        Object userDao2 = context.getBean("userDao");
        System.out.println(userDao);
        System.out.println(userDao2);
        System.out.println("userDao==userDao2  "+(userDao==userDao2));
        UserAction action = (UserAction) context.getBean("userAction");
        UserAction action2 = (UserAction) context.getBean("userAction");
        System.out.println(action);
        System.out.println(action2);
        System.out.println("action==action2  "+(action==action2));
    }
    @Test
    public void testSubString(){
        String str = "ustc.sse.Action";
        System.out.println(str.substring(str.lastIndexOf(".")+1));
    }
    @Test
    public void testOrMapping(){
        MapperClass mapperClass = Configuration2.getMapperClass();
        System.out.println(mapperClass);
    }
}
