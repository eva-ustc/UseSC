package ustx.sse.action;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustx.sse.action
 * @date 2018/11/26 0:03
 * @description God Bless, No Bug!
 */
public class LoginAction {
    /**
     *
     * @return success/failure  转发到/pages/welcome.jsp /pages/failure.jsp
     */
    public String login(){

        return "success"; // 转发到/pages/welcome.jsp
    }
}
