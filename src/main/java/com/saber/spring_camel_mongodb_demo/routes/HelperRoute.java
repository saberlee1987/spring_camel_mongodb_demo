package com.saber.spring_camel_mongodb_demo.routes;

import com.saber.spring_camel_mongodb_demo.dto.person.PersonDto;
import com.saber.spring_camel_mongodb_demo.utility.PersianDateConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HelperRoute extends AbstractRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.DATE_CONVERTER_ROUTE))
                .routeId(Routes.DATE_CONVERTER_ROUTE)
                .setHeader("country", simple("${in.header.requestBody.country}"))
                .setHeader("language", simple("${in.header.requestBody.language}"))
                .setHeader("birthDate", simple("${in.header.requestBody.birthDate}"))
                .bean(PersianDateConverter.class, "convertPersianDateToGeorgian")
                .process(exchange -> {
                    PersonDto personDto = exchange.getIn().getHeader(Headers.requestBody, PersonDto.class);
                    String birthDate = exchange.getIn().getBody(String.class);
                    personDto.setBirthDate(birthDate);
                    exchange.getIn().setBody(personDto);
                });
    }
}
