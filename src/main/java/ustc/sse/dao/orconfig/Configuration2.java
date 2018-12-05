package ustc.sse.dao.orconfig;

import org.dom4j.Element;
import ustc.sse.config.SysConfig;
import utils.SCConstant;
import utils.XmlUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/12/5 22:51
 * @description God Bless, No Bug!
 *      使用对象封装or_mapping配置文件
 */
public class Configuration2 implements IConfiguration{
    private static MapperClass mapperClass;

    public static MapperClass getMapperClass() {
        if (mapperClass == null){
            mapperClass = new MapperClass();

            // 读取配置文件or_mapping.xml
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(SysConfig.getSysConfig().getProperty(SCConstant.OR_MAPPING_LOCATION));
            /*InputStream is = null; // 测试
            try {
                is = new FileInputStream("C:\\Users\\LRK\\Desktop\\log_file\\or_mapping.xml");
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }*/
            Element classEle = XmlUtils.getElementByName(is, "class");
            if (classEle !=null){
                // 将映射文件转为对象
                mapperClass.setEntityName(classEle.elementText("name"));
                mapperClass.setTableName(classEle.elementText("table"));
                mapperClass.setIdName(classEle.element("id").elementText("name"));
                mapperClass.setIdColumn(classEle.element("id").elementText("column"));
                List<Element> propertyList = classEle.elements("property");
                List<MapperProperty> mapperProperties = null;
                if (propertyList != null){

                    mapperProperties = new ArrayList<>();//属性列表对象
                    for(Element property_ele : propertyList){
                        MapperProperty mapperProperty = new MapperProperty();// 属性对象
                        mapperProperty.setPropName(property_ele.elementText("name"));
                        mapperProperty.setPropColumn(property_ele.elementText("column"));
                        mapperProperty.setType(property_ele.elementText("type"));
                        mapperProperty.setIsLazy(property_ele.elementText("lazy"));
                        mapperProperties.add(mapperProperty);
                    }
                }
                mapperClass.setPropertyList(mapperProperties);
            }
        }
        return mapperClass;
    }

    @Override
    public String getTablePK() {
        return getMapperClass().getIdColumn();
    }

    /**
     * 获取实体属性对应的表中字段名
     * @param entity_attr
     * @return
     */
    @Override
    public String getTableColumn(String entity_attr) {
        for (MapperProperty property : getMapperClass().getPropertyList()){
            if (property.getPropName().equals(entity_attr)){
                return property.getPropColumn();
            }
        }
        return null;
    }

    @Override
    public String getTableName() {
        return getMapperClass().getTableName();
    }

    @Override
    public Boolean isLazyLoad(String entity_attr) {
        for (MapperProperty property : getMapperClass().getPropertyList()){
            if (property.getPropName().equals(entity_attr)){
                switch (property.getIsLazy()){
                    case "true":
                        return true;
                    case "false":
                        return false;
                        default:
                            return false;
                }
            }
        }
        return false;
    }
}
