package com.saber.spring_camel_mongodb_demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.spring_camel_mongodb_demo.dto.*;
import com.saber.spring_camel_mongodb_demo.repositories.StudentRepository;
import com.saber.spring_camel_mongodb_demo.routes.Headers;
import com.saber.spring_camel_mongodb_demo.routes.Routes;
import com.saber.spring_camel_mongodb_demo.services.StudentService;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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

    @Autowired
    private ObjectMapper objectMapper;

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

        Boolean insertStudentResult = studentService.insertStudent(studentDto);

        System.out.println(insertStudentResult);

    }

    @Test
    void showStudent() {
        StudentDto studentDto = studentService.findStudentByNationalCodeAndStudentNumber("0079028748", "943175737");
        System.out.println(studentDto);
    }

    @Test
    void addTermToStudent() {

        String nationalCode = "0079028748";
        String studentNumber = "943175737";
        StudentDto student = studentService.findStudentByNationalCodeAndStudentNumber(nationalCode, studentNumber);
        double totalAverage = student.getTotalAverage() == null ? 0 : student.getTotalAverage();
        Term term = new Term();
        term.setTermNumber(1);
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("java", 19.5, 3));
        courses.add(new Course("php", 17.5, 2));
        term.setCourses(courses);
        List<Term> terms = student.getTerms();
        if (terms == null) {
            terms = new ArrayList<>();
        }

        double averageSum = 0.0;
        for (Course course : term.getCourses()) {
            averageSum += course.getNumberUnitCourse() * course.getScore();
            term.setAverage(averageSum);
        }
        terms.add(term);

        totalAverage += averageSum;
        studentRepository.addTermToStudent("0079028748", "943175737", totalAverage, terms);
    }

    @Test
    void testJson() {

        String json = "[{\"courses\":[{\"title\":\"java\",\"score\":19.5,\"numberUnitCourse\":3},{\"title\":\"php\",\"score\":17.5,\"numberUnitCourse\":2}],\"termNumber\":1,\"average\":93.5}]";
        json = "{\n" +
                "         \"studentNumber\" : \"943175737\",\n" +
                "        \"field\" : \"computer\",\n" +
                "        \"firstName\" : \"saber\",\n" +
                "        \"lastName\" : \"Azizi\",\n" +
                "        \"nationalCode\" : \"0079028748\",\n" +
                "        \"age\" : 35,\n" +
                "        \"email\" : \"saberazizi66@yahoo.com\",\n" +
                "        \"mobile\" : \"09365627895\",\n" +
                "        \"country\" : \"iran\",\n" +
                "        \"language\" : \"persian\",\n" +
                "        \"birthDate\" : \"1987-12-07\",\n" +
                "        \"terms\" : \"[{\\\"courses\\\":[{\\\"title\\\":\\\"java\\\",\\\"score\\\":19.5,\\\"numberUnitCourse\\\":3},{\\\"title\\\":\\\"php\\\",\\\"score\\\":17.5,\\\"numberUnitCourse\\\":2}],\\\"termNumber\\\":1,\\\"average\\\":93.5}]\",\n" +
                "        \"totalAverage\" : 93.5\n" +
                "}";
       try {
           StudentDto studentDto= objectMapper.readValue(json, new TypeReference<StudentDto>() {
           });
           System.out.println(studentDto);
       }catch (Exception ex){
           ex.printStackTrace();
       }


    }
}
