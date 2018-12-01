package ustc.sse.ioc;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.ioc
 * @date 2018/11/30 21:35
 * @description God Bless, No Bug!
 */
public interface BeanFactory {
    // 核心方法getBean
    Object getBean(String id);
}
