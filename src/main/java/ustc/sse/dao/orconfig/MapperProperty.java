package ustc.sse.dao.orconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.dao
 * @date 2018/12/5 22:55
 * @description God Bless, No Bug!
 */
@Setter@Getter
public class MapperProperty {
    private String propName;
    private String propColumn;
    private String type;
    private String isLazy;
}
