package ustc.sse.action;

import ustc.sse.domain.User;
import ustc.sse.service.UserService;
import ustc.sse.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.action
 * @date 2018/11/26 0:03
 * @description God Bless, No Bug!
 */
public class RegisterAction {
    UserService userService = new UserServiceImpl();

    public String handleRegister(String action_name, User user,HttpServletRequest request) {

        System.out.println("handleRegister...执行了!");

            if (userService.register(user)){
                return "success";
            }

        return "failure";
    }
}
