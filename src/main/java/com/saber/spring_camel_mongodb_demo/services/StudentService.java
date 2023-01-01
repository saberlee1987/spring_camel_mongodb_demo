package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.student.*;

public interface StudentService {
    AddStudentResponseDto insertStudent(String correlation , StudentDto studentDto);
    AddTermStudentResponseDto addTermToStudentTerms(String correlation , StudentTermCourseDto studentTermCourseDto);
    StudentDto findStudentByNationalCodeAndStudentNumber(String correlation,String nationalCode,String studentNumber);
    StudentDto findPersonByNationalCode(String correlation, String nationalCode);
    StudentDto findPersonByStudentNumber(String correlation, String studentNumber);
    StudentResponse findAllStudents(String correlation);
}