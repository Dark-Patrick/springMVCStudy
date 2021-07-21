package com.lch.service.impl;

import com.lch.dao.StudentDao;
import com.lch.entity.Student;
import com.lch.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentDao studentDao;

    @Override
    public int addStudent(Student student) {

        return studentDao.insertStudent(student);
    }

    @Override
    public List<Student> findStudent() {
        return studentDao.selectStudent();
    }
}
