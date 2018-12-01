package ustc.sse.ioc;

import org.apache.commons.beanutils.BeanUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name utils
 * @date 2018/11/30 19:43
 * @description God Bless, No Bug!
 *      对象管理类,根据配置文件初始化容器
 */
public class ApplicationContext implements BeanFactory {

    // 获取读取的配置文件中的map信息
    private Map<String,Bean> map_config;
    // 作为IOC容器使用
    private Map<String,Object> context = new HashMap<>();
    private static int i = 0;
    public ApplicationContext(String path){
        System.out.println(i++);
        // 初始化IOC容器,创建单例对象并加入IOC容器
        // 获取配置信息
        map_config = ConfigManager.getConfig(path);
        for (Map.Entry<String,Bean> entry : map_config.entrySet()){
            String bean_id = entry.getKey();
            Bean bean = entry.getValue();

            Object existBean = context.get(bean_id);
            // 当容器中没有该对象并且该对象配置为单例时,创建该对象
            if (existBean == null && "true".equals(bean.getSingleton())){

                Object beanObj = createBean(bean);

                context.put(bean_id,beanObj);
            }
        }
        System.out.println(context);

    }

    private Object createBean(Bean bean) {
        Class clazz = null;
        try {
            clazz = Class.forName(bean.getClassName());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到该类"+bean.getClassName());
        }
        Object beanObj = null;
        try {
            beanObj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("没有提供无参构造器");
        }
        // 如果属性列不为空则为创建的对象注入属性
        if (bean.getProperties()!=null){
            for (Property property : bean.getProperties()){

                String name = property.getName();
                String value = property.getValue();
                String ref = property.getBean_ref();

                // 注入分两种: 值类型和引用类型
                if (value!=null){// 值类型
                    Map<String,String[]> paraMap = new HashMap<>();
                    paraMap.put(name,new String[]{value});
                    try {
                        // 使用BeanUtils完成属性注入,可以自动完成类型转换
                        BeanUtils.populate(beanObj,paraMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("请检查您的"+name+"属性");
                    }
                }
                if (ref != null){ // 引用类型
                    Object existBean = context.get(property.getBean_ref());
                    if (existBean == null){ // 如果当前容器不存在该属性对象则递归创建一个
                        existBean = createBean(map_config.get(property.getBean_ref()));
                        // 如果属性对象配置为单例,则装入容器
                        if (("true".equals(map_config.get(property.getBean_ref()).getSingleton()))){

                            context.put(property.getBean_ref(),existBean);
                        }
                    }
                    // 注入引用类型的属性
                    try {
                        BeanUtils.setProperty(beanObj,name,existBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("请检查您的bean的属性"+name+"是否有对应的setter方法");
                    }
                }
            }
        }
        return beanObj;
    }

    @Override
    public Object getBean(String id) {
        Object bean = context.get(id);
        if (bean == null){
            // 如果为空表示不是单例,新创建一个bean对象
            bean = createBean(map_config.get(id));
        }
        System.out.println(context);
        return bean;
    }
}
