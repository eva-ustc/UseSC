package ustc.sse.interceptor;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.interceptor
 * @date 2018/12/5 21:44
 * @description God Bless, No Bug!
 */
@Setter@Getter
public class Interceptor {
    private String name;
    private String className;
    private String preDo;
    private String afterDo;
}
