package com.saber.spring_camel_mongodb_demo.routes;

import com.mongodb.client.model.Filters;
import com.saber.spring_camel_mongodb_demo.dto.person.DeletePersonResponseDto;
import org.apache.camel.Exchange;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class DeletePersonByNationalCodeRoute extends AbstractRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(String.format("direct:%s", Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY))
                .routeId(Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY)
                .routeGroup(Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .log("Request for delete person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation}")
                .to(String.format("direct:%s", Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE));

        from(String.format("direct:%s", Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE))
                .routeId(Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .to(String.format("direct:%s", Routes.FIND_PERSON_BY_NATIONAL_CODE_ROUTE))
                .choice()
                .when(body().isNull())
                .to(String.format("direct:%s", Routes.THROWS_RESOURCE_NOTFOUND_EXCEPTION_ROUTE))
                .otherwise()
                .setHeader(MongoDbConstants.OPERATION_HEADER, constant("remove"))
                .process(exchange -> {
                    String nationalCode = exchange.getIn().getHeader(Headers.nationalCode,String.class);
                    exchange.getIn().setHeader(MongoDbConstants.CRITERIA, Filters.eq(Headers.nationalCode, nationalCode));
                })
                .log("Request for delete person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation} ")
                .to("mongodb:{{camel.component.mongodb.mongo-connection}}?database={{spring.data.mongodb.dataBaseCollection}}&collection={{spring.data.mongodb.collection}}&outputType=Document")
                .log("Response for delete person with nationalCode ${in.header.nationalCode} with correlation ${in.header.correlation} ===> ${in.body}")
                .to(String.format("direct:%s", Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT));

        from(String.format("direct:%s", Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT))
                .routeId(Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GATEWAY_OUT)
                .routeGroup(Routes.DELETE_PERSON_BY_NATIONAL_CODE_ROUTE_GROUP)
                .marshal().json(JsonLibrary.Jackson,DeletePersonResponseDto.class)
                .unmarshal().json(JsonLibrary.Jackson,DeletePersonResponseDto.class)
                .process(exchange -> {
                    DeletePersonResponseDto responseDto = exchange.getIn().getBody(DeletePersonResponseDto.class);
                    responseDto.setCode(0);
                    responseDto.setText("your person deleted with successfully");
                    exchange.getIn().setBody(responseDto);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
