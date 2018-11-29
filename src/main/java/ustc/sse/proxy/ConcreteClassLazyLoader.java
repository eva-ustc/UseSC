package ustc.sse.proxy;

import net.sf.cglib.proxy.LazyLoader;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.proxy
 * @date 2018/11/30 0:55
 * @description God Bless, No Bug!
 *      不知道有什么用的一个类
 */
public class ConcreteClassLazyLoader<CreateStat> implements LazyLoader {
    private CreateStat p;
    public ConcreteClassLazyLoader(CreateStat P){
        this.p = p;
    }
    @Override
    public Object loadObject() throws Exception {
        return p;
    }
}
