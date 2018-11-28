package ustc.sse.dao;

import ustc.sse.domain.User;

import java.sql.Connection;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/11/28 16:18
 * @description God Bless, No Bug!
 */
public interface UserDao {

    User query(String sql);
    Boolean insert(String sql);
    Boolean update(String sql);
    Boolean delete(String sql);
}
