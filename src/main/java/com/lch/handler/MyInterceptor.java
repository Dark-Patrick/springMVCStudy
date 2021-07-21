package com.lch.handler;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器类：拦截用户的请求
public class MyInterceptor implements HandlerInterceptor {
    /**
     *预处理方法
     * 参数：
     *      request
     *      response
     *      handler：被拦截的控制器对象
     * 返回值：
     *      boolean
     * 特点：  1.方法在控制器方法之前先执行的，用户的请求首先到达此方法
     *        2.在这个方法可以获取请求的信息，验证请求是否符合要求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器MyInterceptor的preHandle()");
        //验证登录的用户信息
        Object attr = request.getSession().getAttribute("name");
        if(attr == null){
            request.getRequestDispatcher("/WEB-INF/jsp/tips.jsp").forward(request, response);
            return false;
        }

        String logName = "";
        logName = (String)attr;
        if(!"zs".equals(logName)){
            request.getRequestDispatcher("/WEB-INF/jsp/tips.jsp").forward(request, response);
            return false;
        }
        return true;
    }

    /**
     *后处理方法
     * 参数：
     *      handler：被拦截的控制器对象
     *      modelAndView：处理器方法的返回值
     * 特点：  1.处理器方法之后执行
     *        2.能够获取到处理器方法的返回值，可以修改modelAndView，影响最后的执行结果
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("拦截器MyInterceptor的postHandle()");
    }

    /**
     *最后执行的方法
     * 参数：
     *      handler：被拦截器的处理器对象
     *      ex：程序中发生的异常
     * 特点：  1.请求处理完成后执行的。框架中，当视图处理完成后，就认为请求处理完成
     *        2.一般做资源回收工作的，程序请求过程中创建了一些对象，在这里可以删除，把占用的资源回收
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("拦截器MyInterceptor的afterCompletion()");
    }
}
