package com.lch.controller;

import com.lch.entity.Student;
import com.lch.exception.AgeException;
import com.lch.exception.MyUserException;
import com.lch.exception.NameException;
import com.lch.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    //注册学生
    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addStudent(Student student) throws MyUserException {
        if(student.getAge() > 100){
            throw new AgeException("年龄太大了");
        }
        if("zs".equals(student.getName())){
            throw new NameException("张三不允许注册");
        }
        int nums = studentService.addStudent(student);
        Map<String, Object> map = new HashMap<>();
        if(nums > 0){
            map.put("success", true);
            map.put("student", student);
        }else {
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping("/showStudent")
    @ResponseBody
    public List<Student> showStudent(){
        System.out.println("执行StudentController中的showStudent()");
        return studentService.findStudent();
    }

}
