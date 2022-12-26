package com.saber.spring_camel_mongodb_demo.repositories;

import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import com.saber.spring_camel_mongodb_demo.dto.Term;
import org.bson.Document;

import java.util.List;

public interface StudentRepository {
    InsertOneResult insertStudent(StudentDto studentDto);
    Document getStudentByNationalCodeAndStudentNumber(String nationalCode, String studentNumber);
    UpdateResult addTermToStudent(String nationalCode, String studentNumber, Double totalAverage, List<Term> terms);
}
