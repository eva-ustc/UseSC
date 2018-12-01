package ustc.sse.config;

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
