package ustc.sse.action;

import ustc.sse.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.action
 * @date 2018/11/29 15:55
 * @description God Bless, No Bug!
 */
public interface IUserAction {
    String deleteUser(String action_name, User user,HttpServletRequest request);
    String updateUser(String action_name, User user,HttpServletRequest request);
    String getUsers(String action_name, User user, HttpServletRequest request);
}
