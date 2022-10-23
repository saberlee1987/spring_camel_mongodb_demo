package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.AddPersonResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.ConditionEnum;
import com.saber.spring_camel_mongodb_demo.dto.PersonDto;
import com.saber.spring_camel_mongodb_demo.dto.PersonResponse;

public interface PersonService {
    PersonResponse findAllPerson(String correlation);
    PersonResponse findAllPersonByAgeWithCondition(String correlation, Integer age, ConditionEnum conditionEnum);
    PersonResponse findAllPersonByCountry(String correlation, String country);
    PersonResponse findAllPersonByCountryAndLanguage(String correlation, String country,String language);
    PersonDto findPersonByNationalCode(String correlation,String nationalCode);
    AddPersonResponseDto addPerson(PersonDto personDto,String correlation);
}
