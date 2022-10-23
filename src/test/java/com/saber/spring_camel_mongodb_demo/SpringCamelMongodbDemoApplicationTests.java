package com.saber.spring_camel_mongodb_demo;

import com.saber.spring_camel_mongodb_demo.dto.ConditionEnum;
import com.saber.spring_camel_mongodb_demo.dto.PersonResponse;
import com.saber.spring_camel_mongodb_demo.routes.Headers;
import com.saber.spring_camel_mongodb_demo.routes.Routes;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringCamelMongodbDemoApplicationTests {

	@Autowired
	private ProducerTemplate producerTemplate;

	@Test
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

}
