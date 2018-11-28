package utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name utils
 * @date 2018/11/26 23:39
 * @description God Bless, No Bug!
 */
public class XmlUtils {
    public static Properties config_prop;
    public static SimpleDateFormat date_format;
    static {
        config_prop = new Properties();
        try {
            config_prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("/config.properties"));
            date_format = new SimpleDateFormat(config_prop.getProperty("date_format"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将XML文件转换成HTML文件字节数组输出流
     * @param xsl_path xsl文件路径 (在资源文件目录下)
     * @param xml_path xml文件路径 (在资源文件目录下)
     * @return HTML字节数组输出流
     */
    public static ByteArrayOutputStream ConvertXml2Html(String xsl_path,String xml_path){
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        StreamSource source_xsl = new StreamSource(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(xsl_path));
        StreamSource source_xml = new StreamSource(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(xml_path));
        try {
            transformer = factory.newTransformer(source_xsl);
            StreamResult output = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            output = new StreamResult(baos);

            transformer.transform(source_xml,output);
            String str = baos.toString();
            System.out.println(str);
            return baos;
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取action节点的Attribute属性
     * @param xml_file
     * @param attr
     * @return
     */
    public static List<String> getActionAttributes(File xml_file, String attr){
        List<String> actionNames = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            // 获取xml文档
            Document document = reader.read(xml_file);
            // 获取根元素 sc-configuration
            Element root = document.getRootElement();
            // 获取Controller
            Element controller = root.element("controller");
            List<Element> action_list = controller.elements("action");
            for(Element action :action_list){
                actionNames.add(action.attribute(attr).getText());
            }
//            System.out.println(actionNames);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return  actionNames;
    }

    /**
     * 获取所有的attribute属性
     * @param xml_file
     * @param attrName
     * @return
     */
    public static List<String> getAllAttributes(File xml_file,String attrName){
        List<String> attributes = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            // 获取xml文档
            Document document = reader.read(xml_file);
            // 获取根元素 sc-configuration
            Element root = document.getRootElement();

            List<Node> action_list = root.selectNodes("//action");
            for(Node action :action_list){
                attributes.add(((Element)action).attribute(attrName).getText());
            }
            System.out.println(attributes);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return  attributes;
    }

    /**
     * 获取attr_name=attr_value的element_name元素节点
     * @param xml_file
     * @param element_name
     * @param attr_name
     * @param attr_value
     * @return
     */
    public static Element getElementByAttr(File xml_file, String element_name,String attr_name, String attr_value){
        SAXReader reader = new SAXReader();
        Element element = null;
        try {
            Document document = reader.read(xml_file);
            Element root = document.getRootElement();
            // //name[attr='attrName']
            String sel_str = MessageFormat.format("//{0}[@{1}=''{2}'']", element_name, attr_name, attr_value);
//            System.out.println(sel_str);
            element = (Element) root.selectSingleNode(sel_str);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return element;
    }

    /**
     * 获取节点的属性值
     * @param element
     * @param attrName
     * @return
     */
    public static String getAttrValueByName(Element element, String attrName){
        if (element!=null){

            return element.attribute(attrName).getText();
        }else {
            throw new RuntimeException("传入的元素节点为null!");
        }
    }
   /* public static void handlerResult(String method, HttpServletRequest request, HttpServletResponse response, String type, String value) {
        try {
            request.setAttribute("type",type+":"+ method);
            if ("forward".equals(type)) { // 转发到指定页面
                request.getRequestDispatcher(value).forward(request, response);
            } else if ("redirect".equals(type)) { // 重定向到指定页面
                response.sendRedirect(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
