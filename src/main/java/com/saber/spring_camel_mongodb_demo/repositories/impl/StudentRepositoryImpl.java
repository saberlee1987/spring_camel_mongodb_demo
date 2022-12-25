package com.saber.spring_camel_mongodb_demo.repositories.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceDuplicationException;
import com.saber.spring_camel_mongodb_demo.repositories.StudentRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private MongoClient mongoClient;

    @Override
    public InsertOneResult insertStudent(StudentDto studentDto) {
        MongoDatabase mydB = mongoClient.getDatabase("mydb");
        MongoCollection<Document> student = mydB.getCollection("student");
        MongoCursor<Document> studentCursor = getStudentCursorByNationalCodeAndStudentNumber(studentDto.getNationalCode(), studentDto.getStudentNumber());
        int available = studentCursor.available();
        if (available > 0){
            throw new ResourceDuplicationException(String.format("student with nationalCode %s and studentNumber %s does not exist",studentDto.getNationalCode(),
                    studentDto.getStudentNumber()));
        }
        return student.insertOne(Document.parse(studentDto.toString()));
    }

    @Override
    public Document getStudentByNationalCodeAndStudentNumber(String nationalCode, String studentNumber) {
        MongoCursor<Document> studentCursor = getStudentCursorByNationalCodeAndStudentNumber(nationalCode,studentNumber);
        if (studentCursor.available() == 0) {
           return null;
        }
        return studentCursor.next();
    }

    private MongoCursor<Document> getStudentCursorByNationalCodeAndStudentNumber(String nationalCode, String studentNumber){
        MongoDatabase mydB = mongoClient.getDatabase("mydb");
        MongoCollection<Document> student = mydB.getCollection("student");
        FindIterable<Document> studentDocuments = student.find(
                Filters.and(
                        Filters.eq("nationalCode", nationalCode),
                        Filters.eq("studentNumber", studentNumber)
                )
        );
        return studentDocuments.cursor();
    }
}