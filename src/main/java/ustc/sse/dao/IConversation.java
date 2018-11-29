package ustc.sse.dao;

import ustc.sse.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/11/29 0:20
 * @description God Bless, No Bug!
 */
public interface IConversation {

    User getUserById(Integer id);
    User getUserByName(String name);
    boolean insertUser(User user) throws SQLException;
    boolean deleteUserById(Integer id);
    boolean updateUser(User user);
    Boolean deleteUserByName(String userName);
    List<User> getUsers();
    String getAttrById(Integer userId, String attrName);
    User loadUserById(Integer id);
}
