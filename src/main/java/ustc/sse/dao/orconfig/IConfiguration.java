package ustc.sse.dao.orconfig;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/12/5 22:49
 * @description God Bless, No Bug!
 */
public interface IConfiguration {

    String getTablePK();
    String getTableColumn(String entity_attr);
    String getTableName();
    Boolean isLazyLoad(String entity_attr);
}
