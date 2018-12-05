package ustc.sse.controller;

import org.dom4j.Element;
import ustc.sse.config.SysConfig;
import ustc.sse.domain.User;
import ustc.sse.interceptor.InterceptorContext;
import ustc.sse.interceptor.Interface4Interceptor;
import ustc.sse.ioc.ApplicationContext;
import ustc.sse.ioc.Bean;
import ustc.sse.ioc.ConfigManager;
import ustc.sse.proxy.ActionProxy;
import ustc.sse.proxy.UserProxy;
import utils.SCConstant;
import utils.XmlUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.controller
 * @date 2018/11/26 22:37
 * @description God Bless, No Bug!
 */
public class SimpleControllerProxy extends HttpServlet {
    private static final String ERROR_PAGE = "pages/error.jsp";
    private static final String PRE_EXECUTION = "predo";
    private static final String AFTER_EXECUTION = "afterdo";

    private String action_name;
    private File controller_xml;
    private List<String> actionNames;
    private boolean hasAction;
    private boolean hasInterceptor;
    private String interceptor_name;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        initParams(req, resp); // 初始化各项数据
        for(String actionName : actionNames){ // 判断请求是否合法
            if (actionName.equals(action_name)){// 匹配成功 如:name=login,利用反射执行相应操作
                hasAction =true;

                // 执行拦截器方法
                //  获取当前action节点
                Element action_element = XmlUtils.getElementByAttr(controller_xml,"action","name",actionName);

                // 判断是否存在interceptor-ref节点
                List<Element> interceptor_ref_elements = action_element.elements("interceptor-ref");

                if (interceptor_ref_elements !=null){
                    hasInterceptor = true;
                    for(Element interceptor_ref_element : interceptor_ref_elements){

                        interceptor_name = interceptor_ref_element.attribute("name").getText();
                        doInterceptor(interceptor_name,PRE_EXECUTION);
                    }
                }

                //  反射执行相应操作

                // 获取类名和方法名 如 ustc.sse.action.LoginACtion 和 handleLogin
                String class_name = XmlUtils.getAttrValueByName(action_element,"class");
                String method_name = XmlUtils.getAttrValueByName(action_element,"method");
                try {

                    // 在applicationContext.xml中查找是否包含与请求的action同名的bean节点
                    // applicationContext.xml配置信息:bean列表
//                    Map<String, Bean> bean_config = ConfigManager.getConfig("/applicationContext.xml");
                    Map<String, Bean> bean_config = ConfigManager.getConfig(SysConfig.getSysConfig()
                            .getProperty(SCConstant.APPLICATION_CONTEXT_LOCATION));
                    String action_name = getSimpleName(class_name);
                    Object target = null;
                    Class clazz = null;
                    // 如果没有指定bean节点,则直接通过反射构造Action实例,并分发请求
                    if (!bean_config.containsKey(toLowerCaseFirstOne(action_name))) {
                        // 创建被代理对象和代理对象,调用代理对象的method
                        clazz = Class.forName(class_name);
                        target = clazz.newInstance();
                    }else {
                        // 如果找到了指定bean节点,查看该节点是否有属性依赖其他bean节点
                        // 如果无依赖,直接通过反射构造该bean实例,并风发请求
                        // 如果有属性依赖,反射先构造被依赖属性(如 userDao)的实例,之后再构造依赖(如 userService)
                        // 依赖注入 setter方法,并分发请求 在手写ioc包中实现
//                        ApplicationContext context = new ApplicationContext("/applicationContext.xml");
                        ApplicationContext context = new ApplicationContext(SysConfig.getSysConfig()
                        .getProperty(SCConstant.APPLICATION_CONTEXT_LOCATION));
                        target = context.getBean(toLowerCaseFirstOne(action_name));
                        clazz = target.getClass();
                    }

                    ActionProxy actionProxy = new ActionProxy();
//                    UserProxy actionProxy = new UserProxy();
                    Object proxy = actionProxy.getProxy(target);

                    User user = encapsulateParaUser(req);
                    // 将request传来的参数封装到user对象中

                    // 调用代理对象加强业务方法
                    Method method = clazz.getDeclaredMethod(method_name,String.class,User.class,HttpServletRequest.class);
                    String result = (String) method.invoke(proxy,action_name,user,req);
                    // 根据方法的返回值,查询次action下的result节点的name属性 跳转/重定向
                    handleResult(action_element,result,method_name,req,resp);

                    if (hasInterceptor){
                        for(Element interceptor_ref_element : interceptor_ref_elements){

                            interceptor_name = interceptor_ref_element.attribute("name").getText();
                            doInterceptor(interceptor_name,AFTER_EXECUTION);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (!hasAction) { // 没有匹配请求的方法
            resp.sendRedirect(ERROR_PAGE);
        }
    }

    private void doInterceptor(String interceptor_name, String execution_order) {
        System.out.println("doInterceptor "+interceptor_name+" "+execution_order);
        InterceptorContext interceptorContext = new InterceptorContext(SysConfig.getSysConfig()
        .getProperty(SCConstant.CONTROLLER_LOCATION));
        Object obj = interceptorContext.getInterceptor(interceptor_name);
        if (obj instanceof Interface4Interceptor){
            Interface4Interceptor interceptor = (Interface4Interceptor) obj;
            switch (execution_order){
                case PRE_EXECUTION:
                    interceptor.preAction();
                    break;
                case AFTER_EXECUTION:
                    interceptor.afterAction();
                    break;
                    default:System.out.println("没有匹配的方法...");

            }
        }
    }

    /**
     * 截取全限定类名的简短类名
     * @param class_name
     * @return
     */
    private String getSimpleName(String class_name) {

        return class_name.substring(class_name.lastIndexOf(".")+1);
    }

    /**
     * 将request的参数封装到user对象中
     * @param req
     * @return
     */
    private User encapsulateParaUser(HttpServletRequest req) {
        User user = new User();
        String id_str = (req.getParameter("id"));
        if (id_str != null) {
            user.setUserId(Integer.parseInt(id_str));
        }
        String username = req.getParameter("username");
        if (username != null) {
            user.setUserName(username);
        }
        String password = req.getParameter("password");
        if (password != null) {
            user.setUserPass(password);
        }
        return user;
    }


    /**
     * 处理拦截器的执行 action执行之前/之后
     * @param interceptor_name
     * @param interceptor_order
     */
 /*   private void doInterceptor(String interceptor_name, String interceptor_order) {
        Element interceptor = XmlUtils.getElementByAttr(controller_xml,"interceptor","name",interceptor_name);
        String class_name = interceptor.attribute("class").getText();
        String method_name = interceptor.attribute(interceptor_order).getText();
        doMethod(class_name,method_name);
    }*/

    /**
     * 初始化请求的数据 request_path action_name controller_xml actionNames
     * @param req
     * @param resp
     */
    private void initParams(HttpServletRequest req, HttpServletResponse resp) {
        resp.setCharacterEncoding("gbk"); // 设置response字节编码

        // 获取请求路径
        String request_path = req.getServletPath();
//        System.out.println(path);
        // 获取请求动作名称 截取 /xxx.sc
        action_name = request_path.substring(request_path.lastIndexOf('/')+1, request_path.lastIndexOf('.'));
        System.out.println("action_name: "+action_name);
        // 获取资源文件下的xml配置文件
//        controller_xml = new File(this.getClass()
//                .getResource("/controller.xml").getFile());
        controller_xml = new File(this.getClass()
                .getResource(SysConfig.getSysConfig()
                .getProperty(SCConstant.CONTROLLER_LOCATION)).getFile());
        // 获取所有action的name属性
        actionNames = XmlUtils.getActionAttributes(controller_xml, "name");
        // 判断方法是否匹配
        hasAction = false;
    }

/*    private Object doMethod(String class_name, String method_name)  {
        try{

            Class clazz = Class.forName(class_name); // 根据类名获得Class
            Object instance =  clazz.newInstance();
            Method method = instance.getClass().getDeclaredMethod(method_name);
            return method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("执行反射方法出错了...");
        }
    }*/

    /**
     * 根据Action方法的返回值对结果进行处理
     * @param action_element
     * @param result
     * @param method
     * @param request
     * @param response
     */
    private void handleResult(Element action_element, String result,String method,HttpServletRequest request,HttpServletResponse response) {
        String sel_str = MessageFormat.format("result[@name=''{0}'']",result);
//        System.out.println(sel_str);
        // 获取result节点
        Element result_element = (Element) action_element.selectSingleNode(sel_str);
        // 获取result的type value属性
        String type = result_element.attribute("type").getText();
        String value = result_element.attribute("value").getText();
       /* // 测试是否是一个request 结果:是同一个request
        System.out.println(request);
        List<User> users = (List<User>)request.getAttribute("users");
        System.out.println(users);*/
        if (value.endsWith("_view.xml")) {
            // 根据xml动态生成客户端html视图
            try {
                response.getWriter().write(Objects.requireNonNull(XmlUtils.ConvertXml2Html("/success_view2.xsl",
                        "/success_view.xml")).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                System.out.println(result);
                request.setAttribute("type",type+":"+ method);
                if ("forward".equals(type)) { // 转发到指定页面
                    request.getRequestDispatcher(value).forward(request, response);
                } else if ("redirect".equals(type)) { // 重定向到指定页面
                    response.sendRedirect(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
