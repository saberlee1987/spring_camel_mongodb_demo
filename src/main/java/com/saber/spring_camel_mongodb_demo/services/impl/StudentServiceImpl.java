package com.saber.spring_camel_mongodb_demo.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.saber.spring_camel_mongodb_demo.dto.*;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceDuplicationException;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceNotFoundException;
import com.saber.spring_camel_mongodb_demo.repositories.StudentRepository;
import com.saber.spring_camel_mongodb_demo.services.StudentService;
import com.saber.spring_camel_mongodb_demo.utility.DateUtility;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DateUtility dateUtility;

    @Override
    public AddStudentResponseDto insertStudent(String correlation, StudentDto studentDto) {
        checkStudentDuplicate(studentDto.getNationalCode(), studentDto.getStudentNumber());
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
            boolean acknowledged = insertOneResult.wasAcknowledged();
            AddStudentResponseDto addStudentResponseDto = new AddStudentResponseDto();
            if (acknowledged) {
                addStudentResponseDto.setCode(0);
                addStudentResponseDto.setText("student inserted successfully");
            } else {
                addStudentResponseDto.setCode(-1);
                addStudentResponseDto.setText("student failed inserted");
            }
            return addStudentResponseDto;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public AddTermStudentResponseDto addTermToStudentTerms(String correlation, StudentTermCourseDto studentTermCourseDto) {

        String nationalCoe = studentTermCourseDto.getNationalCode();
        String studentNumber = studentTermCourseDto.getStudentNumber();

        StudentDto student = findStudentByNationalCodeAndStudentNumber(correlation, nationalCoe, studentNumber);

        // calculate old terms
        List<Term> oldTerms = student.getTerms() == null ? Collections.emptyList() : student.getTerms();
        int numberTermsCount = oldTerms.size();
        int numberTerm = numberTermsCount + 1;

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Double oldTermAverageSum = 0.00;
        for (Term oldTerm : oldTerms) {
            oldTermAverageSum += oldTerm.getAverage();
        }
        // add term from user
        Term term = new Term();
        term.setTermNumber(numberTerm);
        // add course from user
        term.setCourses(studentTermCourseDto.getCourses());
        // add course from user

        int numberUnitCourses = 0;
        double averageSum = 0.0;
        for (Course course : term.getCourses()) {
            numberUnitCourses += course.getNumberUnitCourse();
            averageSum += course.getNumberUnitCourse() * course.getScore();
        }
        Double termAverage = averageSum / numberUnitCourses;
        termAverage = Double.parseDouble(decimalFormat.format(termAverage));
        term.setAverage(termAverage);
        // add term from user

        numberTermsCount += 1;
        double totalAverage = (termAverage + oldTermAverageSum) / (numberTermsCount);
        totalAverage = Double.parseDouble(decimalFormat.format(totalAverage));
        List<Document> termDocuments = getTermDocuments(term);
        UpdateResult updateResult = studentRepository.addTermToStudent(nationalCoe, studentNumber, totalAverage, termDocuments);

        AddTermStudentResponseDto addTermStudentResponseDto = new AddTermStudentResponseDto();

        long modifiedCount = updateResult.getModifiedCount();
        if (modifiedCount > 0) {
            addTermStudentResponseDto.setCode(0);
            addTermStudentResponseDto.setText("student terms added successfully");
        } else {
            addTermStudentResponseDto.setCode(-1);
            addTermStudentResponseDto.setText("student terms added failed");
        }
        return addTermStudentResponseDto;
    }

    @Override
    public StudentDto findStudentByNationalCodeAndStudentNumber(String correlation, String nationalCode, String studentNumber) {
        Document studentDocument = checkStudentNotFound(nationalCode, studentNumber);
        return getStudentFromDocument(studentDocument);
    }

    @Override
    public StudentDto findPersonByNationalCode(String correlation, String nationalCode) {
        Document studentDocument = checkStudentNotFoundWithNationalCode(nationalCode);
        return getStudentFromDocument(studentDocument);
    }

    @Override
    public StudentDto findPersonByStudentNumber(String correlation, String studentNumber) {
        Document studentDocument = checkStudentNotFoundWithStudentNumber(studentNumber);
        return getStudentFromDocument(studentDocument);
    }

    @Override
    public StudentResponse findAllStudents(String correlation) {
        List<Document> studentDocuments = studentRepository.findAllStudents();
        StudentResponse studentResponse = new StudentResponse();
        List<StudentDto> students = new ArrayList<>();
        for (Document studentDocument : studentDocuments) {
            students.add(getStudentFromDocument(studentDocument));
        }
        studentResponse.setStudents(students);
        return studentResponse;

    }

    private Document checkStudentNotFound(String nationalCode, String studentNumber) {
        Document studentDocument = studentRepository.getStudentByNationalCodeAndStudentNumber(
                nationalCode, studentNumber);
        if (studentDocument == null) {
            throw new ResourceNotFoundException(String.format("student with nationalCode %s and studentNumber %s does not exist", nationalCode, studentNumber));
        }
        return studentDocument;
    }

    private Document checkStudentNotFoundWithNationalCode(String nationalCode) {
        Document studentDocument = studentRepository.getStudentByNationalCode(
                nationalCode);
        if (studentDocument == null) {
            throw new ResourceNotFoundException(String.format("student with nationalCode %s  does not exist", nationalCode));
        }
        return studentDocument;
    }

    private Document checkStudentNotFoundWithStudentNumber(String studentNumber) {
        Document studentDocument = studentRepository.getStudentByStudentNumber(
                studentNumber);
        if (studentDocument == null) {
            throw new ResourceNotFoundException(String.format("student with studentNumber %s  does not exist", studentNumber));
        }
        return studentDocument;
    }

    private void checkStudentDuplicate(String nationalCode, String studentNumber) {
        if (studentRepository.getStudentByNationalCodeAndStudentNumber(
                nationalCode, studentNumber) == null) {
            throw new ResourceDuplicationException(String.format("student with nationalCode %s and studentNumber %s already exist",
                    nationalCode, studentNumber));
        }
    }

    private List<Document> getTermDocuments(Term term) {
        List<Document> termDocuments = new ArrayList<>();
        Document termDocument = new Document();
        termDocument.put("termNumber", term.getTermNumber());
        termDocument.put("average", term.getAverage());
        List<Document> courseDocuments = new ArrayList<>();
        for (Course course : term.getCourses()) {
            Document courseDocument = new Document();
            courseDocument.put("title", course.getTitle());
            courseDocument.put("score", course.getScore());
            courseDocument.put("numberUnitCourse", course.getNumberUnitCourse());
            courseDocuments.add(courseDocument);
        }
        termDocument.put("courses", courseDocuments);
        termDocuments.add(termDocument);
        return termDocuments;
    }

    private StudentDto getStudentFromDocument(Document studentDocument) {
        try {
            return objectMapper.readValue(studentDocument.toJson(), StudentDto.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
