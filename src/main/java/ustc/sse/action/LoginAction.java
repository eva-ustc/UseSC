package ustc.sse.action;

import org.dom4j.Element;
import utils.XmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.action
 * @date 2018/11/26 0:03
 * @description God Bless, No Bug!
 */
public class LoginAction {
    public String handleLogin(String action_name){
        System.out.println("handleLogin...执行了");
        return "success";
    }


}
