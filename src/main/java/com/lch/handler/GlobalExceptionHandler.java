package com.lch.handler;

import com.lch.exception.AgeException;
import com.lch.exception.NameException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ControllerAdvice: 控制器增强（给控制器类增加功能--异常处理功能）
 *      位置：类的上面
 *      特点：必须让框架知道这个注解所在包名，需要在mvc配置文件声明组件扫描器
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //定义方法，处理发生的异常
    //处理异常的方法和控制器方法的定义一样

    //形参Exception，表示controller中抛出的异常对象，通过形参可以获取发生的异常信息
    //@ExceptionHandler（异常的class）：表示异常的类型，当发生此类型异常时，都当前方法处理
    @ExceptionHandler(NameException.class)
    public ModelAndView doNameException(Exception exception){
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg", "张三不允许注册");
        mv.addObject("exception", exception);
        mv.setViewName("nameError");
        return mv;
    }

    @ExceptionHandler(AgeException.class)
    public ModelAndView doAgeException(Exception exception){
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg", "年龄太大");
        mv.addObject("exception", exception);
        mv.setViewName("ageError");
        return mv;
    }

    //处理未知异常
    @ExceptionHandler
    public ModelAndView doOtherException(Exception exception){
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg", "未知异常");
        mv.addObject("exception", exception);
        mv.setViewName("defaultError");
        return mv;
    }
}
