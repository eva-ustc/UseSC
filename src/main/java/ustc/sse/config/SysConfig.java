package ustc.sse.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.util.Properties;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.config
 * @date 2018/12/1 13:58
 * @description God Bless, No Bug!
 */
public class SysConfig {
    private static Properties sys_config = null;
    private static Document xml_doc = null;
    private static Element root_element = null;

    /**
     * 获取config.properties配置文件信息
     * @return sys_config
     */
    public static Properties getSysConfig(){
        try {
            if (sys_config == null){
                sys_config = new Properties();
                sys_config.load(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("/config.properties"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  sys_config;
    }


}
