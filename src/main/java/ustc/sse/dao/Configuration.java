package ustc.sse.dao;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ustc.sse.config.SysConfig;
import utils.SCConstant;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/11/29 0:17
 * @description God Bless, No Bug!
 *      解析or_mapper.xml映射文件
 */
public class Configuration {
    private static Map<String,Map<String,String>> table = null;

    public static Map<String,Map<String,String>> getTable(){
        if (table == null){
// 初始化table 读取or_mapping
            table = new HashMap<>();
            SAXReader reader = new SAXReader();
            try {
//            Document document = reader.read(Thread.currentThread()
//            .getContextClassLoader().getResourceAsStream("/or_mapping.xml")); // 运行环境映射文件
                Document document = reader.read(Thread.currentThread()
                        .getContextClassLoader().getResourceAsStream(SysConfig.getSysConfig()
                                .getProperty(SCConstant.OR_MAPPING_LOCATION)));
//            Document document = reader.read(new FileInputStream("D:/or_mapping.xml")); //测试环境映射文件
                Element root = document.getRootElement();// OR-Mappering
                Element class_element = (Element) root.selectSingleNode("class");
                for(Element child_ele :class_element.elements()){ // name table id property
                    String child_eleText = child_ele.getName();
                    Map<String,String> column_info = new HashMap<>();
                    switch (child_eleText){
                        case "property": // 读取name属性作为table的key
                            String property_key="";
                            for(Element property_element : child_ele.elements()){ //name column type lazy
                                // 读取property节点下所有属性值,并将name节点值作为table的key
                                String property_name = property_element.getName();
                                if ("name".equals(property_name)){
                                    property_key = property_element.getText();
                                }
                                column_info.put(property_name,property_element.getText());
                            }
                            table.put(property_key,column_info);
                            break;
                        case "id":// 使用tb_primarykey作为table的key
                            for(Element property_id : child_ele.elements()){
                                column_info.put(property_id.getName(),property_id.getText());
                            }
                            table.put("id",column_info);
                            break;
                        default: // 使用节点名作为table的key
                            column_info.put(child_eleText,child_ele.getText());
                            table.put(child_ele.getName(),column_info);
                            break;
                    }
                }
//            System.out.println(table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return table;
    }
    /**
     * 获取表的主键名
     * @return
     */
    public  String getTablePK(){
        return getTable().get("id").get("column");
    }

    /**
     * 根据实体类属性名获取对应的表字段名
     * @param entity_attr
     * @return
     */
    public  String getTableColumn(String entity_attr){
        return getTable().get(entity_attr).get("column");
    }

    /**
     * 获取数据表的名称
     * @return
     */
    public String getTableName() {
        return getTable().get("table").get("table");
    }

    /**
     * 获取对应实体类的类名
     * @return
     */
    public String getEntityName(){
        return getTable().get("name").get("name");
    }

    /**
     * 判断属性是否懒加载
     * @param entity_attr
     * @return
     */
    public  Boolean isLazyLoad(String entity_attr){

        Map<String, String> map = getTable().get(entity_attr);
        if (map != null ) {
            switch (map.get("lazy")){
                case "true":
                    return true;
                case "false":
                    return false;
                    default:
                        return false;
            }
        }
        return false;
    }
}
