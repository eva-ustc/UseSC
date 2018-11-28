package ustc.sse.service.impl;

import ustc.sse.dao.UserDao;
import ustc.sse.dao.impl.UserDaoImpl;
import ustc.sse.domain.User;
import ustc.sse.service.UserService;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.service
 * @date 2018/11/28 19:35
 * @description God Bless, No Bug!
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 登录验证
     * @param user
     * @return
     */
    @Override
    public boolean signIn(User user) {
        // 根据传入的user_userId查询数据库 如果存在并且密码与数据库密码一致返回true
        // 否则返回false
        String sql = "select * from tb_user where username = '"+ user.getUserName()+"'";
        User db_user = userDao.query(sql);
        if (db_user != null && db_user.getUserPass().equals(user.getUserPass())){
            return true;
        }
        return false;
    }
}
