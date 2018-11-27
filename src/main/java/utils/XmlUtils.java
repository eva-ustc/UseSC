package utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name utils
 * @date 2018/11/26 23:39
 * @description God Bless, No Bug!
 */
public class XmlUtils {
/*
<sc-configuration>
    <controller>
        <action name="login" class="ustc.sse.action.LoginAction" method="handleLogin">
            <result name="success" type="forward" value="pages/welcome.jsp"/>
            <result name="failure" type="redirect" value="pages/failure.jsp"/>
        </action>
        <action name="register" class="ustc.sse.action.RegisterAction" method="handleRegister">
            <result name="success" type="forward" value="pages/welcome.jsp"/>
            <result name="failure" type="redirect" value="pages/failure.jsp"/>
        </action>
    </controller>
</sc-configuration>
 */

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
            System.out.println(sel_str);
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
}
