package ustc.sse.action;

import org.dom4j.Element;
import ustc.sse.domain.User;
import ustc.sse.service.UserService;
import ustc.sse.service.impl.UserServiceImpl;
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

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String handleLogin(String action_name, User user, HttpServletRequest request){
        System.out.println("handleLogin...执行了");
        /*User user = new User();
        user.setUserName("eva");
        user.setUserPass("123");*/
        if (userService.signIn(user)){
            return "success";
        }else {
            return "failure";
        }
    }


}
