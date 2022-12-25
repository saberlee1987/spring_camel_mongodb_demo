package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.StudentDto;

public interface StudentService {
    Boolean insertStudent(StudentDto studentDto);
    StudentDto findStudentByNationalCodeAndStudentNumber(String nationalCode,String studentNumber);
}