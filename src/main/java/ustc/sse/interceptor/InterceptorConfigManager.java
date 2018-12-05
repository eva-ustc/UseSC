package ustc.sse.interceptor;

import org.dom4j.Element;
import utils.XmlUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.interceptor
 * @date 2018/12/5 21:44
 * @description God Bless, No Bug!
 */
public class InterceptorConfigManager {
    private static Map<String,Interceptor> map;

    public static Map<String,Interceptor> getInterceptorConfig(String path){
        if (map == null){
            map = new HashMap<>();
            // 解析controller.xml 获取所有interceptor节点
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(path);
            List<Element> interceptor_list = XmlUtils.getElementsByName(is, "interceptor");
            if (interceptor_list !=null){
                for(Element interceptor_ele : interceptor_list ){
                    Interceptor interceptor = new Interceptor();
                    String name = interceptor_ele.attributeValue("name");
                    interceptor.setName(name);
                    interceptor.setClassName(interceptor_ele.attributeValue("class"));
                    interceptor.setPreDo(interceptor_ele.attributeValue("predo"));
                    interceptor.setAfterDo(interceptor_ele.attributeValue("afterdo"));
                    map.put(name,interceptor);
                }
            }
        }
        return map;
    }
}
