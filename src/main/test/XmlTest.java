import org.junit.jupiter.api.Test;
import utils.DBCPUtils;
import utils.XmlUtils;

import java.io.File;
import java.sql.Connection;
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
}
