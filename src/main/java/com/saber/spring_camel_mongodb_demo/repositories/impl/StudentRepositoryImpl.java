package com.saber.spring_camel_mongodb_demo.repositories.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import com.saber.spring_camel_mongodb_demo.dto.Term;
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
    @Autowired
    private ObjectMapper objectMapper;

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
    public UpdateResult addTermToStudent(String nationalCode, String studentNumber, Double totalAverage, List<Term> terms) {


        List<Document> termsDocument = new ArrayList<>();
        Document termDocument = new Document();

        for (Term term : terms) {
            //term.
        }


        Document updateDocument = new Document();
        //BasicDBObject basicDBObject = new BasicDBObject();
        //Gson gson = new Gson();


        // Object[] termDocuments = (Object[]) JSON.parse(gson.toJson(terms));


        //BasicDBList termsObj = new BasicDBList();
//        basicDBObject.put("terms",terms);
        //termDocument.put("terms", termsObj);
        //termDocument.put("totalAverage", totalAverage);
       // updateDocument.put("$push", basicDBObject);

        MongoDatabase mydB = mongoClient.getDatabase("mydb");
        MongoCollection<Document> student = mydB.getCollection("student");
        return student.updateOne(Filters.and(
                Filters.eq("nationalCode", nationalCode),
                Filters.eq("studentNumber", studentNumber)
        ), updateDocument);

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
}
