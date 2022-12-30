package com.saber.spring_camel_mongodb_demo.routes;

import com.saber.spring_camel_mongodb_demo.dto.AddPersonResponseDto;
import com.saber.spring_camel_mongodb_demo.dto.PersonDto;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddPersonRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY))
                .routeId(Routes.ADD_PERSON_ROUTE_GATEWAY)
                .routeGroup(Routes.ADD_PERSON_ROUTE_GROUP)
                .log("Request for add person with correlation ${in.header.correlation} ===> ${in.body}")
                .to("bean-validator://person-validator")
                .setHeader(Headers.requestBody,simple("${in.body}"))
                .setHeader(Headers.nationalCode,simple("${in.body.nationalCode}"))
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE))
                .choice()
                    .when(body().isNotNull())
                       .to(String.format("direct:%s", Routes.THROWS_RESOURCE_DUPLICATION_EXCEPTION_ROUTE))
                    .otherwise()
                        .to(String.format("direct:%s",Routes.DATE_CONVERTER_ROUTE))
                        .setHeader(MongoDbConstants.OPERATION_HEADER, constant("insert"))
                        .marshal().json(JsonLibrary.Jackson, PersonDto.class)
                        .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}")
                        .log("Response for find all person with correlation ${in.header.correlation} ===> ${in.body}")
                        .to(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY_OUT))
                .end();

        from(String.format("direct:%s", Routes.ADD_PERSON_ROUTE_GATEWAY_OUT))
                .routeId(Routes.ADD_PERSON_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.ADD_PERSON_ROUTE_GROUP)
                .process(exchange -> {
                    AddPersonResponseDto responseDto = new AddPersonResponseDto();
                    responseDto.setCode(0);
                    responseDto.setText("person add to collection successfully");
                    exchange.getIn().setBody(responseDto);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
