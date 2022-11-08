package com.saber.spring_camel_mongodb_demo.routes;

import com.saber.spring_camel_mongodb_demo.dto.PersonDto;
import com.saber.spring_camel_mongodb_demo.utility.DateUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HelperRoute extends AbstractRestRouteBuilder{

    private final DateUtility dateUtility;

    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s",Routes.DATE_CONVERTER_ROUTE))
                .routeId(Routes.DATE_CONVERTER_ROUTE)
                .process(exchange -> {
                    PersonDto personDto = exchange.getIn().getHeader(Headers.requestBody,PersonDto.class);
                    String country = personDto.getCountry();
                    String language = personDto.getLanguage();
                    String birthDate = personDto.getBirthDate();
                    if (country.trim().toLowerCase().startsWith("ir") &&
                            (language.toLowerCase().trim().startsWith("per") ||  language.toLowerCase().trim().startsWith("fa")) ){
                        birthDate = dateUtility.convertPersianToGregorianDate(birthDate);
                    }
                    personDto.setBirthDate(birthDate);
                    exchange.getIn().setBody(personDto);
                });
    }
}
