package ustc.sse.interceptor;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.interceptor
 * @date 2018/11/27 20:39
 * @description God Bless, No Bug!
 *      登陆拦截器,记录日志
 */
public class LogInterceptor implements Interface4Interceptor{
    static  int i=0;
    @Override
    public void preAction(){
        System.out.println("preAction..."+i++);
    }
    @Override
    public void afterAction(){
        System.out.println("afterAction..."+i++);
    }
}
