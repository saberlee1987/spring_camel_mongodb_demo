package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import com.saber.spring_camel_mongodb_demo.dto.StudentTermCourseDto;

public interface StudentService {
    Boolean insertStudent(StudentDto studentDto);
    Boolean addTermToStudentTerms(StudentTermCourseDto studentTermCourseDto);
    StudentDto findStudentByNationalCodeAndStudentNumber(String nationalCode,String studentNumber);
}