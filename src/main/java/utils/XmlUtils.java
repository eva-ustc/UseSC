package utils;

import org.apache.xerces.dom.DocumentImpl;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
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
/*    private static Properties config_prop;
//    public static SimpleDateFormat date_format;
    static { // 读取config.preperties配置文件
        config_prop = new Properties();
        try {
            config_prop.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("/config.properties"));
//            date_format = new SimpleDateFormat(config_prop.getProperty("date_format"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    /**
     * 读取xml文档,返回Document对象
     * @return
     */
    public static Document getXmlDoc(String src_path){

        SAXReader reader = new SAXReader();
        Document xml_doc = null;
        try {
            xml_doc = reader.read(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(src_path));
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("xml文档路径可能出错~");
        }
        return xml_doc;
    }
    public static Document getXmlDoc(File src_file){

        SAXReader reader = new SAXReader();
        Document xml_doc = null;
        try {
            xml_doc = reader.read(src_file);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("xml文档路径可能出错~");
        }
        return xml_doc;
    }

    /**
     * 获取xml文档的根元素
     * @param src_path xml文档在资源文件下的路径
     * @return
     */
    public static Element getRootElement(String src_path){

        return getXmlDoc(src_path).getRootElement();
    }
    public static Element getRootElement(File src_file){

        return getXmlDoc(src_file).getRootElement();
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

        // 获取根元素 sc-configuration
        Element root = getRootElement(xml_file);
        // 获取Controller
        Element controller = root.element("controller");
        List<Element> action_list = controller.elements("action");
        for(Element action :action_list){
            actionNames.add(action.attribute(attr).getText());
        }
//            System.out.println(actionNames);


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

        // 获取根元素 sc-configuration
        Element root = getRootElement(xml_file);
        List<Node> action_list = root.selectNodes("//action");
        for(Node action :action_list){
            attributes.add(((Element)action).attribute(attrName).getText());
        }
//          System.out.println(attributes);

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
        Element root = getRootElement(xml_file);
        // //name[attr='attrName']
        String sel_str = MessageFormat.format("//{0}[@{1}=''{2}'']", element_name, attr_name, attr_value);
//            System.out.println(sel_str);
        return (Element) root.selectSingleNode(sel_str);
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
    /**
     * 记录日志到log_xml文件
     * @param document
     * @param file
     */
    public static void writeXML(Document document, File file) {
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        try {
            XMLWriter writer = new XMLWriter(new FileWriter(file),outputFormat);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定标签名的所有节点
     * @param is
     * @param eleName
     * @return
     */
    public static List<Element> getElementsByName(InputStream is, String eleName){
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        return  root.elements(eleName);
    }

    /**
     * 获取一个指定标签名的元素
     * @param is
     * @param eleName
     * @return
     */
    public static Element getElementByName(InputStream is, String eleName){
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        return  root.element(eleName);
    }

}
