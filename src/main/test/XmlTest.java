import org.junit.jupiter.api.Test;
import utils.DBCPUtils;
import utils.XmlUtils;

import java.io.File;
import java.sql.*;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name PACKAGE_NAME
 * @date 2018/11/27 0:09
 * @description God Bless, No Bug!
 */
public class XmlTest {
    @Test
    public void test(){
        File xml_file = new File("C:\\Users\\LRK\\Desktop\\controller.xml");
        List<String> actionName = XmlUtils.getAllAttributes(xml_file,"name");
    }
    @Test
    public void testDBCP(){
        Connection connection = DBCPUtils.getConnection();
        System.out.println(connection);
    }
    @Test
    public void testpostgres(){
        Connection connection=null;
        PreparedStatement statement =null;
        try{
            String url="jdbc:postgresql://192.168.56.101:5432/db_sc";
            String user="postgres";
            String password = "123456";
            Class.forName("org.postgresql.Driver");
            connection= DriverManager.getConnection(url, user, password);
            System.out.println("是否成功连接pg数据库"+connection);
            String sql="select * from tb_user";
            statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();
            while(resultSet.next()){
                String name=resultSet.getString(1);
                System.out.println(name);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            try{
                statement.close();
            }
            catch(SQLException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally{
                try{
                    connection.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
