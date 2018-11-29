package ustc.sse.action;

import ustc.sse.domain.User;
import ustc.sse.service.UserService;
import ustc.sse.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.action
 * @date 2018/11/29 15:54
 * @description God Bless, No Bug!
 */
public class UserAction implements  IUserAction{

    private UserService userService = new UserServiceImpl();

    /**
     * 删除用户
     * @param action_name
     * @param user
     * @return
     */
    @Override
    public String deleteUser(String action_name, User user,HttpServletRequest request) {

        if (userService.deleteUser(user)) {
            return "success";
        }else {
            return "failure";
        }
    }

    /**
     * 更新用户
     * @param action_name
     * @param user
     * @return
     */
    @Override
    public String updateUser(String action_name, User user,HttpServletRequest request) {

        if (userService.updateUser(user)) {
            return "success";
        }else {
            return "failure";
        }
    }

    @Override
    public String getUsers(String action_name, User user, HttpServletRequest request) {
        List<User> users = userService.getUsers();
        request.setAttribute("users",users);
        return "success";
    }
}
