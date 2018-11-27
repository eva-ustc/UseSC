package ustc.sse.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import utils.XmlUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.proxy
 * @date 2018/11/27 22:29
 * @description God Bless, No Bug!
 */
public class ActionProxy implements MethodInterceptor {

    private Object target; // 业务对象,供代理方法中进行真正的业务方法调用
    private Object result_str;
    private Document document;
    private File log_xml;
    private Element root;


    // 动态绑定
    public Object getProxy(Object target){
        this.target = target; // 给业务对象赋值
        // 创建加强器
        Enhancer enhancer = new Enhancer();
        // 为加强器指定要代理的业务类(为下面生成的代理类指定父类)
        enhancer.setSuperclass(this.target.getClass());
        // 设置回调,对于代理类的素有方法的调用,都会调用CallBack
        // CallBack就是重写的intercept方法
        enhancer.setCallback(this);
        // 创建动态代理对象并返回
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try {

            log_xml = new File(XmlUtils.config_prop.getProperty("log_location"));
            if (!log_xml.exists()){ // 如果不存在该目录则创建并初始化log_xml 创建根节点<log>
                log_xml.createNewFile();
                document = DocumentHelper.createDocument();
                root = document.addElement("log");
            }else {
                SAXReader reader =new SAXReader();
                document = reader.read(log_xml);
                root = document.getRootElement();
            }

            // 预处理 记录日志
            System.out.println("preActionProxy...开始打印日志");
            // 添加一个action节点
            Element action = root.addElement("action");
            Map<String,String> map_log = new LinkedHashMap<>();
            // 添加action_name和s_time
            map_log.put("name",objects[0].toString());
            map_log.put("s-time",XmlUtils.date_format.format(new Date()));

            // 执行真正的业务方法
            result_str = methodProxy.invokeSuper(o,objects);
            Thread.sleep(3000);

            // 事后 记录日志
            System.out.println("afterActionProxy...");
            // 添加e_time和result
            map_log.put("e-time",XmlUtils.date_format.format(new Date()));
            map_log.put("result", result_str.toString());
            writeLogElement(action,map_log);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            writeLog(document,log_xml);
        }
        return result_str;
    }

    private void writeLogElement(Element action, Map<String,String> element_map) {
       for(String key:element_map.keySet()){

           Element result = action.addElement(key);
           result.setText(element_map.get(key));
       }
    }

    /**
     * 记录日志到log_xml文件
     * @param document
     * @param log_xml
     */
    private void writeLog(Document document, File log_xml) {
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        try {
            XMLWriter writer = new XMLWriter(new FileWriter(log_xml),outputFormat);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
