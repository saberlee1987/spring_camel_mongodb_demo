package com.saber.spring_camel_mongodb_demo.repositories.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceDuplicationException;
import com.saber.spring_camel_mongodb_demo.repositories.StudentRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
        if (available > 0) {
            throw new ResourceDuplicationException(String.format("student with nationalCode %s and studentNumber %s does not exist", studentDto.getNationalCode(),
                    studentDto.getStudentNumber()));
        }
        return student.insertOne(Document.parse(studentDto.toString()));
    }

    @Override
    public Document getStudentByNationalCodeAndStudentNumber(String nationalCode, String studentNumber) {
        MongoCursor<Document> studentCursor = getStudentCursorByNationalCodeAndStudentNumber(nationalCode, studentNumber);
        if (studentCursor.available() == 0) {
            return null;
        }
        return studentCursor.next();
    }

    @Override
    public Document getStudentByNationalCode(String nationalCode) {
        MongoCursor<Document> studentCursor = getStudentCursorByNationalCode(nationalCode);
        if (studentCursor.available() == 0) {
            return null;
        }
        return studentCursor.next();
    }

    @Override
    public Document getStudentByStudentNumber(String studentNumber) {
        MongoCursor<Document> studentCursor = getStudentCursorByStudentNumber(studentNumber);
        if (studentCursor.available() == 0) {
            return null;
        }
        return studentCursor.next();
    }

    @Override
    public UpdateResult addTermToStudent(String nationalCode, String studentNumber, Double totalAverage, List<Document> termDocuments) {

        Document updateDocument = new Document();

        Document termsDocument = new Document();
        Document eachDocument = new Document();
        eachDocument.append("$each", termDocuments);
        termsDocument.append("terms", eachDocument);

        updateDocument.put("$addToSet", termsDocument);
        updateDocument.put("$set", new Document().append("totalAverage",totalAverage));
        MongoDatabase mydB = mongoClient.getDatabase("mydb");
        MongoCollection<Document> student = mydB.getCollection("student");
        return student.updateOne(Filters.and(
                Filters.eq("nationalCode", nationalCode),
                Filters.eq("studentNumber", studentNumber)
        ), updateDocument);

    }

    @Override
    public List<Document> findAllStudents() {
        MongoDatabase mydB = mongoClient.getDatabase("mydb");
        MongoCollection<Document> student = mydB.getCollection("student");
        FindIterable<Document> documents = student.find();
        List<Document> studentDocuments = new ArrayList<>();
        MongoCursor<Document> cursor = documents.cursor();
        while (cursor.hasNext()){
            studentDocuments.add(cursor.next());
        }
        return studentDocuments;
    }

    private MongoCursor<Document> getStudentCursorByNationalCodeAndStudentNumber(String nationalCode, String studentNumber) {
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

    private MongoCursor<Document> getStudentCursorByNationalCode(String nationalCode) {
        MongoDatabase mydB = mongoClient.getDatabase("mydb");
        MongoCollection<Document> student = mydB.getCollection("student");
        FindIterable<Document> studentDocuments = student.find(Filters.eq("nationalCode", nationalCode));
        return studentDocuments.cursor();
    }

    private MongoCursor<Document> getStudentCursorByStudentNumber(String studentNumber) {
        MongoDatabase mydB = mongoClient.getDatabase("mydb");
        MongoCollection<Document> student = mydB.getCollection("student");
        FindIterable<Document> studentDocuments = student.find(Filters.eq("studentNumber", studentNumber));
        return studentDocuments.cursor();
    }
}
