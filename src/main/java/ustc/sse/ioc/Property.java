package ustc.sse.ioc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.ioc
 * @date 2018/11/30 20:34
 * @description God Bless, No Bug!
 */
@Setter@Getter@ToString
public class Property {

    private String name;
    private String value;
    private String bean_ref;
}
