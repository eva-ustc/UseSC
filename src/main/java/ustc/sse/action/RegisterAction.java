package ustc.sse.action;

import ustc.sse.domain.User;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.action
 * @date 2018/11/26 0:03
 * @description God Bless, No Bug!
 */
public class RegisterAction {

    public String handleRegister(String action_name,User user){
        System.out.println("handleRegister...执行了!");
        return "success";
    }
}
