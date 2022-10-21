package com.saber.spring_camel_mongodb_demo.services;

import com.saber.spring_camel_mongodb_demo.dto.AddPersonResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.PersonDto;
import com.saber.spring_camel_mongodb_demo.dto.PersonResponse;

public interface PersonService {
    PersonResponse findAllPerson(String correlation);
    PersonDto findPersonByNationalCode(String correlation,String nationalCode);
    AddPersonResponseDto addPerson(PersonDto personDto,String correlation);
}
