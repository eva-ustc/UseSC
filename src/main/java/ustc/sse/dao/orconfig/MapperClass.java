package ustc.sse.dao.orconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/12/5 22:52
 * @description God Bless, No Bug!
 */
@Setter@Getter@ToString
public class MapperClass {
    private String entityName;
    private String tableName;
    private String idName;
    private String idColumn;
    private List<MapperProperty> propertyList;
}
