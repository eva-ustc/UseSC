package ustc.sse.controller;

import org.dom4j.Element;
import ustc.sse.domain.User;
import ustc.sse.proxy.ActionProxy;
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

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.controller
 * @date 2018/11/26 22:37
 * @description God Bless, No Bug!
 */
public class SimpleControllerProxy extends HttpServlet {
    private static final String ERROR = "pages/error.jsp";
    private static final String PRE_EXECUTION = "predo";
    private static final String AFTER_EXECUTION = "afterdo";
    private String request_path;
    private String action_name;
    private File controller_xml;
    private List<String> actionNames;
    private boolean hasAction;
    private boolean hasInterceptor;
    private String interceptor_name;
    private String result;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initParams(req, resp);
        for(String actionName : actionNames){
            if (actionName.equals(action_name)){// 匹配成功 name=login,利用反射执行相应操作
                hasAction =true;
                //  获取当前action节点
                Element action_element = XmlUtils.getElementByAttr(controller_xml,"action","name",actionName);

                // 判断是否存在interceptor-ref节点
                List<Element> interceptor_ref_elements = action_element.elements("interceptor-ref");

/*                if (interceptor_ref_elements !=null){
                    hasInterceptor = true;
                    for(Element interceptor_ref_element : interceptor_ref_elements){

                        interceptor_name = interceptor_ref_element.attribute("name").getText();
                        doInterceptorProxy(interceptor_name);
//                        doInterceptor(interceptor_name,PRE_EXECUTION);
                    }
                }*/

                String class_name = XmlUtils.getAttrValueByName(action_element,"class");
                String method_name = XmlUtils.getAttrValueByName(action_element,"method");
                try {
                    // 创建被代理对象和代理对象,调用代理对象的method
                    Class clazz = Class.forName(class_name);
                    ActionProxy actionProxy = new ActionProxy();

                    Object target = clazz.newInstance();
                    Object proxy = actionProxy.getProxy(target);

                    User user = new User();
                    user.setUserName(req.getParameter("username"));
                    user.setUserPass(req.getParameter("password"));
                    // 调用代理对象加强业务方法
                    Method method = clazz.getDeclaredMethod(method_name,String.class,User.class);

                    String result = (String) method.invoke(proxy,action_name,user);
                    // 根据方法的返回值,查询次action下的result节点的name属性 跳转/重定向
                    handleResult(action_element, result,method_name,req,resp);
                    /*// 利用反射执行指定方法获取方法返回值
                    result = (String) doMethod(class_name, method_name);

                    if (hasInterceptor){ // 执行afterdo 方法
                        for(Element interceptor_ref_element : interceptor_ref_elements){
                            interceptor_name = interceptor_ref_element.attribute("name").getText();
//                            doInterceptor(interceptor_name,AFTER_EXECUTION);
                        }
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!hasAction) { // 没有匹配请求的方法

            resp.sendRedirect(ERROR);
        }
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
        request_path = req.getServletPath();
//        System.out.println(path);
        // 获取请求动作名称 截取 /xxx.sc
        action_name = request_path.substring(request_path.lastIndexOf('/')+1, request_path.lastIndexOf('.'));
        System.out.println("action_name: "+action_name);
        // 获取资源文件下的xml配置文件
        controller_xml = new File(this.getClass().getResource("/controller.xml").getFile());
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

        if (value.endsWith("_view.xml")) {
            // 根据xml动态生成客户端html视图
            try {
                response.getWriter().write(XmlUtils.ConvertXml2Html("/success_view2.xsl",
                        "/success_view.xml").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {

            try {
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
