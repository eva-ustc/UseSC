package ustc.sse.interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.interceptor
 * @date 2018/12/5 22:09
 * @description God Bless, No Bug!
 */
public class InterceptorContext {

    private Map<String,Interceptor> map_config;
    private Map<String,Object> context = new HashMap<>();

    public InterceptorContext(String path){
        map_config = InterceptorConfigManager.getInterceptorConfig(path);
        for (Map.Entry<String,Interceptor> entry : map_config.entrySet()){
            String interceptor_name = entry.getKey();
            Interceptor interceptor = entry.getValue();
            Object existInterceptor = context.get(interceptor_name);
            if (existInterceptor == null){
                try {
                    Class clazz = Class.forName(interceptor.getClassName());
                    existInterceptor = clazz.newInstance();
                    context.put(interceptor_name,existInterceptor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取拦截器的实例,拦截器均是单例
     * @param name
     * @return
     */
    public Object getInterceptor(String name){
        return context.get(name);
    }
}
