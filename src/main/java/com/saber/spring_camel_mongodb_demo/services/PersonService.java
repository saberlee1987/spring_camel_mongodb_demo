package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.*;

public interface PersonService {
    PersonResponse findAllPerson(String correlation);
    PersonResponse findAllPersonByAgeWithCondition(String correlation, Integer age, ConditionEnum conditionEnum);
    PersonResponse findAllPersonByCountry(String correlation, String country);
    PersonResponse findAllPersonByCountryAndLanguage(String correlation, String country,String language);
    PersonResponseCountDto findAllPersonByCountryCount(String correlation, String country);
    PersonResponseCountDto findAllPersonByAgeWithConditionCount(String correlation, Integer age, ConditionEnum conditionEnum);
    PersonResponseCountDto findAllPersonByCountryAndLanguageCount(String correlation, String country,String language);
    PersonDto findPersonByNationalCode(String correlation,String nationalCode);
    AddPersonResponseDto addPerson(PersonDto personDto,String correlation);
}
