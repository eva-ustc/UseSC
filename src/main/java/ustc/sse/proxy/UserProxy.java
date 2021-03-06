package ustc.sse.proxy;

import net.sf.cglib.proxy.*;
import ustc.sse.dao.impl.ConversationTemplete;
import ustc.sse.dao.orconfig.IConfiguration;
import ustc.sse.domain.User;
import ustc.sse.ioc.ApplicationContext;
import utils.CommonUtils;

import java.lang.reflect.Method;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.proxy
 * @date 2018/11/29 20:19
 * @description God Bless, No Bug!
 *      User动态代理类,实现懒加载
 */
public class UserProxy implements MethodInterceptor {
    ApplicationContext context = new ApplicationContext("/applicationContext.xml");
    private ConversationTemplete conversationTemplete = (ConversationTemplete) context.getBean("conversationTemplete");

    public void setConversationTemplete(ConversationTemplete conversationTemplete) {
        this.conversationTemplete = conversationTemplete;
    }

    private IConfiguration configuration = (IConfiguration) context.getBean("configuration");

    public void setConfiguration(IConfiguration configuration) {
        this.configuration = configuration;
    }

    private Class target;

    public <P> P getProxy(Class<P> target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        try {
            enhancer.setSuperclass(this.target);
            enhancer.setCallback(this);
            return (P)enhancer.create();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//        System.out.println("method,getName: "+method.getName()); //getUserName
//        System.out.println("methodProxy.getSuperName: " + methodProxy.getSuperName()); //CGLIB$getUserName$1
        Object obj = null;
        String methodName = method.getName();
        if (methodName.equals("getUserId")) { // 如果是getUserId 直接返回id
            return methodProxy.invokeSuper(o,objects);
        } else if (methodName.startsWith("get")) { // 如果是getXXX属性,读取configuration配置文件,并查看属性是否是懒加载
            User user = (User) o;
            String attrNameUpperFirstOne = methodName.substring(3);
            String attrNameLowwerFirstOne = CommonUtils.toLowerCaseFirstOne(attrNameUpperFirstOne);

            if (!configuration.isLazyLoad(attrNameLowwerFirstOne)) { //如果不是懒加载则直接返回

                return methodProxy.invokeSuper(user, objects);
            } else if (configuration.isLazyLoad(attrNameLowwerFirstOne)){ // 如果是懒加载则在这里再去加载该属性值
                // 构造set方法
                String setName = "set" + attrNameUpperFirstOne;
                System.out.println(setName);

                // 从数据库查出对应数据并执行set方法赋值
                String attrValue = conversationTemplete.getAttrById(user.getUserId(),attrNameLowwerFirstOne);

                Method setMethod = user.getClass().getDeclaredMethod(setName, String.class);
                setMethod.invoke(user, attrValue);
            }

            obj = methodProxy.invokeSuper(user, objects);
        } else { // 非get方法则直接执行

            obj = methodProxy.invokeSuper(o, objects);
        }

        return obj;
//        Object result =methodProxy.invokeSuper(user,objects);
    }
}
