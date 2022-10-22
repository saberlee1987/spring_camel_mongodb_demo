package com.saber.spring_camel_mongodb_demo.services.impl;

import com.saber.spring_camel_mongodb_demo.dto.*;
import com.saber.spring_camel_mongodb_demo.exceptions.GatewayException;
import com.saber.spring_camel_mongodb_demo.routes.Headers;
import com.saber.spring_camel_mongodb_demo.routes.Routes;
import com.saber.spring_camel_mongodb_demo.services.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final ProducerTemplate producerTemplate;

    @Override
    public PersonResponse findAllPerson(String correlation) {
        Exchange responseExchange = producerTemplate.send(String.format("direct:%s", Routes.FIND_ALL_PERSON_ROUTE_GATEWAY), exchange -> {
            exchange.getIn().setHeader(Headers.correlation, correlation);
        });
        checkException("findAllPerson", responseExchange, correlation);
        PersonResponse response = responseExchange.getIn().getBody(PersonResponse.class);
        log.info("Response for findAllPerson ===> {}", response);
        return response;
    }

    @Override
    public PersonResponse findAllPersonByAgeWithCondition(String correlation, Integer age, ConditionEnum conditionEnum) {
        Exchange responseExchange = producerTemplate.send(String.format("direct:%s", Routes.FIND_PERSON_BY_AGE_ROUTE_GATEWAY), exchange -> {
            exchange.getIn().setHeader(Headers.correlation, correlation);
            exchange.getIn().setHeader(Headers.condition, conditionEnum);
            exchange.getIn().setHeader(Headers.age, age);
        });
        checkException("findAllPersonByAgeWithCondition", responseExchange, correlation);
        PersonResponse response = responseExchange.getIn().getBody(PersonResponse.class);
        log.info("Response for findAllPersonByAgeWithCondition ===> {}", response);
        return response;
    }

    @Override
    public PersonDto findPersonByNationalCode(String correlation, String nationalCode) {
        Exchange responseExchange = producerTemplate.send(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY), exchange -> {
            exchange.getIn().setHeader(Headers.correlation, correlation);
            exchange.getIn().setHeader(Headers.nationalCode, nationalCode);
        });
        checkException("findPersonByNationalCode", responseExchange, correlation);
        PersonDto response = responseExchange.getIn().getBody(PersonDto.class);
        log.info("Response for findAllPerson ===> {}", response);
        return response;
    }

    @Override
    public AddPersonResponseDto addPerson(PersonDto personDto, String correlation) {
        Exchange responseExchange = producerTemplate.send(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY), exchange -> {
            exchange.getIn().setHeader(Headers.correlation, correlation);
            exchange.getIn().setBody(personDto);
        });
        checkException("addPerson", responseExchange, correlation);
        AddPersonResponseDto response = responseExchange.getIn().getBody(AddPersonResponseDto.class);
        log.info("Response for addPerson ===> {}", response);
        return response;
    }

    private void checkException(String methodName, Exchange responseExchange, String correlation) {
        int statusCode = responseExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
        if (statusCode != HttpStatus.OK.value()) {
            ErrorResponse errorResponse = responseExchange.getIn().getBody(ErrorResponse.class);
            log.error("Error for correlation : {} , {} , statusCode {} , with body {}", correlation, methodName, statusCode, errorResponse);
            throw new GatewayException(statusCode, correlation, errorResponse);
        }
    }
}
