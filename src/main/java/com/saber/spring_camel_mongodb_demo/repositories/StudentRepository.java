package com.saber.spring_camel_mongodb_demo.repositories;

import com.mongodb.client.result.InsertOneResult;
import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import org.bson.Document;

public interface StudentRepository {
    InsertOneResult insertStudent(StudentDto studentDto);
    Document getStudentByNationalCodeAndStudentNumber(String nationalCode, String studentNumber);
}
