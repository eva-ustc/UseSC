package ustc.sse.dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import ustc.sse.dao.Configuration;
import ustc.sse.dao.Conversation;
import ustc.sse.domain.User;
import ustc.sse.proxy.UserProxy;
import utils.DBCPUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/11/29 0:17
 * @description God Bless, No Bug!
 *      对象操作映射为数据表操作
 */
public class ConversationTemplete implements Conversation {

    private Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private Connection conn= null;
    private QueryRunner queryRunner = new QueryRunner();
    private String table_name = null;
    private String table_pk = null;
    private String table_username = null;
    private String table_password = null;
    // TODO 对象操作转为表操作

    private void initTable(){
        if (table_name == null){
            table_name = configuration.getTableName();
        }
        if (table_pk == null){
            table_pk = configuration.getTablePK();
        }
        if (table_username == null){
            table_username = configuration.getTableColumn("userName");
        }
        if (table_password == null){
            table_password = configuration.getTableColumn("userPass");
        }
    }
    /**
     * 根据Id查询用户
     * @param id user_id
     * @return user
     */
    @Override
    public User getUserById(Integer id) {
        initTable();
        String sql = MessageFormat.format("select * from {0} where {1}={2}", table_name, table_pk, id);
        try {
            conn = DBCPUtils.getConnection();
            System.out.println(sql);
            return queryRunner.query(conn,sql,new UserHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户名查询用户
     * @param name
     * @return
     */
    @Override
    public User getUserByName(String name) {
        initTable();
        String sql = MessageFormat.format("select * from {0} where {1}=''{2}''", table_name, table_username, name);
        try {
            conn = DBCPUtils.getConnection();
            System.out.println(sql);
            return queryRunner.query(conn,sql,new UserHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新添加用户
     * @param user
     * @return
     * @throws SQLException
     */
    @Override
    public boolean insertUser(User user) throws SQLException {
        initTable();
        String sql = MessageFormat.format("insert into {0}(username,password) values(''{1}'',''{2}'')",
                table_name,user.getUserName(),user.getUserPass());
        System.out.println(sql);
        conn = DBCPUtils.getConnection();
        if (queryRunner.update(conn, sql)!=0){

            return true;
        }

        return false;
    }

    /**
     * 根据Id删除用户
     * @param id
     * @return
     */
    @Override
    public boolean deleteUserById(Integer id) {
        initTable();
        String sql = MessageFormat.format("delete from {0} where {1}={2}",table_name, table_pk, id);
        try {
            conn = DBCPUtils.getConnection();
            System.out.println(sql);
            queryRunner.update(conn,sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据用户名删除用户
     * @param userName
     * @return
     */
    @Override
    public Boolean deleteUserByName(String userName) {
        initTable();
        String sql = MessageFormat.format("delete from {0} where {1}=''{2}''",table_name, table_username, userName);
        try {
            conn = DBCPUtils.getConnection();
            System.out.println(sql);
            queryRunner.update(conn,sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取所有用户信息
     * @return
     */
    @Override
    public List<User> getUsers() {
        initTable();
        String sql = MessageFormat.format("select * from {0}",table_name);
        try {
            conn = DBCPUtils.getConnection();
            return queryRunner.query(conn,sql,new UserListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Id查询属性值,懒加载
     * @param userId
     * @param attrName
     * @return
     */
    @Override
    public String getAttrById(Integer userId, String attrName) {
        initTable();
        String sql = MessageFormat.format("select {0} from {1} where user_id={2}",
                configuration.getTableColumn(attrName),table_name,userId);
        try {
            conn = DBCPUtils.getConnection();
            return queryRunner.query(conn, sql, new ResultSetHandler<String>() {
                @Override
                public String handle(ResultSet rs) throws SQLException {
                    if (rs.next()){
                       return rs.getString(1);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 懒加载,返回User的仅包含user_id和非懒加载属性的代理对象
     * @param id
     * @return
     */
    @Override
    public User loadUserById(Integer id) {
        initTable();
        StringBuilder sql = new StringBuilder("select user_id");
        if (!configuration.isLazyLoad("userName")){
            sql.append(","+table_username);
        }
        if (!configuration.isLazyLoad("userPass")){
            sql.append("," + table_password);
        }
        sql.append(" from "+table_name +" where user_id=" +id);
        try {
            /*return queryRunner.query(conn, sql.toString(), new ResultSetHandler<User>() {
                @Override
                public User handle(ResultSet rs) throws SQLException {
                    if (rs.next()){

                        UserProxy proxy = new UserProxy();
                        User user = new User();
                        User userProxy = (User) proxy.getProxy(user);
                        userProxy.setUserId(rs.getInt("user_id"));
                        if (!configuration.isLazyLoad("userName")){
                            userProxy.setUserName(rs.getString("userName"));
                        }
                        if (!configuration.isLazyLoad("userPass")){
                            userProxy.setUserPass(rs.getString("userPass"));
                        }
                        return userProxy;
                    }
                    return null;
                }
            });*/
            conn = DBCPUtils.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                boolean isUserNameLazy = configuration.isLazyLoad("userName");
                boolean isPasswordLazy = configuration.isLazyLoad("userPass");
                UserProxy proxy = new UserProxy();

                User userProxy = proxy.getProxy(User.class);
                userProxy.setUserId(rs.getInt("user_id"));
                if (!isUserNameLazy){
                    userProxy.setUserName(rs.getString("username"));
                }
                if (!isPasswordLazy){
                    userProxy.setUserPass(rs.getString("password"));
                }
                return userProxy;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户
     * 目前只提供根据用户名修改密码(验证环节待实现)
     */
    @Override
    public boolean updateUser(User user) {
        initTable();
        String sql = MessageFormat.format("update {0} set {1}=''{2}'' where {3}=''{4}''",
                table_name,table_password,user.getUserPass(),table_username,user.getUserName());
        try {
            conn = DBCPUtils.getConnection();
            System.out.println(sql);
            queryRunner.update(conn,sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * UserHandler 处理QueryRunner查询结果跟实体类的映射
     */
    class UserHandler implements ResultSetHandler<User>{
        @Override
        public User handle(ResultSet rs) throws SQLException {
            User user= null;
            if (rs.next()){
                user = new User();
                user.setUserId(rs.getInt(configuration.getTablePK()));
                user.setUserName(rs.getString(configuration.getTableColumn("userName")));
                user.setUserPass(rs.getString(configuration.getTableColumn("userPass")));
            }
            return user;
        }
    }

    class UserListHandler implements ResultSetHandler<List<User>>{

        @Override
        public List<User> handle(ResultSet rs) throws SQLException {
            List<User> users = new ArrayList<>();
            while (rs.next()){
                User user = new User();
                user.setUserId(rs.getInt(configuration.getTablePK()));
                user.setUserName(rs.getString(configuration.getTableColumn("userName")));
                user.setUserPass(rs.getString(configuration.getTableColumn("userPass")));
                users.add(user);
            }
            return users;
        }
    }
}
