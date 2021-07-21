package com.lch.service;

import com.lch.entity.Student;

import java.util.List;

public interface StudentService {
    int addStudent(Student student);

    List<Student> findStudent();

}
