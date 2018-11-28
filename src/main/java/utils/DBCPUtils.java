package utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name utils
 * @date 2018/11/28 19:06
 * @description God Bless, No Bug!
 */
public class DBCPUtils {
    public static Properties properties = new Properties();
    public static DataSource dataSource;
    // 加载DBCP配置
    static {
        try {
            // 加载配置数据源文件
//            InputStream is = new FileInputStream("C:\\Users\\LRK\\Desktop\\log_file\\dbcp.properties");
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/dbcp.properties");
                    properties.load(is);

//            properties.load(new FileInputStream("src/main/resources/dbcp.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }try{
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据源获取一个连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            // 可以设置关闭自动提交
//            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
