package ustc.sse.service;

import ustc.sse.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.service
 * @date 2018/11/28 19:35
 * @description God Bless, No Bug!
 *      User服务层接口
 */
public interface UserService {

    Boolean signIn(User user);
    Boolean register(User user);
    Boolean deleteUser(User user);
    Boolean updateUser(User user);

    List<User> getUsers();
}
