package com.saber.spring_camel_mongodb_demo;

import com.saber.spring_camel_mongodb_demo.dto.ConditionEnum;
import com.saber.spring_camel_mongodb_demo.dto.PersonResponse;
import com.saber.spring_camel_mongodb_demo.dto.StudentDto;
import com.saber.spring_camel_mongodb_demo.routes.Headers;
import com.saber.spring_camel_mongodb_demo.routes.Routes;
import com.saber.spring_camel_mongodb_demo.services.StudentService;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class SpringCamelMongodbDemoApplicationTests {

	@Autowired
	private ProducerTemplate producerTemplate;
	@Autowired
	private StudentService studentService;

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
	void testCount(){
		Exchange responseExchange = producerTemplate.send(String.format("direct:%s", Routes.FIND_ALL_PERSON_COUNT_WITH_AGE_ROUTE_GATEWAY), exchange -> {
			exchange.getIn().setHeader(Headers.correlation, UUID.randomUUID());
			exchange.getIn().setHeader(Headers.age, 45);
			exchange.getIn().setHeader(Headers.condition, ConditionEnum.GreatEqual);
		});
		String body = responseExchange.getIn().getBody(String.class);
		System.out.println(body);
	}

	@Test
	void insertStudent(){

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
	void showStudent(){
		StudentDto studentDto = studentService.findStudentByNationalCodeAndStudentNumber("0079028748", "943175737");
		System.out.println(studentDto);
	}
}
