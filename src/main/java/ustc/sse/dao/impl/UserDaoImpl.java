package ustc.sse.dao.impl;

import ustc.sse.dao.UserDao;
import ustc.sse.domain.User;
import utils.DBCPUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao.impl
 * @date 2018/11/28 19:41
 * @description God Bless, No Bug!
 */
public class UserDaoImpl implements UserDao {

    private ConversationTemplete conversationTemplete;

    public void setConversationTemplete(ConversationTemplete conversationTemplete) {
        this.conversationTemplete = conversationTemplete;
    }

    @Override
    public User query(String sql) {
        Connection connection=null;
        PreparedStatement stmt=null;
        ResultSet resultSet = null;
        try {
            connection = DBCPUtils.getConnection();
            stmt = connection.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setUserName(resultSet.getString("username"));
                user.setUserPass(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public User getUserById(Integer id) {
        return conversationTemplete.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return conversationTemplete.getUserByName(name);
    }

    @Override
    public Boolean insertUser(User user) throws SQLException {
        return conversationTemplete.insertUser(user);
    }

    @Override
    public Boolean updateUser(User user) {
        return conversationTemplete.updateUser(user);
    }

    @Override
    public Boolean deleteUserByName(String userName) {
        return conversationTemplete.deleteUserByName(userName);
    }

    @Override
    public Boolean deleteUserById(Integer id) {
        return conversationTemplete.deleteUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return conversationTemplete.getUsers();
    }


}
