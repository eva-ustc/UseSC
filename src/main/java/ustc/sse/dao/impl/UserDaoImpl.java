package ustc.sse.dao.impl;

import ustc.sse.dao.UserDao;
import ustc.sse.domain.User;
import utils.DBCPUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao.impl
 * @date 2018/11/28 19:41
 * @description God Bless, No Bug!
 */
public class UserDaoImpl implements UserDao {


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
    public Boolean insert(String sql) {
        return null;
    }

    @Override
    public Boolean update(String sql) {
        return null;
    }

    @Override
    public Boolean delete(String sql) {
        return null;
    }
}
