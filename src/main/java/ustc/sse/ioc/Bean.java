package ustc.sse.ioc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.ioc
 * @date 2018/11/30 20:32
 * @description God Bless, No Bug!
 */
@Setter@Getter
public class Bean {
    private String id;
    private String className;
    private String singleton;
    List<Property> properties = new ArrayList<>();
}
