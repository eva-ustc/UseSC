package utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name utils
 * @date 2018/11/30 23:45
 * @description God Bless, No Bug!
 */
public class CommonUtils {

    /**
     * 首字母转小写
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
