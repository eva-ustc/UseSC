package ustc.sse.ioc;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.ioc
 * @date 2018/11/30 20:42
 * @description God Bless, No Bug!
 *  配置管理类
 */
public class ConfigManager {

    private static Map<String,Bean> map = new HashMap<>();

    /**
     * 读取配置文件并返回读取结果
     * 返回Map集合方便注入,key是每个bean的id
     * @param path 资源文件下的路径
     * @return
     */
    public static Map<String,Bean> getConfig(String path){

        // 解析xml配置文件
        SAXReader reader = new SAXReader();
        InputStream config_is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        Document document = null;
        try {
            document = reader.read(config_is);
            Element root = document.getRootElement();
            List<Element> beanList = root.elements("bean");
            if (beanList != null) {
                for (Element bean_element : beanList){
                    Bean bean = new Bean();
                    String id = bean_element.attributeValue("id");
                    String className = bean_element.attributeValue("class");
                    String singleton = bean_element.attributeValue("singleton")==null
                            ?"true":bean_element.attributeValue("singleton");
                    bean.setId(id);
                    bean.setClassName(className);
                    bean.setSingleton(singleton);

                    List<Element> properties_element = bean_element.elements("property");
                    if (properties_element != null) {
                        List<Property> propertyList = new ArrayList<>();
                        for (Element property_element : properties_element){
                            Property property = new Property();
                            String pName = property_element.attributeValue("name");
                            String pValue = property_element.attributeValue("value");
                            String pRef = property_element.attributeValue("bean-ref");
                            property.setName(pName);
                            property.setValue(pValue);
                            property.setBean_ref(pRef);

                            propertyList.add(property);
                        }
                        bean.setProperties(propertyList);
                    }
                    map.put(id,bean);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return  map;
    }
}
