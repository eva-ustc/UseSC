package ustc.sse.controller;

import org.dom4j.Element;
import org.dom4j.util.StringUtils;
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
public class SimpleController2 extends HttpServlet {
    public static final String ERROR = "pages/error.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("gbk");
        // 获取请求路径
        String path = req.getServletPath();
//        System.out.println(path);
        // 获取请求动作名称 截取 /xxx.sc
        String action = path.substring(path.lastIndexOf('/')+1,path.lastIndexOf('.'));
        System.out.println(action);
        File controller_xml = new File(this.getClass().getResource("/controller.xml").getFile());
        List<String> actionNames = XmlUtils.getActionAttributes(controller_xml, "name");
        boolean hasAction = false; // 判断方法是否匹配
        for(String actionName : actionNames){
            if (actionName.equals(action)){// 匹配成功 name=login,利用反射执行相应操作
                hasAction=true;
                // TODO 反射执行相应操作
                Element action_element = XmlUtils.getElementByAttr(controller_xml,"action","name",actionName);
                String class_name = XmlUtils.getAttrValueByName(action_element,"class");
                String method_name = XmlUtils.getAttrValueByName(action_element,"method");
                try {
                    // 利用反射执行指定方法获取方法返回值
                    String result = doMethod(class_name, method_name);

                    // 根据方法的返回值,查询次action下的result节点的name属性 跳转/重定向
                    handleResult(action_element,result,method_name,req,resp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!hasAction) { // 没有请求的方法

            resp.sendRedirect(ERROR);
        }
    }

    private String doMethod(String class_name, String method_name) throws Exception {
        Class clazz = Class.forName(class_name); // 根据类名获得Class
        Object instance =  clazz.newInstance();
        Method method = instance.getClass().getDeclaredMethod(method_name);
        return (String)method.invoke(instance);
    }

    /**
     * 根据result结果字符串处理跳转/重定向
     * @param action_element
     * @param result
     */
    private void handleResult(Element action_element, String result,String method,HttpServletRequest request,HttpServletResponse response) {
        String sel_str = MessageFormat.format("result[@name=''{0}'']",result);
        System.out.println(sel_str);
        Element result_element = (Element) action_element.selectSingleNode(sel_str);
        String type = result_element.attribute("type").getText();
        String value = result_element.attribute("value").getText();
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
