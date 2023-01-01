package com.saber.spring_camel_mongodb_demo;

import com.saber.spring_camel_mongodb_demo.dto.*;
import com.saber.spring_camel_mongodb_demo.dto.person.PersonResponse;
import com.saber.spring_camel_mongodb_demo.dto.student.AddStudentResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.student.Course;
import com.saber.spring_camel_mongodb_demo.dto.student.StudentDto;
import com.saber.spring_camel_mongodb_demo.dto.student.Term;
import com.saber.spring_camel_mongodb_demo.repositories.StudentRepository;
import com.saber.spring_camel_mongodb_demo.routes.Headers;
import com.saber.spring_camel_mongodb_demo.routes.Routes;
import com.saber.spring_camel_mongodb_demo.services.StudentService;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class SpringCamelMongodbDemoApplicationTests {

    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;

    //@Test
    void contextLoads() {
        Exchange responseExchange = producerTemplate.send(String.format("direct:%s", Routes.FIND_PERSON_BY_AGE_ROUTE_GATEWAY), exchange -> {
            exchange.getIn().setHeader(Headers.age, 45);
            exchange.getIn().setHeader(Headers.condition, ConditionEnum.GreatThan);
        });
        Integer statusCode = responseExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
        PersonResponse personResponse = responseExchange.getIn().getBody(PersonResponse.class);

        System.out.println(statusCode);
        System.out.println(personResponse);
    }

    @Test
    void testCount() {
        Exchange responseExchange = producerTemplate.send(String.format("direct:%s", Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GATEWAY), exchange -> {
            exchange.getIn().setHeader(Headers.correlation, UUID.randomUUID());
            exchange.getIn().setHeader(Headers.age, 45);
            exchange.getIn().setHeader(Headers.condition, ConditionEnum.GreatEqual);
        });
        String body = responseExchange.getIn().getBody(String.class);
        System.out.println(body);
    }

    @Test
    void insertStudent() {

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("saber");
        studentDto.setLastName("Azizi");
        studentDto.setNationalCode("0079028748");
        studentDto.setAge(35);
        studentDto.setEmail("saberazizi66@yahoo.com");
        studentDto.setBirthDate("1366/09/16");
        studentDto.setLanguage("persian");
        studentDto.setCountry("iran");
        studentDto.setMobile("09365627895");
        studentDto.setStudentNumber("943175737");
        studentDto.setField("computer");

        AddStudentResponseDto addStudentResponseDto = studentService.insertStudent(UUID.randomUUID().toString(), studentDto);

        System.out.println(addStudentResponseDto);

    }

    @Test
    void showStudent() {
        StudentDto studentDto = studentService.findStudentByNationalCodeAndStudentNumber(UUID.randomUUID().toString(),"0079028748", "943175737");
        System.out.println(studentDto);
    }

    @Test
    void addTermToStudent() {

        String nationalCode = "0079028748";
        String studentNumber = "943175737";
        StudentDto student = studentService.findStudentByNationalCodeAndStudentNumber(UUID.randomUUID().toString(),nationalCode, studentNumber);

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
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("java3", 16.56, 3));
        courses.add(new Course("php3", 17.57, 2));
        courses.add(new Course("c#3", 16.58, 3));
        term.setCourses(courses);
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

        studentRepository.addTermToStudent("0079028748", "943175737", totalAverage, termDocuments);
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
}
