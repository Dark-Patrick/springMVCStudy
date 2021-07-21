package com.lch.dao;

import com.lch.entity.Student;

import java.util.List;

public interface StudentDao {
    int insertStudent(Student student);

    List<Student> selectStudent();
}
