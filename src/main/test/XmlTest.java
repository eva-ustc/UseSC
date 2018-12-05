import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import ustc.sse.dao.impl.ConversationTemplete;
import ustc.sse.dao.orconfig.Configuration2;
import ustc.sse.domain.User;
import ustc.sse.ioc.ApplicationContext;
import ustc.sse.proxy.UserProxy;
import utils.DBCPUtils;
import utils.XmlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testParseTable(){
        Map<String,Map<String,String>> table;

            table = new HashMap<>();
            SAXReader reader = new SAXReader();
            try {
                Document document = reader.read(new FileInputStream("D:/or_mapping.xml"));
                Element root = document.getRootElement();// OR-Mappering
                Element class_element = (Element) root.selectSingleNode("class");
                for(Element child_ele :class_element.elements()){ // name table id property
                    String child_eleText = child_ele.getName();
                    Map<String,String> column_info = new HashMap<>();
                    switch (child_eleText){
                        case "property": // 读取name属性作为table的key
                            String property_key="";
                            for(Element property_element : child_ele.elements()){ //name column type lazy
                                // 读取property节点下所有属性值,并将name节点值作为table的key
                                String property_name = property_element.getName();
                                if ("name".equals(property_name)){
                                    property_key = property_element.getText();
                                }
                                column_info.put(property_name,property_element.getText());
                            }
                            table.put(property_key,column_info);
                            break;
                        case "id":// 使用tb_primarykey作为table的key
                            for(Element property_id : child_ele.elements()){
                                column_info.put(property_id.getName(),property_id.getText());
                            }
                            table.put("id",column_info);
                            break;
                        default: // 使用节点名作为table的key
                            column_info.put(child_eleText,child_ele.getText());
                            table.put(child_ele.getName(),column_info);
                            break;
                    }
                }
            System.out.println(table);

//            XmlUtils.writeXML(document,new File("C:\\Users\\LRK\\Desktop\\log_file\\mappering.xml"));
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    @Test
    public void testConversionTemplete(){
        ConversationTemplete templete = new ConversationTemplete();
        User user = templete.getUserById(1);
        System.out.println(user);
    }
    @Test
    public void testUserProxy(){

        UserProxy userProxy = new UserProxy();
        User user = new User();
        user.setUserName(null);
        Object user_proxy =  userProxy.getProxy(User.class);
        System.out.println(((User)user_proxy).getUserName());
    }

    @Test
    public void testgetAttrById(){
        ConversationTemplete conversationTemplete = new ConversationTemplete();
        System.out.println(conversationTemplete.getAttrById(1,"userName"));
    }

    @Test
    public void testLoadUserById(){ // 懒加载测试
//        ApplicationContext context = new ApplicationContext("/applicationContext.xml");
//        ConversationTemplete conversationTemplete = (ConversationTemplete) context.getBean("conversationTemplete");
        ConversationTemplete conversationTemplete = new ConversationTemplete();
        Configuration2 configuration2 = new Configuration2();
        conversationTemplete.setConfiguration(configuration2);
        User user = conversationTemplete.loadUserById(4);

        System.out.println("=============");
        System.out.println(user.getUserPass());
        System.out.println(user);
    }
}
