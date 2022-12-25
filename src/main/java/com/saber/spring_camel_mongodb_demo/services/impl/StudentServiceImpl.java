package com.saber.spring_camel_mongodb_demo.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.InsertOneResult;
import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceDuplicationException;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceNotFoundException;
import com.saber.spring_camel_mongodb_demo.repositories.StudentRepository;
import com.saber.spring_camel_mongodb_demo.services.StudentService;
import com.saber.spring_camel_mongodb_demo.utility.DateUtility;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DateUtility dateUtility;

    @Override
    public Boolean insertStudent(StudentDto studentDto) {
        if (studentRepository.getStudentByNationalCodeAndStudentNumber(
                studentDto.getNationalCode(), studentDto.getStudentNumber()) != null) {
            throw new ResourceDuplicationException(String.format("student with nationalCode %s and studentNumber %s already exist",
                    studentDto.getNationalCode(), studentDto.getStudentNumber()));
        }
        try {
            String country = studentDto.getCountry();
            String language = studentDto.getLanguage();
            String birthDate = studentDto.getBirthDate();
            if (country.trim().toLowerCase().startsWith("ir") &&
                    (language.toLowerCase().trim().startsWith("per") || language.toLowerCase().trim().startsWith("fa"))) {
                birthDate = dateUtility.convertPersianToGregorianDate(birthDate);
            }
            studentDto.setBirthDate(birthDate);
            InsertOneResult insertOneResult = studentRepository.insertStudent(studentDto);
            return insertOneResult.wasAcknowledged();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public StudentDto findStudentByNationalCodeAndStudentNumber(String nationalCode, String studentNumber) {
        Document studentDocument
                = studentRepository.getStudentByNationalCodeAndStudentNumber(nationalCode, studentNumber);
        if (studentDocument == null) {
            throw new ResourceNotFoundException(String.format("student with nationalCode %s and studentNumber %s does not exist", nationalCode, studentNumber));
        }
        try {
            return objectMapper.readValue(studentDocument.toJson(),StudentDto.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
